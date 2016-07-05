package orbits;

import orbits.GUIMenu;
import orbits.Simulation;
import orbits.Planet;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Board extends JPanel{

	public static class BoardConstants {
		// gravitational constant
		final double G = 9.8; // 10
		final double dt = .05;
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
		
		Graphics2D g = (Graphics2D)q;
		
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

		double dx;
		double dy;
		Simulation sim = Simulation.getInstance();
		GUIMenu board = sim.getHandle();
		for(int i = 0; i < sim.getPlanetCount(); i++) {
			Planet p2 = sim.getDrawPlanet(i);
			dx = p2.getDx();
			dy = p2.getDy();
			for (int k = 0; k < sim.getPlanetCount(); k++) {
				Planet p1 = sim.getDrawPlanet(k);
				if (i == k) {
					dx += 0;
					dy += 0;
				} else {
					double dpx = p1.x() - p2.x();
					double dpy = p1.y() - p2.y();
					double rng2 = Math.pow(dpx, 2) + Math.pow(dpy, 2);
					
					dx += consts.dt * consts.G * p1.getMass()/(Math.pow(rng2, 1.5))*(dpx);
					dy += consts.dt * consts.G * p1.getMass()/(Math.pow(rng2, 1.5))*(dpy);
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
			sim.getDrawPlanet(i).setDx(dx);
			
			sim.getDrawPlanet(i).setDy(dy);
			
		}
		// Moves
		for (int i = 0; i < sim.getPlanetCount(); i++) {
			Planet p1 = sim.getDrawPlanet(i);
			if (board.tabbedPane.getSelectedIndex() == 1){
				p1.move(consts.dt);
				if(i < COLORS.size()){
					g.setColor(COLORS.get(i));
				}
				else {
					g.setColor(Color.gray);
				}
				g.fillOval((int) p1.getCoords()[0], (int) p1.getCoords()[1], 10, 10);
				// Graphical indication of a fixed planet
				if (p1.getFixed()) {
					g.setColor(Color.black);
					g.drawOval((int)p1.getCoords()[0], (int)p1.getCoords()[1], 10, 10);
					
				}
			} else if (board.tabbedPane.getSelectedIndex() == 0) {
				p1.move(consts.dt);
				String displaytext = "";
				for(int k = 0; k < sim.getPlanetCount(); k++) {
					String temp = "  Index: " + (k+1) + "\t" + sim.getDrawPlanet(k).toString() + "\n";
					displaytext += temp;
				}
				board.dispfield.setText(displaytext);
			}
		}
		
		// Checks for collisions
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

		if(board.isRunning){
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
