package orbits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by amunzer on 7/4/16.
 */
public class Simulation {

    private static Simulation simulation = new Simulation();

    public static Simulation getInstance() {
        return simulation;
    }

    private List<Planet> drawPlanets;
    private final GUIMenu handle;
    private final PlanetBuilder planetBuilder;

    private Simulation() {
        this.drawPlanets = new ArrayList<Planet>();
        this.handle = new GUIMenu("Orbits");
        this.planetBuilder = new PlanetBuilder(handle.board);
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
}
