package orbits.calc;

import orbits.Simulation;
import orbits.model.Planet;
import orbits.ui.Board;

/**
 * Created by amunzer on 7/6/16.
 */
public class Gravity {
    private final Simulation sim;

    public Gravity(Simulation sim) {
        this.sim = sim;
    }

    public void movePlanets(Board.BoardConstants consts) {
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

                    dx += consts.dt * consts.G * p1.getMass() / (Math.pow(rng2, 1.5)) * (dpx);
                    dy += consts.dt * consts.G * p1.getMass() / (Math.pow(rng2, 1.5)) * (dpy);
                }
            }
            // Top wall bounce
            if (p2.y() >= 335) {
                if (p2.getDy() > 0)
                    dy = -dy;
            }
            // Right wall bounce
            if (p2.x() >= 361) {
                if (p2.getDx() > 0)
                    dx = -dx;
            }
            // Bottom wall bounce
            if (p2.y() <= -329) {
                if (p2.getDy() < 0)
                    dy = -dy;
            }
            // Left wall bounce
            if (p2.x() <= -364) {
                if (p2.getDx() < 0)
                    dx = -dx;
            }
            p2.setDx(dx);
            p2.setDy(dy);
        }


    }

}
