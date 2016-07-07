package orbits.calc;

import orbits.Simulation;
import orbits.model.Planet;
import orbits.ui.Board;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by amunzer on 7/6/16.
 */
public class Collisions {
    private final Simulation sim;

    public Collisions(Simulation simulation) {
        this.sim = simulation;
    }

    public void check(Board.BoardConstants consts) {
        final HashSet<Planet> lostPlanets = new HashSet<Planet>();
        final double mergeThresholdMassRatioMax = sim.getOptions().getMergeThresholdMassRatioMax();
        for (int i = 0; i < sim.getPlanetCount(); i++) {
            final Planet p1 = sim.getDrawPlanet(i);
            if (lostPlanets.contains(p1)) {
                continue;
            }
            for (int j = 0; j < sim.getPlanetCount(); j++) {
                final Planet p2 = sim.getDrawPlanet(j);
                if (lostPlanets.contains(p2)) {
                    continue;
                }
                Planet lost = bounceOrMerge(consts, lostPlanets, mergeThresholdMassRatioMax, i, p1, j, p2);
                if (lost != null) {
                    lostPlanets.add(lost);
                }
            }
        }
        // Cleanup
        if (!lostPlanets.isEmpty()) {
            Iterator<Planet> iter = sim.planetIterator();
            while (iter.hasNext()) {
                if (lostPlanets.contains(iter.next())) {
                    iter.remove();
                }
            }
        }
    }

    private Planet bounceOrMerge(Board.BoardConstants consts, HashSet<Planet> lostPlanets, double mergeThresholdMassRatioMax, int i, Planet p1, int j, Planet p2) {
        // If the distance is closer than the 2 planet radii (one diameter)
        if(Math.sqrt(Math.pow(p2.x()-p1.x(), 2)
                + Math.pow(p2.y()-p1.y(), 2)) <= 15
                && i != j) {
            double vx1 = p1.getFixed() ? 0 : p1.getDx();
            double vy1 = p1.getFixed() ? 0 : p1.getDy();
            double vx2 = p2.getFixed() ? 0 : p2.getDx();
            double vy2 = p2.getFixed() ? 0 : p2.getDy();
            double m1 = p1.getMass();
            double m2 = p2.getMass();
            double vxc = (vx1 * m1 + vx2 * m2)/(m1 + m2);
            double vyc = (vy1 * m1 + vy2 * m2)/(m1 + m2);

            // TODO Speed should be a factor too, eventually...
            if (merge(m1, m2, mergeThresholdMassRatioMax)) {
                sim.logState("Merge " + m1 + " and " + m2 + " < " + mergeThresholdMassRatioMax);
                lostPlanets.add(p2);
                p1.setDx(vxc);
                p1.setDy(vyc);
                p1.setFixed(false);
                p1.move(consts.dt);
                return p2;
            } else {
                if (mergeThresholdMassRatioMax > 0) {
                    // too close in size to merge, instead transfer
                    double transfer = massToTransfer(m1, m2, vxc, vyc);
                    sim.logState("Transfer: " + transfer + " between " + m1 + " and " + m2);
                    p1.setMass(m1 + transfer);
                    p2.setMass(m2 - transfer);
                }
                p1.setDx((2 * m2 * (vx2 - vxc) + m1 * (vx1 - vxc) - m2 * (vx1 - vxc)) / (m1 + m2) + vxc);
                p1.setDy((2 * m2 * (vy2 - vyc) + m1 * (vy1 - vyc) - m2 * (vy1 - vyc)) / (m1 + m2) + vyc);
                p2.setDx((2 * m1 * (vx1 - vxc) + m2 * (vx2 - vxc) - m1 * (vx2 - vxc)) / (m1 + m2) + vxc);
                p2.setDy((2 * m1 * (vy1 - vyc) + m2 * (vy2 - vyc) - m1 * (vy2 - vyc)) / (m1 + m2) + vyc);
                p1.move(consts.dt);
                p2.move(consts.dt);
            }
        }
        return null;
    }

    /**
     * @return half of smaller mass (negative sign if p1 is smaller)
     */
    protected double massToTransfer(double m1, double m2, double vxc, double vyc) {
        double angular = Math.max(1, Math.sqrt(vxc * vxc + vyc * vyc));
        if (m1 > m2) {
            return m2 * 0.99 * (1/angular);
        } else {
            return -m1 * 0.99 * (1/angular);
        }
    }

    /**
     * @return true if we have merged p1+p2 -> p1
     */
    protected boolean merge(double m1, double m2, double ratioMax) {
        // e.g. 5/10 < .25 == false (and 10/5), but 2/10 < .25 == true
        return ratioMax > 0 && (m1/(m2+.000001) < ratioMax
                || m2/(m1+.000001) < ratioMax);
    }
}
