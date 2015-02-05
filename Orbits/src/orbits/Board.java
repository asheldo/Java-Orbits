package orbits;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Board extends JPanel{
	public static double dt = .05;
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
		
		
		//double G = 1; // gravitational constant
		double G = 10;
		
		
		double dx;
		double dy;
		Planet p1;
		Planet p2;
		for(int i = 0; i < Runner.drawPlanets.size(); i++) {
			p2 = Runner.drawPlanets.get(i);
			dx = p2.getDx();
			dy = p2.getDy();
			for (int k = 0; k < Runner.drawPlanets.size(); k++) {
				p1 = Runner.drawPlanets.get(k);
				if (i == k) {
					dx += 0;
					dy += 0;
				} else {
					double dpx = p1.x() - p2.x();
					double dpy = p1.y() - p2.y();
					double rng2 = Math.pow(dpx, 2) + Math.pow(dpy, 2);
					
					dx += dt*G*p1.getMass()/(Math.pow(rng2, 1.5))*(dpx);
					
					dy += dt*G*p1.getMass()/(Math.pow(rng2, 1.5))*(dpy);
					
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
			Runner.drawPlanets.get(i).setDx(dx);
			
			Runner.drawPlanets.get(i).setDy(dy);
			
		}
		// Moves
		for (int i = 0; i < Runner.drawPlanets.size(); i++) {
			if (Runner.handle.tabbedPane.getSelectedIndex() == 1){
				p1 = Runner.drawPlanets.get(i);
				
				Runner.drawPlanets.get(i).move();
				Planet draw = Runner.drawPlanets.get(i);
				if(i < COLORS.size()){
					g.setColor(COLORS.get(i));
				}
				else {
					g.setColor(Color.gray);
				}
				g.fillOval((int)draw.getCoords()[0], (int)draw.getCoords()[1], 10, 10);
				// Graphical indication of a fixed planet
				if (draw.getFixed()) {
					g.setColor(Color.black);
					g.drawOval((int)draw.getCoords()[0], (int)draw.getCoords()[1], 10, 10);
					
				}
			} else if (Runner.handle.tabbedPane.getSelectedIndex() == 0) {
				Runner.drawPlanets.get(i).move();
				String displaytext = "";
				for(int k = 0; k < Runner.drawPlanets.size(); k++) {
					String temp = "  Index: " + (k+1) + "\t" + Runner.drawPlanets.get(k).toString() + "\n";
					displaytext += temp;
				}
				Runner.handle.dispfield.setText(displaytext);
			}
		}
		
		// Checks for collisions
		for (int i = 0; i < Runner.drawPlanets.size(); i++) {
			p1 = Runner.drawPlanets.get(i);
			
			
			
			for (Planet p3 : Runner.drawPlanets) {
				// If the distance is closer than the 2 planet radii (one diameter)
				if(Math.sqrt(Math.pow(p3.x()-p1.x(), 2) + Math.pow(p3.y()-p1.y(), 2)) <= 10 && Math.pow(p3.x()-p1.x(), 2) + Math.pow(p3.y()-p1.y(), 2) != 0) {
					Runner.drawPlanets.get(i).setFixed(true); //fix planets
					
//					double xp1p3 = p3.x() - p1.x();
//					double yp1p3 = p3.y() - p1.y();
//					double dp1p3 = Math.pow(Math.pow(yp1p3, 2) + Math.pow(xp1p3, 2) , .5);
					
					
				}
			}
			
			
			
		}
		
		
		
		
		
		if(Runner.handle.isRunning){
			
			Runner.handle.makeplanet.setEnabled(false);
			Runner.handle.xpos.setEnabled(false);
			Runner.handle.ypos.setEnabled(false);
			Runner.handle.mass.setEnabled(false);
			Runner.handle.xvel.setEnabled(false);
			Runner.handle.yvel.setEnabled(false);
			Runner.handle.tabbedPane.setEnabled(false);
			Runner.handle.btnResetSimulation.setEnabled(false);
//			Runner.handle.btnEditPlanets.setEnabled(false);
//			
//			if (Runner.handle.restartindex == 3) {
//				if (drawPlanets.get(3).x() >=1 && drawPlanets.get(3).x() <=4 && drawPlanets.get(3).y() >= 190)
//					Runner.handle.recallConfig();
//			}
			
			// This repaint command is what keeps the simulation running, along with the time listener
			Runner.handle.repaint();
			
		} else {
			
		Runner.handle.makeplanet.setEnabled(true);
		Runner.handle.xpos.setEnabled(true);
		Runner.handle.ypos.setEnabled(true);
		Runner.handle.mass.setEnabled(true);
		Runner.handle.xvel.setEnabled(true);
		Runner.handle.yvel.setEnabled(true);
		Runner.handle.tabbedPane.setEnabled(true);
		Runner.handle.btnResetSimulation.setEnabled(true);
		
		String displaytext = "";
		for(int k = 0; k < Runner.drawPlanets.size(); k++) {
			String temp = "  Index: " + (k+1) + "\t" + Runner.drawPlanets.get(k).toString() + "\n";
			displaytext += temp;
		}
		
		Runner.handle.dispfield.setText(displaytext);
		}
		
	}


}
