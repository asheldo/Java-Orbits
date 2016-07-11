package orbits.ui;

import orbits.Simulation;
import orbits.model.Planet;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;

public class Board extends JPanel{

	public static final int TAB_SIMULATION_VIEW = 1;
	public static final int TAB_INFORMATION = 0;

	private long drawings;
	private long lastMove;

	public static class BoardConstants {
		// gravitational constant
		public final double G = 9.8; // 10
		public final double dt = .25;
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

		int selected = board.tabbedPane.getSelectedIndex();
		try {
			if (selected == TAB_SIMULATION_VIEW) {
				long move = sim.getMoves();
				if (move > lastMove) {
					++drawings;
					lastMove = move;
					drawVisiblePlanets(sim, g);
				} else {
					// TODO Redraw exactly last time, or don't repaint?
				}
			} else if (selected == TAB_INFORMATION) {
				String text = textMovePlanet(sim);
				board.dispfield.setText(text);
			}
		} catch (Exception e) {
			sim.logState(e.getMessage(), Level.WARNING);
		}
		controlsUpdate(board, sim);
	}

	protected void drawVisiblePlanets(Simulation sim, Graphics2D g) {
		double reduceX = sim.getReductionFactorX();
		double reduceY = sim.getReductionFactorY();
		// count = sim.getPlanetCount();

		int i = 0, invisible = 0, visible = 0;
		Iterator<Planet> iter = sim.planetIterator();
		while (iter.hasNext()) {
			Planet p1 = iter.next();
			int x = (int) translateX(p1.x() * reduceX); // ;
			int y = (int) translateY(p1.y() * reduceY); // getCoords()[1];
			if (x < -10 || x > getWidth() + 10
					|| y < -10 || y > getHeight() + 10) {
				// don't waste drawing effort (although calculation effort is bigger problem)
				++invisible;
			} else {
				++ visible;
				drawVisiblePlanet(i++, g, x, y, p1.getFixed(), p1.getMass(),
						p1.getQuadrant(sim.getFixedCenter()));
			}
		}
		if (drawings % 50 == 0) {
			sim.logState("Iteration=" + drawings + ", Invisible=" + invisible, Level.WARNING);
		}
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

	public String textMovePlanet(Simulation sim) {
		String displaytext = "";
		int k = 0;
		Iterator<Planet> iter = sim.planetIterator();
		while (iter.hasNext()) {
			String temp = " #" + (++k) + iter.next().toString() + "\n";
			displaytext += temp;
		}
		return displaytext;
	}

	private void drawVisiblePlanet(int i, Graphics2D g, int x, int y, boolean fixed, double mass, int quadrant) {

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

		// Graphical indication of a fixed planet
		if (fixed) {
			g.fillOval(x, y, diameter, diameter);
			g.setColor(Color.black);
			g.drawOval(x, y, diameter, diameter);
			g.setColor(Color.white);
			g.drawOval(x, y, diameter + 1, diameter + 1);
		} else if (quadrant == 0) {
			g.fillOval(x, y, diameter, diameter);
		} else if (quadrant == 1) {
			g.fillRect(x, y, diameter, diameter);
		} else if (quadrant == 2) {
			g.drawRect(x, y, diameter, diameter);
		} else {
			g.drawOval(x, y, diameter, diameter);
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
			long move = sim.getMoves();
			if (move == lastMove) {
				try {
					// We aren't redrawing the same Positions more than once any more, so wait for calcs:
					Thread.sleep(100L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
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

			String displaytext = textMovePlanet(sim);
			board.dispfield.setText(displaytext);
		}
		
	}

	public BoardConstants getConsts() {
		return consts;
	}

}
