package orbits.model;

import orbits.Simulation;
import orbits.ui.Board;

import java.util.Collection;

/**
 * Created by amunzer on 7/4/16.
 */
public class PlanetBuilder {
    private Board board;

    public PlanetBuilder() {
        this(Simulation.getInstance().getHandle().getBoard());
    }
    public PlanetBuilder(Board board) {
        this.board = board;
    }

    public Planet build(double tryX, double tryY, int tryMass, double tryDX, double tryDY, boolean tryFixed) {
        Planet planet = new Planet(tryX, tryY, tryMass, tryDX, tryDY, tryFixed);
        planet.setBoard(board);
        return planet;
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
}
