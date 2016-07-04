package orbits;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Runner{
	
	public static ArrayList<Planet> drawPlanets = new ArrayList<Planet>();
	
	public static GUIMenu handle = new GUIMenu("Orbits");
	
	public static Timer timer = new Timer(1, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
		}
	});

	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable()
			{
				public void run() {
					
					try {
						
						handle.setVisible(true);
						
						handle.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						
						handle.setResizable(false);
								
					} catch (Exception e) {
						
						e.printStackTrace();
						
					}
				}
			}
		);
	}
}
