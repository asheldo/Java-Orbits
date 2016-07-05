package orbits;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Runner{
	
	public static Timer timer = new Timer(1, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
		}
	});

	public static void main(String[] args) {

		final Simulation sim = Simulation.getInstance();

		EventQueue.invokeLater(new Runnable()
			{
				public void run() {
					sim.getHandle().start();
				}
			}
		);
	}
}
