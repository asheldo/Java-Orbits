package orbits;

import java.util.logging.Level;

/**
 * Created by amunzer on 7/6/16.
 */
public class OrbitsConfigOptions {

    /**
     * IF minorMass/majorMass ratio < ratio THEN collision is merge not bounce!
     */
    private double mergeThresholdMassRatioMax;

    // public Level logLevel = Level.INFO; // WARNING;
    public Level logLevel = Level.WARNING;

    public int logEach = 25000;

    private long sleep = 0L; // 5L; // ms

    private double halfMaxDimension = 10000;

    private double outerSpace = 50000;

    public OrbitsConfigOptions(Simulation simulation) {
        // v. transfer mass
        this.mergeThresholdMassRatioMax = 0.1; // 0. old
    }

    public double getMergeThresholdMassRatioMax() {
        return mergeThresholdMassRatioMax;
    }

    public double getHalfMaxDimension() {
        return halfMaxDimension;
    }

    public long getSleep() {
        return sleep;
    }

    public double getOuterSpace() {
        return outerSpace;
    }
}
