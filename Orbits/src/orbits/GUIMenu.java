package orbits;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

//mport net.miginfocom.swing.MigLayout;

//import javax.swing.table.DefaultTableModel;

public class GUIMenu extends JFrame{
	
	
	private static final long serialVersionUID = 1L;

	public boolean isRunning; //false
	
	private boolean presetFirstrun = true;
	
	public WarningBox warning;
	
	public String warningtext;
	
	public TextField xpos = new TextField();
	public TextField ypos = new TextField();
	public TextField mass = new TextField();
	public TextField xvel = new TextField();
	public TextField yvel = new TextField();
	
	public boolean editorIsUp = false;
	
	public JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
	public Board board = new Board();
	public ConfirmReset crdb;
	public JTextArea dispfield = new JTextArea();
	
	public int restartindex;
	public JPanel panel_2 = new JPanel(true);
	public JPanel PlanetListTab;
	public JPanel SimulationTab;
	public JPanel PresetOrbitChooser;
	public JPanel contentPane;
	public JButton btnResetSimulation = new JButton("Reset");
	public JButton btnRestartSimulation = new JButton("Restart Simulation");
	public JButton makeplanet;
	public JButton btnsimulate;
	public ArrayList<Planet> restartPlArrList = new ArrayList<Planet>();
//	public JButton btnEditPlanets = new JButton("Edit Planets");
	private final JTextField textField = new JTextField();
	private final JTextField textField_1 = new JTextField();
	//private final JPanel panel = new JPanel();
	public PlanetEditor pEdit;
	public JPanel pEditHolder = new JPanel();
	
		public GUIMenu(String title) {
		setForeground(Color.CYAN);
		setBackground(Color.DARK_GRAY);
		setBounds(5, 5, 940, 755);
		
		setTitle(title);
		
		contentPane = new JPanel();
		contentPane.setForeground(Color.CYAN);
		contentPane.setBackground(Color.CYAN);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBackground(Color.CYAN);
		splitPane.setEnabled(false);
		splitPane.setContinuousLayout(true);
		splitPane.setForeground(Color.DARK_GRAY);
		contentPane.add(splitPane);
		
		JPanel ControlBox = new JPanel();
		ControlBox.setBorder(null);
		ControlBox.setLocation(new Point(5, 0));
		ControlBox.setBackground(Color.CYAN);
		splitPane.setLeftComponent(ControlBox);
		GridBagLayout gbl_ControlBox = new GridBagLayout();
		gbl_ControlBox.columnWidths = new int[] {135, 5};
		gbl_ControlBox.rowHeights = new int[] {33, 0, 0, 0, 0, 0, 33, 33, 33, 405, 33, 0};
		gbl_ControlBox.columnWeights = new double[] {1.0, Double.MIN_VALUE};
		gbl_ControlBox.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		ControlBox.setLayout(gbl_ControlBox);
		
		
		// New Planet button
		{
			makeplanet = new JButton("New Planet");
			makeplanet.setFont(new Font("Tahoma", Font.BOLD, 12));
			makeplanet.setBackground(Color.BLACK);
			makeplanet.setForeground(Color.CYAN);
			GridBagConstraints gbc_makeplanet = new GridBagConstraints();
			gbc_makeplanet.fill = GridBagConstraints.BOTH;
			gbc_makeplanet.insets = new Insets(0, 0, 5, 0);
			gbc_makeplanet.gridx = 0;
			gbc_makeplanet.gridy = 0;
			ControlBox.add(makeplanet, gbc_makeplanet);
			makeplanet.addActionListener (new ActionListener() {public void actionPerformed(ActionEvent e) { 
				creationOfPlanet();
				
			}});
		}
		
		{
			xpos.setForeground(Color.CYAN);
			xpos.setBackground(Color.DARK_GRAY);
			xpos.setFont(new Font("Consolas", Font.ITALIC, 12));
			xpos.setText("X-Coordinate");
			GridBagConstraints gbc_xpos = new GridBagConstraints();
			gbc_xpos.fill = GridBagConstraints.HORIZONTAL;
			gbc_xpos.insets = new Insets(0, 0, 5, 0);
			gbc_xpos.gridx = 0;
			gbc_xpos.gridy = 1;
			ControlBox.add(xpos, gbc_xpos);
			xpos.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent f) {
					tabbedPane.setSelectedIndex(tabbedPane.indexOfComponent(pEdit));
					if(xpos.getText().equalsIgnoreCase("X-Coordinate"))
					xpos.setText("");
				}
				public void focusLost(FocusEvent f) {
					if(xpos.getText().equalsIgnoreCase(""))
						xpos.setText("X-Coordinate");
				}
			});
		}
				
		
		// Y position text box
		{
			ypos.setForeground(Color.CYAN);
			ypos.setBackground(Color.DARK_GRAY);
			ypos.setFont(new Font("Consolas", Font.ITALIC, 12));
			ypos.setText("Y-Coordinate");
			GridBagConstraints gbc_ypos = new GridBagConstraints();
			gbc_ypos.fill = GridBagConstraints.HORIZONTAL;
			gbc_ypos.insets = new Insets(0, 0, 5, 0);
			gbc_ypos.gridx = 0;
			gbc_ypos.gridy = 2;
			ControlBox.add(ypos, gbc_ypos);
			ypos.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent f) {
					if(ypos.getText().equalsIgnoreCase("Y-Coordinate"))
					ypos.setText("");
				}
				public void focusLost(FocusEvent f) {
					if(ypos.getText().equalsIgnoreCase(""))
						ypos.setText("Y-Coordinate");
				}
			});
		}
		
		
		// Mass text box
		{
			mass.setBackground(Color.DARK_GRAY);
			mass.setForeground(Color.CYAN);
			mass.setFont(new Font("Consolas", Font.ITALIC, 12));		
			mass.setText("Mass");
			GridBagConstraints gbc_mass = new GridBagConstraints();
			gbc_mass.fill = GridBagConstraints.HORIZONTAL;
			gbc_mass.insets = new Insets(0, 0, 5, 0);
			gbc_mass.gridx = 0;
			gbc_mass.gridy = 3;
			ControlBox.add(mass, gbc_mass);
			mass.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent f) {
					if(mass.getText().equalsIgnoreCase("Mass"))
					mass.setText("");
				}
				public void focusLost(FocusEvent f) {
					if(mass.getText().equalsIgnoreCase(""))
						mass.setText("Mass");
				}
			});
		}
		
		
		// X-Velocity text box
		{
			xvel.setForeground(Color.CYAN);
			xvel.setBackground(Color.DARK_GRAY);
			xvel.setFont(new Font("Consolas", Font.ITALIC, 12));
			xvel.setText("X-Velocity");
			GridBagConstraints gbc_xvel = new GridBagConstraints();
			gbc_xvel.fill = GridBagConstraints.HORIZONTAL;
			gbc_xvel.insets = new Insets(0, 0, 5, 0);
			gbc_xvel.gridx = 0;
			gbc_xvel.gridy = 4;
			ControlBox.add(xvel, gbc_xvel);
			xvel.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent f) {
					if(xvel.getText().equalsIgnoreCase("X-Velocity"))
					xvel.setText("");
				}
				public void focusLost(FocusEvent f) {
					if(xvel.getText().equalsIgnoreCase(""))
						xvel.setText("X-Velocity");
				}
			});
		}
		
		
		// Y-Velocity text box
		{
			yvel.setBackground(Color.DARK_GRAY);
			yvel.setForeground(Color.CYAN);
			yvel.setFont(new Font("Consolas", Font.ITALIC, 12));
			yvel.setText("Y-Velocity");
			GridBagConstraints gbc_yvel = new GridBagConstraints();
			gbc_yvel.insets = new Insets(0, 0, 5, 0);
			gbc_yvel.fill = GridBagConstraints.HORIZONTAL;
			gbc_yvel.gridx = 0;
			gbc_yvel.gridy = 5;
			ControlBox.add(yvel, gbc_yvel);
			yvel.addKeyListener(new KeyListener() {
				
				@Override
				public void keyTyped(KeyEvent e) {
				}
				
				@Override
				public void keyReleased(KeyEvent e) {
					
				}
				
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						creationOfPlanet();
				}
				}
			});

			GridBagConstraints gbc_btnRestartSimulation = new GridBagConstraints();
			gbc_btnRestartSimulation.fill = GridBagConstraints.BOTH;
			gbc_btnRestartSimulation.insets = new Insets(0, 0, 5, 0);
			gbc_btnRestartSimulation.gridx = 0;
			gbc_btnRestartSimulation.gridy = 7;
			btnRestartSimulation.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnRestartSimulation.setForeground(Color.CYAN);
			btnRestartSimulation.setBackground(Color.BLACK);
			btnRestartSimulation.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					recallConfig();
					Runner.handle.repaint();
				}
			});
			textField.setBackground(Color.GRAY);
			textField.setEditable(false);
			textField.setColumns(10);
			
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.insets = new Insets(0, 0, 5, 0);
			gbc_textField.fill = GridBagConstraints.BOTH;
			gbc_textField.gridx = 0;
			gbc_textField.gridy = 6;
			ControlBox.add(textField, gbc_textField);
			ControlBox.add(btnRestartSimulation, gbc_btnRestartSimulation);
			
			GridBagConstraints gbc_btnResetSimulation = new GridBagConstraints();
			gbc_btnResetSimulation.fill = GridBagConstraints.BOTH;
			gbc_btnResetSimulation.gridx = 0;
			gbc_btnResetSimulation.gridy = 10;
			btnResetSimulation.setIcon(new ImageIcon(GUIMenu.class.getResource("/com/sun/javafx/scene/web/skin/Undo_16x16_JFX.png")));
			btnResetSimulation.setFont(new Font("Tahoma", Font.BOLD, 11));
			btnResetSimulation.setForeground(Color.CYAN);
			btnResetSimulation.setBackground(Color.BLACK);
			btnResetSimulation.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					crdb = new ConfirmReset();
				}
			});
			dispfield.setText("\n\t Welcome to orbit simulator! Enter X and Y coordinates, mass, and X and Y component velocities and click\r\n\tNew Planet to add it to the array list of Planets. Click simulate to start/stop the simulation. To make a planet\r\n\t               ignore the gravity of other planets, enter fixed into either the X-Velocity or Y-Velocity field.");
			
			// Simulate button
			{
				JButton btnSimulate = new JButton("Simulate");
				btnSimulate.setSelectedIcon(new ImageIcon(GUIMenu.class.getResource("/com/sun/javafx/webkit/prism/resources/mediaPause.png")));
				btnSimulate.setForeground(Color.CYAN);
				btnSimulate.setBackground(Color.BLACK);
				btnSimulate.setIcon(new ImageIcon(GUIMenu.class.getResource("/com/sun/javafx/webkit/prism/resources/mediaPlayDisabled.png")));
				GridBagConstraints gbc_btnSimulate = new GridBagConstraints();
				gbc_btnSimulate.fill = GridBagConstraints.BOTH;
				gbc_btnSimulate.insets = new Insets(0, 0, 5, 0);
				gbc_btnSimulate.gridx = 0;
				gbc_btnSimulate.gridy = 8;
				ControlBox.add(btnSimulate, gbc_btnSimulate);
				btnSimulate.addActionListener (
					new ActionListener() {
						public void actionPerformed(ActionEvent h) {
							
							if (isRunning == false) {
								isRunning = true;
								
							} else {
								isRunning = false;
							}
							tabbedPane.setSelectedIndex(1);
							Runner.handle.repaint();
						}
					}
				);
			}
			textField_1.setForeground(new Color(128, 128, 128));
			textField_1.setBackground(Color.GRAY);
			textField_1.setEditable(false);
			textField_1.setColumns(10);
			
			GridBagConstraints gbc_textField_1 = new GridBagConstraints();
			gbc_textField_1.insets = new Insets(0, 0, 5, 0);
			gbc_textField_1.fill = GridBagConstraints.BOTH;
			gbc_textField_1.gridx = 0;
			gbc_textField_1.gridy = 9;
			ControlBox.add(textField_1, gbc_textField_1);
			ControlBox.add(btnResetSimulation, gbc_btnResetSimulation);
			yvel.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent f) {
					if(yvel.getText().equalsIgnoreCase("Y-Velocity"))
					yvel.setText("");
				}
				public void focusLost(FocusEvent f) {
					if(yvel.getText().equalsIgnoreCase(""))
						yvel.setText("Y-Velocity");
				}
			});
		}
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		splitPane.setRightComponent(scrollPane);
		
		
		JPanel TabPaneHolder = new JPanel();
		scrollPane.setViewportView(TabPaneHolder);
		TabPaneHolder.setLayout(null);
		tabbedPane.setBounds(0, 0, 1193, 715);
		
		
		TabPaneHolder.add(tabbedPane);
		
		PlanetListTab = new JPanel();
		tabbedPane.addTab("Info:", null, PlanetListTab, null);
		PlanetListTab.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("661px:grow"),},
			new RowSpec[] {
				RowSpec.decode("90px"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("597px:grow"),}));
		dispfield.setForeground(Color.CYAN);
		dispfield.setBackground(Color.DARK_GRAY);
		
		PlanetListTab.add(dispfield, "1, 1, 2, 3, fill, fill");
		
		SimulationTab = new JPanel();
		tabbedPane.addTab("Simulation", null, SimulationTab, null);
		SimulationTab.setLayout(null);
		panel_2.setBounds(10, 5, 750, 690);
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		SimulationTab.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));
		board.setSize(panel_2.getSize());
		panel_2.add(board);
		board.setBackground(Color.DARK_GRAY);
		board.setLayout(new BoxLayout(board, BoxLayout.X_AXIS));
		
		PresetOrbitChooser = new JPanel();
		tabbedPane.addTab("Presets", null, PresetOrbitChooser, null);
		PresetOrbitChooser.setLayout(null);
		
		JButton CircularOrbit = new JButton("");
		CircularOrbit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			restartindex = 1;
			recallConfig();
			}
		});
		CircularOrbit.setIcon(new ImageIcon(GUIMenu.class.getResource("/orbits/CircularOrbit.png")));
		CircularOrbit.setBounds(10, 11, 51, 51);
		PresetOrbitChooser.add(CircularOrbit);
		
		JButton LinearOrbit = new JButton("");
		LinearOrbit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			restartindex = 2;
			recallConfig();
			}
		});
		LinearOrbit.setIcon(new ImageIcon(GUIMenu.class.getResource("/orbits/LinearOrbit.png")));
		LinearOrbit.setBounds(10, 73, 51, 51);
		PresetOrbitChooser.add(LinearOrbit);
		
		JButton QuadOrbit = new JButton("");
		QuadOrbit.setBounds(10, 135, 51, 51);
		QuadOrbit.setIcon(new ImageIcon(GUIMenu.class.getResource("/orbits/QuadOrbit.png")));
		QuadOrbit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			restartindex = 3;
			recallConfig();
			}
		});
		PresetOrbitChooser.add(QuadOrbit);
		
		pEditHolder.setForeground(Color.CYAN);
		pEditHolder.setBackground(Color.DARK_GRAY);
		
		
		
		tabbedPane.add("Editor", pEdit);
		tabbedPane.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				if (tabbedPane.getSelectedIndex()==tabbedPane.indexOfComponent(pEdit)){
					if(!editorIsUp){
						pEdit = new PlanetEditor();
						editorIsUp = true;
						tabbedPane.setComponentAt(3, pEdit);
					}
				}
				
			}
		});
		
		
	}
	
	public void newplanet() throws PlanetsCoincideError {
		//dispfield.setText(""+Runner.drawPlanets.size());
		try {
			for (int i = 0; i < Runner.drawPlanets.size(); i++) {
				
				/* This big ugly thing checks to see if the distance between the planet
				 that is currently being made is less than the diameter of a planet,
				 if it is then it throws a PlanetsCoincideError.*/
				if (Math.sqrt(Math.pow(Runner.drawPlanets.get(i).x() - Double.parseDouble(xpos.getText()), 2) + Math.pow(Runner.drawPlanets.get(i).y() - Double.parseDouble(ypos.getText()), 2)) < 10) {
					throw new PlanetsCoincideError(Runner.drawPlanets.get(i));
				}
			}
			if ((yvel.getText().equalsIgnoreCase("fixed")) || (xvel.getText().equalsIgnoreCase("fixed"))) {
				
				Runner.drawPlanets.add(new Planet(Double.parseDouble(xpos.getText()),
						Double.parseDouble(ypos.getText()),
						Integer.parseInt(mass.getText()), 0, 0, true));
				
				restartPlArrList.add(new Planet(Double.parseDouble(xpos.getText()),
						Double.parseDouble(ypos.getText()),
						Integer.parseInt(mass.getText()), 0, 0, true));
				
			} else {
				
				Runner.drawPlanets.add(new Planet(Double.parseDouble(xpos.getText()),
					Double.parseDouble(ypos.getText()),
					Integer.parseInt(mass.getText()),
					Double.parseDouble(xvel.getText()),
					Double.parseDouble(yvel.getText()), false));
			
			restartPlArrList.add(new Planet(Double.parseDouble(xpos.getText()),
					Double.parseDouble(ypos.getText()),
					Integer.parseInt(mass.getText()),
					Double.parseDouble(xvel.getText()),
					Double.parseDouble(yvel.getText()), false));
			}
			
//			String newbox = ""
			
			String displaytext = "";
			
			for(int i = 0; i < Runner.drawPlanets.size(); i++) {
				String temp = "  Index: " + (i+1) + "\t" + Runner.drawPlanets.get(i).toString() + "\n";
				displaytext += temp;
			}
				
				
				dispfield.setText(displaytext);
				xpos.setText("X-Coordinate");
				ypos.setText("Y-Coordinate");
				mass.setText("Mass");
				xvel.setText("X-Velocity");
				yvel.setText("Y-Velocity");
				
		
				
		
		} catch(NumberFormatException f) {
			
			dispfield.setText(f.toString() + f.fillInStackTrace());
			
		}
		
	}
	public void recallConfig() {
		if (restartindex == 0){
			String d = "";
				
			for(int i = 0; i < restartPlArrList.size(); i++) {
				Runner.drawPlanets.set(i, new Planet(restartPlArrList.get(i)));
				String temp = "  Index: " + (i+1) + "\t" + Runner.drawPlanets.get(i).toString() + "\n";
				d += temp;
			}
			dispfield.setText(d);
			isRunning = false;
			tabbedPane.setSelectedIndex(3);
		} else if (restartindex == 1) {
			Runner.drawPlanets = new ArrayList<Planet>();
			Runner.drawPlanets.add(new Planet(0, 0, 5000, 0, 0, true));
			Runner.drawPlanets.add(new Planet(100, 0, 0, 0, 22, false));
			if(!presetFirstrun) {
				isRunning = false;
				tabbedPane.setSelectedIndex(3);
			} else {
				isRunning = true;
				tabbedPane.setSelectedIndex(1);
				presetFirstrun = false;
			}
		} else if (restartindex == 2) {
			Runner.drawPlanets = new ArrayList<Planet>();
			Runner.drawPlanets.add(new Planet(-20, 0, 2500, 0, 0, true));
			Runner.drawPlanets.add(new Planet(20, 0, 2500, 0, 0, true));
			Runner.drawPlanets.add(new Planet(0, 0, 0, 0, 66, false));
			if(!presetFirstrun) {
				isRunning = false;
				tabbedPane.setSelectedIndex(3);
			} else {
				isRunning = true;
				tabbedPane.setSelectedIndex(1);
				presetFirstrun = false;
			}
		} else if (restartindex == 3) {
			Runner.drawPlanets = new ArrayList<Planet>();
			Runner.drawPlanets.add(new Planet(200, 0, 10000, 0, 10, false));
			Runner.drawPlanets.add(new Planet(-200, 0, 10000, 0, -10, false));
			Runner.drawPlanets.add(new Planet(0, 200, 10000, -10, 0, false));
			Runner.drawPlanets.add(new Planet(0, -200, 10000, 10, 0, false));
			if(!presetFirstrun) {
				isRunning = false;
				tabbedPane.setSelectedIndex(3);
			} else {
				isRunning = true;
				tabbedPane.setSelectedIndex(1);
				presetFirstrun = false;
			}
		}
	}
	public void creationOfPlanet() {
		if (Runner.handle.tabbedPane.getSelectedIndex() != 3) {
			//try{newplanet();}
			//catch(PlanetsCoincideError f) {dispfield.setText(f.getMessage());}
	} else {
		boolean empty = false;
		
		double tryX = -364;  //boolean xTried = false;
		double tryY = -323;  //boolean yTried = false;
		int tryMass = 0;  //boolean massTried = false;
		double tryDX = 0; //boolean dxTried = false;
		double tryDY = 0; //boolean dyTried = false;
		boolean tryFixed = false;
		
		
		boolean encounteredError = false;
		
		try {tryX    = Double.parseDouble(xpos.getText());} catch (NumberFormatException a) {encounteredError = true;} 
		try {tryY    = Double.parseDouble(ypos.getText());} catch (NumberFormatException a) {encounteredError = true;}
		try {tryMass = Integer.  parseInt(mass.getText());} catch (NumberFormatException a) {encounteredError = true;}
		try {tryDX   = Double.parseDouble(xvel.getText());} catch (NumberFormatException a) {encounteredError = true;}
		try {tryDY   = Double.parseDouble(yvel.getText());} catch (NumberFormatException a) {encounteredError = true;}
		
		if(encounteredError){
			warningtext = "\n  Error parsing contents of some\n   entry fields, default values\n       have been inserted.";
			try{warning.dispose();} catch (NullPointerException npe) {}
			warning = new WarningBox();
		}
		
		if (String.valueOf(xvel.getText()).equalsIgnoreCase("FIXED") || (String.valueOf(yvel.getText()).equalsIgnoreCase("FIXED"))) {
			tryDX = 0;
			tryDY = 0;
			tryFixed = true;
		}
		
		
		if (Runner.drawPlanets.size() == 0) {
			empty = true;
		}
		
		int errorIndex = -1;
		
		while(!empty){
			for (Planet p: Runner.drawPlanets){
				if ((Math.sqrt(Math.pow(p.x() - tryX, 2) + Math.pow(p.y() - tryY, 2)) < 10)) {
					tryX += 11;
					errorIndex = Runner.drawPlanets.indexOf(p);
				} else {
					empty = true;
				}
			}
		}
		
		
		if ((errorIndex != -1)&&((!xpos.getText().equalsIgnoreCase("X-Position")))&&(!(ypos.getText().equalsIgnoreCase("Y-Position")))){
			Planet p = Runner.drawPlanets.get(errorIndex);
			warningtext = "\n   The planet you are trying to\n   make would coincide with the\n      planet at (" + (int)p.x() + " ," + (int)p.y() + ")";
			try{warning.dispose();} catch (NullPointerException npe) {}
			warning = new WarningBox();
		}
		
//		if(xvel.getText().equalsIgnoreCase("X-Velocity") && yvel.getText().equalsIgnoreCase("Y-Velocity")) {
//			int numOfFixed = 0;
//			for(Planet p : Runner.drawPlanets){
//				if(p.getFixed()) numOfFixed ++;
//			}
//			if (numOfFixed == 1){
//				
//			}
//			
//		}
		
		if(empty)
			Runner.drawPlanets.add(new Planet(tryX, tryY, tryMass, tryDX, tryDY, tryFixed));
		xpos.setText("X-Coordinate");
		ypos.setText("Y-Coordinate");
		mass.setText("Mass");
		xvel.setText("X-Velocity");
		yvel.setText("Y-Velocity");
		Runner.handle.tabbedPane.setSelectedIndex(0);
		Runner.handle.tabbedPane.setSelectedIndex(3);
		
	}
	}

}
