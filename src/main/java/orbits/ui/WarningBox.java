package orbits.ui;

import orbits.Simulation;

import java.awt.BorderLayout;
//import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;

import java.awt.Color;
import java.awt.Font;

@SuppressWarnings("serial")
public class WarningBox extends JFrame{

	private JPanel contentPane;
	
	public WarningBox(){
		
			
		
		setVisible(true);
		setAlwaysOnTop(true);
		setAutoRequestFocus(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 200, 400, 200);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 204, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 18));
		textArea.setForeground(new Color(255, 204, 0));
		textArea.setBackground(Color.DARK_GRAY);
		contentPane.add(textArea, BorderLayout.CENTER);
		textArea.setText(Simulation.getInstance().getHandle().warningtext);
	}

}
