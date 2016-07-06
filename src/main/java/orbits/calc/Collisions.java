package orbits.calc;

import orbits.Simulation;
import orbits.model.Planet;
import orbits.ui.Board;

/**
 * Created by amunzer on 7/6/16.
 */
public class Collisions {
    private final Simulation sim;

    public Collisions(Simulation simulation) {
        this.sim = simulation;
    }


    public void check(Board.BoardConstants consts) {
        for (int i = 0; i < sim.getPlanetCount(); i++) {
            Planet p1 = sim.getDrawPlanet(i);

            for (int j = 0; j < sim.getPlanetCount(); j++) {
                Planet p2 = sim.getDrawPlanet(j);
                // If the distance is closer than the 2 planet radii (one diameter)
                if(Math.sqrt(Math.pow(p2.x()-p1.x(), 2) + Math.pow(p2.y()-p1.y(), 2)) <= 15 && i != j) {
                    double vx1 = p1.getFixed() ? 0 : p1.getDx();
                    double vy1 = p1.getFixed() ? 0 : p1.getDy();
                    double vx2 = p2.getFixed() ? 0 : p2.getDx();
                    double vy2 = p2.getFixed() ? 0 : p2.getDy();
                    double m1 = p1.getMass();
                    double m2 = p2.getMass();
                    double vxc = (vx1 * m1 + vx2 * m2)/(m1 + m2);
                    double vyc = (vy1 * m1 + vy2 * m2)/(m1 + m2);

                    p1.setDx((2 * m2 * (vx2 - vxc) + m1 * (vx1 - vxc) - m2 * (vx1 - vxc)) / (m1 + m2) + vxc);
                    p1.setDy((2 * m2 * (vy2 - vyc) + m1 * (vy1 - vyc) - m2 * (vy1 - vyc)) / (m1 + m2) + vyc);
                    p2.setDx((2 * m1 * (vx1 - vxc) + m2 * (vx2 - vxc) - m1 * (vx2 - vxc)) / (m1 + m2) + vxc);
                    p2.setDy((2 * m1 * (vy1 - vyc) + m2 * (vy2 - vyc) - m1 * (vy2 - vyc)) / (m1 + m2) + vyc);
                    p1.move(consts.dt);
                    p2.move(consts.dt);
                }
            }
        }

    }
}
