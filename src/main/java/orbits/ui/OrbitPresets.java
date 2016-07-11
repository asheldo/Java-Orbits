package orbits.ui;

import orbits.Simulation;
import orbits.model.Planet;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by andy on 7/10/16.
 */
public class OrbitPresets {

    private boolean presetFirstrun = true;

    private final Simulation sim;
    private final JTextArea dispfield;
    private final ArrayList<Planet> restartPlArrList;
    private final Board.BoardConstants consts;
    private JTabbedPane tabbedPane;

    public OrbitPresets(Simulation sim, JTextArea dispfield, ArrayList<Planet> restartPlArrList,
                        Board.BoardConstants consts, JTabbedPane tabbedPane) {
        this.sim = sim;
        this.dispfield = dispfield;
        this.restartPlArrList = restartPlArrList;
        this.consts = consts;
        this.tabbedPane = tabbedPane;
    }

    public void recallSquareOrbits() {
        sim.restart();
        sim.buildPlanet(200, 0, 1000, 0, 2, false);
        sim.buildPlanet(-200, 0, 1000, 0, -2, false);
        sim.buildPlanet(0, 200, 1000, -2, 0, false);
        sim.buildPlanet(0, -200, 1000, 2, 0, false);

        if(!presetFirstrun) {
            sim.setRunning(false);
            tabbedPane.setSelectedIndex(3);
        } else {
            sim.setRunning(true);
            tabbedPane.setSelectedIndex(1);
            presetFirstrun = false;
        }
    }

    public void recallSlingshot() {
        sim.restart();
        sim.buildPlanet(-40, 0, 250, 0, 0, true);
        sim.buildPlanet(40, 0, 250, 0, 0, true);
        sim.buildPlanet(0, 0, 0, 0, 3, false);

        if(!presetFirstrun) {
            sim.setRunning(false);
            tabbedPane.setSelectedIndex(3);
        } else {
            sim.setRunning(true);
            tabbedPane.setSelectedIndex(1);
            presetFirstrun = false;
        }
    }

    public void recallCircularCloudOrbits(int additionalPlanets) {
        sim.restart();
        Planet center = sim.buildPlanet(0, 0, 200000, 0, 0, true);
        double dim = sim.getOptions().getHalfMaxDimension();
        double count = Math.pow((double) additionalPlanets, 0.75);
        if (additionalPlanets % 5 == 1) {
            // Top button in column of 5 generates many more small bodies
            recallCloudPlus(additionalPlanets, center, dim, count);
        } else {
            // Bottom 4 buttons in column of 5 generate ++1 more, increasing-size body
            for (int more = 0; more <= additionalPlanets; ++more) {
                sim.buildPlanet(
                        (50 + 3 * more),
                        0 * more, // y
                        5 * more, // mass
                        0, // dx
                        // dy
                        4 + 0.2 * more,
                        false);
            }
        }
        if (!presetFirstrun) {
            sim.setRunning(false);
            tabbedPane.setSelectedIndex(3);
        } else {
            sim.setRunning(true);
            tabbedPane.setSelectedIndex(1);
            presetFirstrun = false;
        }
    }

    private void recallCloudPlus(int additionalPlanets, Planet center, double dim, double count) {
        for (int more = 0; more <= additionalPlanets; ++more) {
            Planet p1 = sim.buildPlanet(
                    // (50 + 10 * more),
                    center.getRadius() * 2.5
                            + (0.25 * dim * Math.pow((double) (more) / (double) (additionalPlanets), .33)),
                    0 * more, // y
                    1 + 4 * additionalPlanets, // mass
                    0, // dx
                    // dy
                    center.getRadius() + (4 * Math.sqrt((double) (more) / (double) (additionalPlanets))),
                    // 4 + 0.5 more,
                    false);
            for (int distributed = 0; distributed < count; ++distributed) {
                Planet p2 = new Planet(p1);
                p2.setMass(p2.getMass() * .5 * ((double) distributed/ count));
                p2.randomizeOnCircle(consts.dt);
                sim.addPlanet(p2);
            }
        }
    }

    public void recallCircularOrbit() {
        sim.restart();
        sim.buildPlanet(0, 0, 5000, 0, 0, true);
        sim.buildPlanet(100, 0, 0, 0,
                // factorX *
                11, // 22
                false);

        if (!presetFirstrun) {
            sim.setRunning(false);
            tabbedPane.setSelectedIndex(3);
        } else {
            sim.setRunning(true);
            tabbedPane.setSelectedIndex(1);
            presetFirstrun = false;
        }
    }

    public void recallPlanetsRestart() {
        String d = "Planets=" + sim.getPlanetCount();
        sim.logState(d, Level.WARNING);
        for(int i = 0; i < restartPlArrList.size(); i++) {
            sim.setPlanet(i, new Planet(restartPlArrList.get(i)));
            String temp = "  Index: " + (i+1) + "\t" + Simulation.getInstance().getDrawPlanet(i).toString() + "\n";
            d += temp;
        }
        dispfield.setText(d);
        sim.setRunning(false);
        tabbedPane.setSelectedIndex(3);
        tabbedPane.setEnabled(true);
    }


}
