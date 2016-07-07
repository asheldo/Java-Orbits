package orbits;

import orbits.calc.Collisions;
import orbits.calc.Gravity;
import orbits.model.Planet;
import orbits.model.PlanetBuilder;
import orbits.ui.GUIMenu;
import orbits.ui.PlanetsCoincideError;

import java.util.*;

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
        this.options = new OrbitsConfigOptions(this);
        this.sized = new Comparator<Planet>() {
            @Override
            public int compare(Planet o1, Planet o2) {
                Double s1 = o1.getMass();
                return s1.compareTo(o2.getMass());
            }
        }
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

    @Override
    public String toString() {
        return drawPlanets.toString();
    }

    public void logState(String s) {
        System.out.println(s + "\n\t -> " + this.toString());
    }
}
