package orbits.calc;

import orbits.OrbitsConfigOptions;
import orbits.Simulation;
import orbits.model.Planet;
import orbits.ui.Board;

import java.util.Iterator;
import java.util.logging.Level;

/**
 * Created by amunzer on 7/6/16.
 */
public class Gravity {
    private final Simulation sim;

    public Gravity(Simulation sim) {
        this.sim = sim;
    }

    public void movePlanets() {
        OrbitsConfigOptions settings = sim.getOptions();
        // TODO Options:
        // double emptySpaceY = sim.getOptions().getEmptySpaceY();
        // double emptySpaceX = sim.getOptions().getEmptySpaceX();
        double halfMaxDimension = settings.getHalfMaxDimension();
        double outerSpace = settings.getOuterSpace();

        double maxY = halfMaxDimension; // 335 + emptySpaceY;
        double minY = -halfMaxDimension; // 335 + emptySpaceY;
        double maxX = halfMaxDimension; // 335 + emptySpaceY;
        double minX = -halfMaxDimension; // -364 - emptySpaceY;

        int i = 0;
        Iterator<Planet> iter = sim.planetIterator();
        while (iter.hasNext()) {
            Planet p2 = iter.next();
            double dx = p2.getDx();
            double dy = p2.getDy();
            int k = 0;
            Iterator<Planet> iter2 = sim.planetIterator();
            while (iter2.hasNext()) {
                Planet p1 = iter2.next();
                if (i == k) {
                    dx += 0;
                    dy += 0;
                } else {
                    double dpx = p1.x() - p2.x();
                    double dpy = p1.y() - p2.y();
                    double rng2 = Math.pow(dpx, 2) + Math.pow(dpy, 2);
                    if (rng2 > 0) {
                        dx += settings.dt * settings.G * p1.getMass() / (Math.pow(rng2, 1.5)) * (dpx);
                        dy += settings.dt * settings.G * p1.getMass() / (Math.pow(rng2, 1.5)) * (dpy);
                    } else {
                        sim.logState(p2.index + " and " + p1.index + " coincident at " + sim.savePositions().index,
                                Level.WARNING);
                    }
                }
                ++k;
            }
            ++i;
            boolean flip = false;
            // Top wall bounce
            if (p2.y() >= maxX + outerSpace) {
                if (p2.getDy() > 0)
                    if (flip) {
                        flip(p2);
                    } else
                        dy = -dy * reentryCost(dy);
            }
            // Right wall bounce
            if (p2.x() >= maxX + outerSpace) {
                if (p2.getDx() > 0)
                    if (flip) {
                        flip(p2);
                    } else
                        dx = -dx * reentryCost(dx);
            }
            // Bottom wall bounce
            if (p2.y() <= minY - outerSpace) {
                if (p2.getDy() < 0)
                    if (flip) {
                        flip(p2);
                    } else
                        dy = -dy * reentryCost(dy);
            }
            // Left wall bounce
            if (p2.x() <= minX - outerSpace) {
                if (p2.getDx() < 0)
                    if (flip) {
                        flip(p2);
                    } else
                        dx = -dx * reentryCost(dx);
            }
            p2.setDx(dx);
            p2.setDy(dy);
        }


    }

    private double reentryCost(double dx) {
        double sqrt = Math.sqrt(Math.abs(dx)) / Math.abs(dx);
        return sqrt;
    }

    protected void flip(Planet p) {
        p.setX(-p.x());
        p.setY(-p.y());
    }

}
