package orbits;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by amunzer on 7/6/16.
 */
public class OrbitsConfigOptions {

    /**
     * IF minorMass/majorMass ratio < ratio THEN collision is merge not bounce!
     */
    private double mergeThresholdMassRatioMax;
    private double emptySpaceY;
    private double emptySpaceX;

    private boolean moveThreaded;

    public Level logLevel = Level.INFO; // WARNING;
    public int logEach = 250;
    public long sleep = 10L; // ms

    public OrbitsConfigOptions(Simulation simulation) {
        // v. transfer mass
        this.mergeThresholdMassRatioMax = 0.1; // 0. old
        this.emptySpaceX = 100;
        this.emptySpaceY = 100;
        // this.flip
        this.moveThreaded = true;
    }

    public double getMergeThresholdMassRatioMax() {
        return mergeThresholdMassRatioMax;
    }

    public double getEmptySpaceY() {
        return emptySpaceY;
    }

    public double getEmptySpaceX() {
        return emptySpaceX;
    }

    public boolean isMoveThreaded() {
        return moveThreaded;
    }
}
