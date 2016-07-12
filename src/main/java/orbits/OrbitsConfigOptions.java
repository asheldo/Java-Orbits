package orbits;

import java.util.logging.Level;

/**
 * Created by amunzer on 7/6/16.
 */
public class OrbitsConfigOptions {

    // gravitational constant
    public final double G = 10;

    // Relative time slice for calculations, no real unit for now
    public final double dt = .05;

    /**
     * IF minorMass/majorMass ratio < ratio THEN collision is merge not bounce!
     */
    private double mergeThresholdMassRatioMax = 0.2; // 0 effectively before

    // public Level logLevel = Level.INFO; // WARNING;
    public Level logLevel = Level.WARNING;

    public int logEach = 25000;

    private long sleep = 0L; // 5L; // ms

    private double halfMaxDimension = 10000;

    private double outerSpace = 50000;


    public OrbitsConfigOptions(Simulation simulation) {

    }

    public double getMergeThresholdMassRatioMax() {
        return mergeThresholdMassRatioMax;
    }

    public double getHalfMaxDimension() {
        return halfMaxDimension;
    }

    /**
     * Between calcs
     */
    public long getSleep() {
        return sleep;
    }

    public double getOuterSpace() {
        return outerSpace;
    }
}
