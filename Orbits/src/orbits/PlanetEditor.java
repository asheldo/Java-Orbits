package orbits;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.*;
import javax.swing.table.*;

import java.awt.*;
import java.awt.event.KeyListener;
import java.util.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class PlanetEditor extends JPanel {

	private static final long serialVersionUID = 1L;
	public KeyListener listen;
	//public JPanel mainWindowPanel;
	public JTable planetsTable;
	public DefaultTableModel repaint;
//	public ArrayList<Planet> alp;
	public static Timer timer = new Timer(1, new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {}
	});
	
	
	public PlanetEditor() {
		
		ArrayList<Planet> alp = new ArrayList<Planet>();
		
		for(int i1 = 0; i1 < Runner.drawPlanets.size(); i1++)
			alp.add(null);
		for(int i = 0; i < Runner.drawPlanets.size(); i++)
			alp.set(i, Runner.drawPlanets.get(i));
		
		
		this.setForeground(new Color(0, 0, 0));
		this.setBackground(Color.CYAN);
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLayout(null);		
		
		planetsTable = new JTable();
		planetsTable.setModel(new DefaultTableModel(
			rows(alp),
			new String[] {
				"Index", "X Position", "Y Position", "Mass", "X Velocity", "Y Velocity"
			}
		));
		
		planetsTable.setForeground(Color.cyan);
		planetsTable.setBackground(Color.DARK_GRAY);
		planetsTable.setFillsViewportHeight(true);
		
		planetsTable.setBounds(10, 5, 750, 683);
		this.add(planetsTable);

		
		planetsTable.addKeyListener(listen = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {if (e.getKeyCode() == KeyEvent.VK_ENTER){
				
			}}
			
			@Override
			public void keyPressed(KeyEvent a) {if (a.getKeyCode() == KeyEvent.VK_SPACE){
					for (int row = 0; row < alp.size(); row++){
					try{
						alp.get(row).setX(Double.parseDouble(String.valueOf(planetsTable.getValueAt(row+1, 1))));
						alp.get(row).setY(Double.parseDouble(String.valueOf(planetsTable.getValueAt(row+1, 2))));
						alp.get(row).setMass(Integer.parseInt(String.valueOf(planetsTable.getValueAt(row+1, 3))));
						if ((String.valueOf(planetsTable.getValueAt(row+1, 4))).equalsIgnoreCase("fixed") || String.valueOf(planetsTable.getValueAt(row+1, 5)).equalsIgnoreCase("fixed")) {
							alp.get(row).setDx(0);
							alp.get(row).setDy(0);
							alp.get(row).setFixed(true);
						} else {
							alp.get(row).setDx(Double.parseDouble(String.valueOf(planetsTable.getValueAt(row+1,4))));
							alp.get(row).setDy(Double.parseDouble(String.valueOf(planetsTable.getValueAt(row+1,5))));
						}
						
					} catch (NumberFormatException g) {g.getMessage(); g.printStackTrace();}
				}
					for(int i2 = 0; i2 < alp.size(); i2++){
						Runner.drawPlanets.set(i2, alp.get(i2));
					}
					
			}
		}});
	}
	
			
	
	private Object[][] rows(ArrayList<Planet> alp) {
		
		Object [][] rows = new Object[alp.size()+1][6];

		rows[0][0] = "Index";
		rows[0][1] = "X Position";
		rows[0][2] = "Y Position";
		rows[0][3] = "Mass";
		rows[0][4] = "X Velocity";
		rows[0][5] = "Y Veloity";
		
		for (int r = 1; r < rows.length; r++){
			Planet p = alp.get(r-1);
			for (int c = 0; c < rows[0].length; c++){
				if (c == 0)
					rows[r][c] = r;
				else if (c == 1)
					rows[r][c] = p.x();
				else if (c == 2)
					rows[r][c] = p.y();
				else if (c == 3)
					rows[r][c] = p.getMass();
				else if (c == 4)
					rows[r][c] = p.getDx();
				else if (c == 5)
					rows[r][c] = p.getDy();
				else if (p.getFixed()){
					rows[r][4] = (Object)"Fixed";
					rows[r][5] = (Object)"Fixed";
				}
			}
		}
		return rows;
	}
}
