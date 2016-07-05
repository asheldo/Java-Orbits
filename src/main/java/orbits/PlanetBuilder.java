package orbits;

/**
 * Created by amunzer on 7/4/16.
 */
public class PlanetBuilder {
    private Board board;

    public PlanetBuilder() {
        this(Simulation.getInstance().getHandle().board);
    }
    public PlanetBuilder(Board board) {
        this.board = board;
    }

    public Planet build(double tryX, double tryY, int tryMass, double tryDX, double tryDY, boolean tryFixed) {
        Planet planet = new Planet(tryX, tryY, tryMass, tryDX, tryDY, tryFixed);
        planet.setBoard(board);
        return planet;
    }
}
