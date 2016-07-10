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
 *
 * TODO Where is
 */
public class Simulation {

    private static Simulation simulation = new Simulation();
    private Collisions collisions;
    private Gravity gravity;
    private Mover mover;
    private MoveExecutor executor;

    private OrbitsConfigOptions options;
    private final OrbitsUIOptions optionsUI;

    // Sorted large to small
    private Comparator<Planet> sized;

    private EvictingQueue<Positions> savedPositionsFifo;
    private boolean isRunning;

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

    public OrbitsConfigOptions getOptions() {
        return options;
    }

    public GUIMenu getHandle() {
        return handle;
    }

    public int getPlanetCount() {
        return drawPlanets.size();
    }

    /**
     * @return new UnmodifiableRandomAccessList
     */
    public List<Planet> getDrawPlanets() {
        return Collections.unmodifiableList(drawPlanets);
    }

    protected void setDrawPlanets(List<Planet> list) {
        drawPlanets = list;
    }

    public Planet getDrawPlanet(int i) {
        return drawPlanets.get(i);
    }

    /**
     * And add
     */
    public Planet buildPlanet(double tryX, double tryY, int tryMass, double tryDX, double tryDY, boolean tryFixed) {
        double factorX = 1./getReductionFactorX();
        double factorY = 1./getReductionFactorY();
        // builder sets board too
        Planet planet = new Planet(
                tryX * factorX,
                tryY * factorY,
                (int) (tryMass * factorX),
                tryDX * Math.pow(factorX, 0.5),
                tryDY * Math.pow(factorY, 0.5),
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

    public int notEmpty(Collection<Planet> planets, double tryX, double tryY) {
        int i = 0;
        int errorIndex = -1;
        for (Planet p: planets) {
            if ((Math.sqrt(Math.pow(p.x() - tryX, 2) + Math.pow(p.y() - tryY, 2))
                    < 10)) {
                tryX += 11;
                errorIndex = i;
            }
            ++i;
        }
        return errorIndex;
    }

    public void validate(String xposText, String yposText) throws PlanetsCoincideError {
        for (int i = 0; i < drawPlanets.size(); i++) {
				/* This big ugly thing checks to see if the distance between the planet
				 that is currently being made is less than the diameter of a planet,
				 if it is then it throws a PlanetsCoincideError.*/
            Planet planet = getDrawPlanet(i);
            if (Math.sqrt(
                    Math.pow(planet.x() - Double.parseDouble(xposText), 2)
                    + Math.pow(planet.y() - Double.parseDouble(yposText), 2)) < 10) {
                throw new PlanetsCoincideError(planet);
            }
        }
    }

    public void addPlanet(Planet planet) {
        drawPlanets.add(planet);
    }

    public void setPlanet(int i, Planet planet) {
        drawPlanets.set(i, planet);
    }

    public void restart() {
        setDrawPlanets(new ArrayList<Planet>());
    }

    public void start() {
        handle.start();
    }

    public Collisions getCollisions() {
        return collisions;
    }

    public Gravity getGravity() {
        return gravity;
    }

    /**
     *
     * @return same order
     */
    public Iterator<Planet> planetIterator() {
        return drawPlanets.listIterator();
    }

    /**
     * TODO Keep one of these instead...
     *
     * @return increasing size order, unmodifiable --
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

    @Override
    public String toString() {
        return drawPlanets.toString();
    }

    public void logState(String s) {
        if (options.logLevel == Level.INFO) {
            System.out.println(s + "\n\t -> " + this.toString());
        }
    }

    public void logState(String s, Level level) {
        if (options.logLevel.intValue() <= level.intValue()) {
            System.out.println(s + "\n\t -> Positions #" + this.savedPositionsFifo.peek().index);
        }
    }

    public void logStateForced(String s) {
        if (options.logLevel != Level.SEVERE) {
            System.out.println(s + "\n\t -> " + this.toString());
        }
    }

    public void movePlanets(Board.BoardConstants consts) {
        if (!optionsUI.isMoveThreaded()) {
            movePlanetsOnce(consts);
        } else {
            startMover(consts);
        }
    }

    protected void startMover(Board.BoardConstants consts) {
        if (mover == null) {
            mover = new Mover(this, consts);
            mover.resume();
        }
    }

    protected void stopMover() {
        if (mover != null) {
            mover.pause();
        }
        mover = null;
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
        private final Board.BoardConstants consts;
        private final Simulation sim;

        public Mover(Simulation sim, Board.BoardConstants consts) {
            this.consts = consts;
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
                sim.movePlanetsOnce(consts);
                if (sim.options.getSleep() > 0)
                    try {
                        Thread.sleep(sim.options.getSleep());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
            sim.logState("Pausing run");
        }
    }

    protected void movePlanetsOnce(Board.BoardConstants consts) {
        gravity.movePlanets(consts);
        // Moves
        for (Planet p : drawPlanets) {
            p.move(consts.dt);
        }
        // Checks for collisions
        collisions.check(consts);
        // Memento
        Positions pos = savePositions();
        if (options.logEach > 0 && pos.index % options.logEach == 0) {
            logState(pos.toString());
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
