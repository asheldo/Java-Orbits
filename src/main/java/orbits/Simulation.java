package orbits;

import com.google.common.collect.EvictingQueue;
import com.sun.media.jfxmedia.logging.Logger;
import orbits.calc.Collisions;
import orbits.calc.Gravity;
import orbits.calc.Positions;
import orbits.model.Planet;
import orbits.ui.Board;
import orbits.ui.GUIMenu;
import orbits.ui.PlanetsCoincideError;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Created by asheldon on 7/4/16.
 */
public class Simulation {

    private static Simulation simulation = new Simulation();

    private double totalMass;
    private Collisions collisions;
    private Gravity gravity;
    private Mover mover;
    private MoveExecutor executor;

    private OrbitsConfigOptions options;
    private final OrbitsUIOptions optionsUI;

    // Sorted large to small
    private Comparator<Planet> sized;

    private long moves = 0;

    private EvictingQueue<Positions> savedPositionsFifo;
    private boolean isRunning;

    private Planet fixedCenter = new Planet(0, 0, 0, 0, 0, true);
    private boolean needRandomizeOnCircle;
    private boolean needCenterOnBody;
    private int centerPlanet = 0;


    public static Simulation getInstance() {
        return simulation;
    }

    private List<Planet> drawPlanets;
    private final GUIMenu handle;

    private Simulation() {
        this.handle = new GUIMenu("Orbits");
        this.drawPlanets = new ArrayList<Planet>();
        this.collisions = new Collisions(this);
        this.gravity = new Gravity(this);
        this.savedPositionsFifo = EvictingQueue.create(1000);
        this.options = new OrbitsConfigOptions(this);
        this.optionsUI = new OrbitsUIOptions(this);
        this.executor = new MoveExecutor();

        this.sized = new Comparator<Planet>() {
            @Override
            public int compare(Planet o1, Planet o2) {
                Double s1 = o1.getMass();
                return -1 * s1.compareTo(o2.getMass());
            }
        };
    }

    public long getMoves() {
        return moves;
    }
    public OrbitsConfigOptions getOptions() {
        return options;
    }

    public GUIMenu getHandle() {
        return handle;
    }

    public void start() {
        handle.start();
    }

    public void restart() {
        drawPlanets = new ArrayList<Planet>();
    }

    public int getPlanetCount() {
        return drawPlanets.size();
    }

    // getDrawPlanets() { return Collections.unmodifiableList(drawPlanets); }
    public Planet getDrawPlanet(int i) {
        return drawPlanets.get(i);
    }

    /**
     *
     * @return same order
     */
    public Iterator<Planet> planetIterator() {
        return drawPlanets.listIterator();
    }

    /**
     * @return decreasing size order, unmodifiable
     */
    public Iterator<Planet> planetSizeIterator() {
        LinkedList<Planet> sorted = new LinkedList<Planet>(drawPlanets);
        Collections.sort(sorted, sized);
        return Collections.unmodifiableList(sorted).listIterator();
    }

    public Positions savePositions() {
        Positions pos = new Positions(drawPlanets);
        savedPositionsFifo.add(pos);
        return pos;
    }

    /**
     * And add
     */
    public Planet buildPlanet(double tryX, double tryY, int tryMass, double tryDX, double tryDY, boolean tryFixed) {
        double factorX = 1./getReductionFactorX();
        // double factorY = 1./getReductionFactorY();
        // builder sets board too
        Planet planet = new Planet(
                tryX * factorX,
                tryY * factorX,
                (int) (tryMass),
                tryDX * Math.pow(factorX, 0.5),
                tryDY * Math.pow(factorX, 0.5),
                tryFixed);
        addPlanet(planet);
        return planet;
    }

    public double getReductionFactorX() {
        Board board = getHandle().getBoard();
        double fact = board.getWidth() / (getOptions().getHalfMaxDimension() * 2);
        return fact;
    }

    public double getReductionFactorY() {
        Board board = getHandle().getBoard();
        double fact = board.getHeight() / (getOptions().getHalfMaxDimension() * 2);
        return fact;
    }

    public int notEmpty(double tryX, double tryY) {
        int i = 0;
        int errorIndex = -1;
        Iterator<Planet> iter = planetIterator();
        while (iter.hasNext()) {
            Planet p = iter.next();
            if ((Math.sqrt(Math.pow(p.x() - tryX, 2) + Math.pow(p.y() - tryY, 2))
                    < 10)) {
                tryX += 11;
                errorIndex = i;
            }
            ++i;
        }
        return errorIndex;
    }

    public void addPlanet(Planet planet) {
        drawPlanets.add(planet);
        totalMass += planet.getMass();
    }

    public double getTotalMass() {
        return totalMass;
    }

    public void addFragment(Planet planet) {
        drawPlanets.add(planet);
    }

    public void setPlanet(int i, Planet planet) {
        drawPlanets.set(i, planet);
    }

    public Collisions getCollisions() {
        return collisions;
    }

    public Gravity getGravity() {
        return gravity;
    }

    @Override
    public String toString() {
        return drawPlanets.toString();
    }

    public void logState(Object o) {
        if (options.logLevel == Level.INFO) {
            System.out.println(o + "\n\t -> " + this.toString());
        }
    }

    public void logState(Object o, Level level) {
        if (options.logLevel.intValue() <= level.intValue()) {
            System.out.println(o + "\n\t -> Positions #" +
                    (this.savedPositionsFifo.isEmpty() ? 0 : this.savedPositionsFifo.peek().index));
        }
    }

    public void logStateForced(Object o) {
        if (options.logLevel != Level.SEVERE) {
            System.out.println(o + "\n\t -> " + this.toString());
        }
    }

    public void movePlanets() {
        if (!optionsUI.isMoveThreaded()) {
            movePlanetsOnce();
        } else {
            startMover();
        }
    }

    protected void startMover() {
        if (mover == null) {
            mover = new Mover(this);
            mover.resume();
        }
    }

    protected void stopMover() {
        if (mover != null) {
            mover.pause();
        }
        mover = null;
    }

    public Planet getFixedCenter() {
        return fixedCenter;
    }

    public void setFixedCenter(Planet fix) {
        fixedCenter = fix;
    }

    public void needCenterOnBody(boolean centerMajor) {
        if (centerMajor) {
            centerPlanet = 0;
        } else {
            int count = getPlanetCount();
            if (centerPlanet >= count) {
                centerPlanet = 1;
            } else {
                centerPlanet += (count > 100 ? 5 : 1);
            }
        }
        if (isRunning()) {
            this.needCenterOnBody = true;
        } else {
            centerOnBody();
            this.needCenterOnBody = false;
        }
    }

    public void needRandomizeOnCircle() {
        if (isRunning()) {
            this.needRandomizeOnCircle = true;
        } else {
            randomizeOnCircle();
            this.needRandomizeOnCircle = false;
        }
    }

    /**
     *
     */
    public static class MoveExecutor {
        int poolSize = 2, max = 2, keepAlive = 60000;
        BlockingQueue blockingQ = new PriorityBlockingQueue();

        ThreadPoolExecutor executor;

        public void execute(Runnable mover) {
            executor = new ThreadPoolExecutor(
                    poolSize, max, keepAlive, TimeUnit.MILLISECONDS, blockingQ);
            executor.execute(mover);
        }
        public void kill() {
            executor.shutdown();
        }
    }

    /**
     *
     */
    public static class Mover implements Runnable {
        private final Simulation sim;

        public Mover(Simulation sim) {
            this.sim = sim;
        }
        public void pause() {
            sim.executor.kill();
        }
        public void resume() {
            sim.executor.execute(this);
        }
        @Override
        public void run() {
            while (sim.isRunning()) {
                if (sim.needCenterOnBody) {
                    sim.centerOnBody();
                    sim.needCenterOnBody = false;
                }
                if (sim.needRandomizeOnCircle) {
                    sim.randomizeOnCircle();
                    sim.needRandomizeOnCircle = false;
                } else {
                    sim.movePlanetsOnce();
                }
                if (sim.options.getSleep() > 0
                        || (sim.getPlanetCount() < 50 && sim.moves % 5 == 0))
                    try {
                        Thread.sleep(sim.options.getSleep() + 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
            sim.logState("Pausing run");
        }
    }

    public void randomizeOnCircle() {
        // sim.setFixedCenter(center);
        Iterator<Planet> planets = planetIterator();
        Planet center = planets.next();
        while (planets.hasNext()) {
            Planet p = planets.next();
            p.randomizeOnCircle(getOptions().dt);
        }
        // sim.logState("Randomized: " + sim.getDrawPlanets().toString());

    }

    protected void centerOnBody() {
        int i = 0;
        Planet center = null;
        Iterator<Planet> planets = planetIterator();
        while (planets.hasNext() && i++ <= centerPlanet) {
            center = planets.next();
            if (i == 1) {
                setFixedCenter(center);
            }
        }
        double dcx = center.x(), dcy = center.y();
        center.setX(0);
        center.setY(0);
        if (centerPlanet == 0) {
            // center.setFixed(true);
        }
        else {
            planets = planetSizeIterator();
        }
        while (planets.hasNext()) {
            Planet p = planets.next();
            if (!p.equals(center)) {
                p.setX(p.x() - dcx);
                p.setY(p.y() - dcy);
            }
        }
        // sim.logState("Centered: " + sim.getDrawPlanets().toString());
    }

    protected void movePlanetsOnce() {
        ++moves;
        gravity.movePlanets();

        // Checks for happening collisions before moving
        collisions.check();

        // Moves
        // for (Planet p : drawPlanets) { p.move(consts.dt); }
        Iterator<Planet> iter = simulation.planetIterator();
        while (iter.hasNext()) {
           iter.next().move(options.dt);
        }
        // Memento
        Positions pos = savePositions();
        if (options.logEach > 0 && pos.index % options.logEach == 0) {
            logState(pos);
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
        stopMover();
    }
}
