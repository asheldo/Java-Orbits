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
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						Runner.handle.dispfield.setText("\n\t Welcome to orbit simulator! Enter X and Y coordinates, mass, and X and Y component velocities and click\r\n\tNew Planet to add it to the array list of Planets. Click simulate to start/stop the simulation. To make a planet\r\n\t               ignore the gravity of other planets, enter fixed into either the X-Velocity or Y-Velocity field.");
						Runner.handle.tabbedPane.setSelectedIndex(0);
						Runner.handle.restartindex = 0;
						Runner.handle.makeplanet.setEnabled(true);
						Runner.handle.xpos.setEnabled(true);
						Runner.handle.ypos.setEnabled(true);
						Runner.handle.mass.setEnabled(true);
						Runner.handle.xvel.setEnabled(true);
						Runner.handle.yvel.setEnabled(true);
						Runner.handle.tabbedPane.setEnabled(true);
						Runner.handle.btnResetSimulation.setEnabled(true);
						Runner.handle.restartPlArrList = new ArrayList<Planet>();
						PlanetEditor.alp = new ArrayList<Planet>();
						Runner.drawPlanets = new ArrayList<Planet>();
						Runner.handle.editorIsUp = false;
						Runner.handle.crdb.dispose();
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
						Runner.handle.crdb.dispose();
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
