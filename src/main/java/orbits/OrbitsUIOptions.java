package orbits;

import java.util.logging.Level;

/**
 * Created by amunzer on 7/6/16.
 */
public class OrbitsUIOptions {

    private boolean moveThreaded;

    public OrbitsUIOptions(Simulation simulation) {
        // this.flip
        this.moveThreaded = true;
    }

    public boolean isMoveThreaded() {
        return moveThreaded;
    }

}
