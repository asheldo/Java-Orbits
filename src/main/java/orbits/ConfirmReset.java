package orbits;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class ConfirmReset extends JDialog {

	private final JPanel contentPanel = new JPanel();

	public ConfirmReset() {
		setUndecorated(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ConfirmReset.class.getResource("/javax/swing/plaf/metal/icons/Warn.gif")));
		setBounds(13, 670, 197, 83);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.RED);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JTextArea txtrAreYouSure = new JTextArea();
			txtrAreYouSure.setBounds(5, 5, 187, 49);
			txtrAreYouSure.setText("Are you sure you want\r\n      to reset?\r\n\r\n");
			txtrAreYouSure.setForeground(Color.CYAN);
			txtrAreYouSure.setBackground(Color.DARK_GRAY);
			txtrAreYouSure.setEditable(false);
			contentPanel.add(txtrAreYouSure);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(Color.RED);
			FlowLayout fl_buttonPane = new FlowLayout(FlowLayout.RIGHT);
			fl_buttonPane.setVgap(3);
			buttonPane.setLayout(fl_buttonPane);
			getContentPane().add(buttonPane, BorderLayout.SOUTH);

			final Simulation sim = Simulation.getInstance();
			final GUIMenu board = sim.getHandle();

			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						board.dispfield.setText("\n\t Welcome to orbit simulator! Enter X and Y coordinates, mass, and X and Y component velocities and click\r\n\tNew Planet to add it to the array list of Planets. Click simulate to start/stop the simulation. To make a planet\r\n\t               ignore the gravity of other planets, enter fixed into either the X-Velocity or Y-Velocity field.");
						board.tabbedPane.setSelectedIndex(0);
						board.restartindex = 0;
						board.makeplanet.setEnabled(true);
						board.xpos.setEnabled(true);
						board.ypos.setEnabled(true);
						board.mass.setEnabled(true);
						board.xvel.setEnabled(true);
						board.yvel.setEnabled(true);
						board.tabbedPane.setEnabled(true);
						board.btnResetSimulation.setEnabled(true);
						board.restartPlArrList = new ArrayList<Planet>();
						PlanetEditor.alp = new ArrayList<Planet>();
						sim.restart();
						board.editorIsUp = false;
						board.crdb.dispose();
					}
				});
				okButton.setBackground(Color.DARK_GRAY);
				okButton.setForeground(Color.CYAN);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						board.crdb.dispose();
					}
				});
				cancelButton.setForeground(Color.CYAN);
				cancelButton.setBackground(Color.DARK_GRAY);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
