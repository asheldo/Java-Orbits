package orbits.ui;

import orbits.Simulation;
import orbits.calc.Collisions;
import orbits.calc.Gravity;
import orbits.model.Planet;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Board extends JPanel{

	public static class BoardConstants {
		// gravitational constant
		public final double G = 9.8; // 10
		public final double dt = .05;
	}

	private BoardConstants consts = new BoardConstants();

	private static final long serialVersionUID = 1L;

	private final ArrayList<Color> COLORS = new ArrayList<Color>();

	@Override
	public int getWidth() {
		return 750;
	}; 
	
	@Override
	public int getHeight() {
		return 690;
	}

	@Override
	protected void paintComponent(Graphics q) {

		super.paintComponent(q);

		Graphics2D g = (Graphics2D) q;

		g.setColor(Color.gray);
		g.drawRect(5, 5, 735, 669);

		COLORS.add(Color.red);
		COLORS.add(Color.blue);
		COLORS.add(Color.green);
		COLORS.add(Color.YELLOW);
		COLORS.add(Color.orange);
		COLORS.add(Color.cyan);
		COLORS.add(Color.magenta);
		COLORS.add(Color.PINK);

		Simulation sim = Simulation.getInstance();
		// Ensure the simulation has started moving
		sim.movePlanets(consts);
		GUIMenu board = sim.getHandle();

		double reduceX = sim.getReductionFactorX();
		double reduceY = sim.getReductionFactorY();

		int selected = board.tabbedPane.getSelectedIndex();
		for (int i = 0; i < sim.getPlanetCount(); i++) {
			if (selected == 1) {
				Planet p1 = sim.getDrawPlanet(i);
				int x = (int) translateX(p1.x() * reduceX); // ;
				int y = (int) translateY(p1.y() * reduceY); // getCoords()[1];
				showMovePlanet(i, g, x, y, p1.getFixed(), p1.getMass());
			} else if (selected == 0) {
				String text = textMovePlanet(sim, i);
				board.dispfield.setText(text);
			}
		}
		controlsUpdate(board, sim);
	}

	// Returns the X and Y pixels in the form of an array of length 2
	public double translateX(double xpos) {
		double xTranslate = getWidth()/2 + xpos - 5;
		return xTranslate;
	}

	public double translateY(double ypos) {
		double yTranslate = getHeight()/2 - ypos - 5;
		return yTranslate;
	}

	private String textMovePlanet(Simulation sim, int i) {
		String displaytext = "";
		for (int k = 0; k < sim.getPlanetCount(); k++) {
			String temp = "  Index: " + (k + 1) + "\t" + sim.getDrawPlanet(k).toString() + "\n";
			displaytext += temp;
		}
		return displaytext;
	}

	private void showMovePlanet(int i, Graphics2D g, int x, int y, boolean fixed, double mass) {

		int diameter = 4;
		if (mass > 10000) {
			diameter = 8;
		} else if (mass > 2000) {
			diameter = 6;
		} else if (mass > 1000) {
			diameter = 5;
		}

		if (i < COLORS.size()) {
			g.setColor(COLORS.get(i));
		} else {
			g.setColor(Color.gray);
		}

		g.fillOval(x, y, diameter, diameter);
		// Graphical indication of a fixed planet
		if (fixed) {
			g.setColor(Color.black);
			g.drawOval(x, y, diameter, diameter);
			g.setColor(Color.white);
			g.drawOval(x, y, diameter + 1, diameter + 1);
		}
	}

	protected void controlsUpdate(GUIMenu board, Simulation sim) {
		if(sim.isRunning()) {
			board.makeplanet.setEnabled(false);
			board.xpos.setEnabled(false);
			board.ypos.setEnabled(false);
			board.mass.setEnabled(false);
			board.xvel.setEnabled(false);
			board.yvel.setEnabled(false);
			board.tabbedPane.setEnabled(false);
			board.btnResetSimulation.setEnabled(false);
//			Runner.handle.btnEditPlanets.setEnabled(false);
//			
//			if (Runner.handle.restartindex == 3) {
//				if (drawPlanets.get(3).x() >=1 && drawPlanets.get(3).x() <=4 && drawPlanets.get(3).y() >= 190)
//					Runner.handle.recallConfig();
//			}
			
			// This repaint command is what keeps the simulation running
			board.repaint();
			
		} else {

			board.makeplanet.setEnabled(true);
			board.xpos.setEnabled(true);
			board.ypos.setEnabled(true);
			board.mass.setEnabled(true);
			board.xvel.setEnabled(true);
			board.yvel.setEnabled(true);
			board.tabbedPane.setEnabled(true);
			board.btnResetSimulation.setEnabled(true);

			String displaytext = "";
			for(int k = 0; k < sim.getPlanetCount(); k++) {
				String temp = "  Index: " + (k+1) + "\t" + sim.getDrawPlanet(k).toString() + "\n";
				displaytext += temp;
			}

			board.dispfield.setText(displaytext);
		}
		
	}


}
