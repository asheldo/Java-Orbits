package orbits.calc;

import orbits.Simulation;
import orbits.model.Planet;
import orbits.ui.Board;

import java.util.logging.Level;

/**
 * Created by amunzer on 7/6/16.
 */
public class Gravity {
    private final Simulation sim;

    public Gravity(Simulation sim) {
        this.sim = sim;
    }

    public void movePlanets(Board.BoardConstants consts) {
        // TODO Options:
        // double emptySpaceY = sim.getOptions().getEmptySpaceY();
        // double emptySpaceX = sim.getOptions().getEmptySpaceX();
        double halfMaxDimension = sim.getOptions().getHalfMaxDimension();
        double outerSpace = sim.getOptions().getOuterSpace();
        double maxY = halfMaxDimension; // 335 + emptySpaceY;
        double minY = -halfMaxDimension; // 335 + emptySpaceY;
        double maxX = halfMaxDimension; // 335 + emptySpaceY;
        double minX = -halfMaxDimension; // -364 - emptySpaceY;

        for (int i = 0; i < sim.getPlanetCount(); ++i) {
            Planet p2 = sim.getDrawPlanet(i);
            double dx = p2.getDx();
            double dy = p2.getDy();
            for (int k = 0; k < sim.getPlanetCount(); k++) {
                Planet p1 = sim.getDrawPlanet(k);
                if (i == k) {
                    dx += 0;
                    dy += 0;
                } else {
                    double dpx = p1.x() - p2.x();
                    double dpy = p1.y() - p2.y();
                    double rng2 = Math.pow(dpx, 2) + Math.pow(dpy, 2);
                    if (rng2 > 0) {
                        dx += consts.dt * consts.G * p1.getMass() / (Math.pow(rng2, 1.5)) * (dpx);
                        dy += consts.dt * consts.G * p1.getMass() / (Math.pow(rng2, 1.5)) * (dpy);
                    } else {
                        sim.logState(p2.index + " and " + p1.index + " coincident at " + sim.savePositions().index,
                                Level.WARNING);
                    }
                }
            }
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
