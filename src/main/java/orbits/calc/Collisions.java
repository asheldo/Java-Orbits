package orbits.calc;

import orbits.OrbitsConfigOptions;
import orbits.Simulation;
import orbits.model.Planet;
import orbits.ui.Board;

import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;

/**
 * Created by amunzer on 7/6/16.
 */
public class Collisions {
    private final Simulation sim;

    private double lostMass;

    public Collisions(Simulation simulation) {
        this.sim = simulation;
    }

    public void check() {
        final HashSet<Planet> lostPlanets = new HashSet<Planet>();
        final HashSet<Planet> newPlanets = new HashSet<Planet>();
        int i = 0;
        Iterator<Planet> iter = sim.planetIterator();
        while (iter.hasNext()) {
            final Planet p1 = iter.next();
            if (lostPlanets.contains(p1)) {
                continue;
            }
            try {
                int j = 0;
                Iterator<Planet> iter2 = sim.planetIterator();
                while (iter2.hasNext()) {
                    final Planet p2 = iter2.next();
                    if (lostPlanets.contains(p2)) {
                        continue;
                    }
                    Planet lost = bounceOrMerge(lostPlanets, newPlanets,
                            i, p1, j, p2);
                    if (lost != null) {
                        lostPlanets.add(lost);
                    }
                    ++j;
                }
            } catch (Exception e) {
                System.out.println(i + " " + e.getMessage());
            }
            ++i;
        }
        // Cleanup
        for (Planet p : newPlanets) {
            sim.addFragment(p);
        }
        if (!lostPlanets.isEmpty()) {
            Iterator<Planet> iter3 = sim.planetIterator();
            while (iter3.hasNext()) {
                if (lostPlanets.contains(iter3.next())) {
                    iter3.remove();
                }
            }
        }
    }

    /**
     *
     * @param lostPlanets
     * @param i
     * @param p1
     * @param j
     * @param p2
     *
     * @return lost Planet if a merge-absorption occurs
     */
    protected Planet bounceOrMerge(HashSet<Planet> lostPlanets, HashSet<Planet> newPlanets,
                                 int i, Planet p1, int j, Planet p2) {
        if (p1.getMass() > 0 && p2.getMass() > 0
            && i != j)
        {
            OrbitsConfigOptions settings = sim.getOptions();
            final double mergeThresholdMassRatioMax = settings.getMergeThresholdMassRatioMax();
            return bounceOrMerge(mergeThresholdMassRatioMax, lostPlanets, newPlanets, p1, p2);
        }
        // sim.logState("zero collision: " + p1 + "->"+ p2, Level.WARNING); // we don't do zero anymore
        return null;
    }

    protected Planet bounceOrMerge(double mergeThresholdMassRatioMax,
                                   HashSet<Planet> lostPlanets, HashSet<Planet> newPlanets,
                                   Planet p1, Planet p2) {
        double xd = Math.abs(p2.x()-p1.x()), yd = Math.abs(p2.y()-p1.y());
        double d = Math.sqrt(xd * xd + yd * yd);
        double r1 = p1.getRadius(), r2 = p2.getRadius();
        double m1 = p1.getMass(), m2 = p2.getMass();
        double vx1 = p1.getFixed() ? 0 : p1.getDx();
        double vy1 = p1.getFixed() ? 0 : p1.getDy();
        double vx2 = p2.getFixed() ? 0 : p2.getDx();
        double vy2 = p2.getFixed() ? 0 : p2.getDy();
        // If the distance is closer than the 2 planet radii (one diameter)
        double thresholdCollisionDistance = r1 + r2; // 15;
        // Good enough, don't need hypotenuse accuracy
        // if (collided)
        boolean collided = xd <= thresholdCollisionDistance && yd <= thresholdCollisionDistance;
        if (!collided &&
                m1 + m2 > 1) {
            collided = p1.crossing(p2, xd, yd, sim.getOptions().dt, d);
        }
        if (collided) {
            double vxc = (vx1 * m1 + vx2 * m2)/(m1 + m2);
            double vyc = (vy1 * m1 + vy2 * m2)/(m1 + m2);

            // TODO Speed should be a factor too, eventually...

            if (merge(m1, m2, mergeThresholdMassRatioMax)) {
                Planet keep = m1 > m2 ? p1 : p2;
                Planet lost = m1 > m2 ? p2 : p1;
                // sim.logState("Merge: " + lost + " with " + keep + " < " + mergeThresholdMassRatioMax, Level.INFO);

                lostPlanets.add(lost);
                // Some mass is lost ... to where?
                if (keep.getFixed()
                        && false) {
                    // TODO Heat up instead??
                } else {
                    if (keep.getMass()/(m1 + m2) < .9995) {
                        keep.setFixed(false);
                        // Some momentum of smaller body is converted to angular momentum instead
                        // probably, so average before/after...
                        keep.setDx((vxc + keep.getDx()) / 2.);
                        keep.setDy((vyc + keep.getDy()) / 2.);
                        // p1.move(consts.dt/2.); // extra move only valid for half of time interval
                    }
                }
                keep.setMass(m1 + m2);
                lost.setMass(-1);
                keep.absorbed(lost);
                return lost;
            } else {
                // too close in size to merge, instead transfer
                double verySmallMass = 25.;
                // Don't bother with mass transfer part if two very small bodies
                if (mergeThresholdMassRatioMax > 0 && (m1 + m2 > verySmallMass)) {
                    double transfer = massToTransfer(m1, m2, vxc, vyc);
                    Planet p3 = calvePlanet(p1, p2, transfer);
                    if (p3 != null) {
                        newPlanets.add(p3);
                    }
                } else {
                    sim.logState("Collision", // between " + p1 + " to " + p2,
                             Level.INFO);
                }
                // Some momentum of smaller body is converted to angular momentum instead
                // probably, so average before/after...
                p1.setDx((((2 * m2 * (vx2 - vxc) + m1 * (vx1 - vxc) - m2 * (vx1 - vxc)) / (m1 + m2) + vxc)
                        + p1.getDx())/2.);
                p1.setDy((((2 * m2 * (vy2 - vyc) + m1 * (vy1 - vyc) - m2 * (vy1 - vyc)) / (m1 + m2) + vyc)
                        + p1.getDy())/2.);
                p2.setDx((((2 * m1 * (vx1 - vxc) + m2 * (vx2 - vxc) - m1 * (vx2 - vxc)) / (m1 + m2) + vxc)
                        + p2.getDx())/2.);
                p2.setDy((((2 * m1 * (vy1 - vyc) + m2 * (vy2 - vyc) - m1 * (vy2 - vyc)) / (m1 + m2) + vyc)
                        + p2.getDy())/2.);
                // p1.move(consts.dt/2.); // extra move only valid for half of time interval
                // p2.move(consts.dt/2.); // extra move only valid for half of time interval
            }
        }
        return null;
    }

    protected Planet calvePlanet(Planet p1, Planet p2, double transfer) {
        sim.logState("Transfer: " + transfer + " between " + p1 + " to " + p2, Level.INFO);
        double m1 = p1.getMass(), m2 = p2.getMass();
        p1.setMass(m1 + transfer);
        p2.setMass(m2 - transfer);

        // Sometimes some mass in impactee is lost create frag
        if (sim.getPlanetCount() < 500 && transfer > 0) {
            Planet p3 = new Planet(p2);
            // Remove some
            // Todo angular
            p1.setMass(m1 + transfer * .75);

            p3.setMass(transfer * .25);
            p3.setX(p2.x() + p2.getRadius() * 5); // move n radii
            p3.setY(p2.y() + p2.getRadius() * 5); // move n radii
            return p3;
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
