package orbits.calc;

import orbits.model.Planet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by amunzer on 7/8/16.
 */
public class Positions implements Serializable {
    private final List<Planet> planetsAtT;
    private static int counter = 0;

    public final Integer index;

    public Positions(List<Planet> planets) {
        List<Planet> copy = new ArrayList<Planet>(planets.size());
        for (Planet p : planets) {
            copy.add(new Planet(p));
        }
        this.planetsAtT = Collections.unmodifiableList(copy);
        this.index = counter++;
    }

    public List<Planet> getPlanetsAtT() {
        return planetsAtT;
    }

    @Override
    public String toString() {
        return index.toString() + ": " + planetsAtT;
    }
}
