package orbits;

import com.google.common.collect.EvictingQueue;
import com.sun.media.jfxmedia.logging.Logger;
import orbits.calc.Collisions;
import orbits.calc.Gravity;
import orbits.calc.Positions;
import orbits.model.Planet;
import orbits.model.PlanetBuilder;
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
    private OrbitsConfigOptions options;
    private Comparator<Planet> sized;
    private EvictingQueue<Object> savedPositionsFifo;
    private boolean isRunning;
    private Mover mover;
    private MoveExecutor executor;

    public static Simulation getInstance() {
        return simulation;
    }

    private List<Planet> drawPlanets;
    private final GUIMenu handle;
    private final PlanetBuilder planetBuilder;

    private Simulation() {
        this.drawPlanets = new ArrayList<Planet>();
        this.handle = new GUIMenu("Orbits");
        this.planetBuilder = new PlanetBuilder(handle.getBoard());
        this.collisions = new Collisions(this);
        this.gravity = new Gravity(this);
        this.savedPositionsFifo = EvictingQueue.create(1000);
        this.options = new OrbitsConfigOptions(this);
        this.executor = new MoveExecutor();

        this.sized = new Comparator<Planet>() {
            @Override
            public int compare(Planet o1, Planet o2) {
                Double s1 = o1.getMass();
                return s1.compareTo(o2.getMass());
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

    public PlanetBuilder getPlanetBuilder() {
        return planetBuilder;
    }

    /**
     * And add
     */
    public void buildPlanet(double tryX, double tryY, int tryMass, double tryDX, double tryDY, boolean tryFixed) {
        // builder sets board too
        Planet planet = planetBuilder.build(tryX, tryY, tryMass, tryDX, tryDY, tryFixed);
        addPlanet(planet);
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

    public void movePlanets(Board.BoardConstants consts) {
        if (!options.isMoveThreaded()) {
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
                if (sim.options.sleep > 0)
                    try {
                        Thread.sleep(sim.options.sleep);
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
        if (pos.index % options.logEach == 0) {
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
