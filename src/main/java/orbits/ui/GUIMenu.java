package orbits.ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import orbits.model.Planet;
import orbits.Simulation;

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
	
	public Board board = new Board();

	public int restartindex;

	public boolean editorIsUp = false;

	public JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

	public ConfirmReset crdb;
	public JTextArea dispfield = new JTextArea();

	public JPanel panel_2 = new JPanel(true);
	public JPanel PlanetListTab;
	public JPanel SimulationTab;
	public JPanel PresetOrbitChooser;
	public JPanel contentPane;
	public JButton btnResetSimulation = new JButton("Reset");
	public JButton btnRestartSimulation = new JButton("Restart Simulation");
	public JButton makeplanet;
	public JButton btnsimulate;

	public PlanetEditor pEdit;
	public JPanel pEditHolder = new JPanel();

	public ArrayList<Planet> restartPlArrList = new ArrayList<Planet>();

	public GUIMenu(String title) {
		layout(title);
	}

	public void start() {
		try {
			setVisible(true);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setResizable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void layout(String title) {
		setForeground(Color.CYAN);
		setBackground(Color.DARK_GRAY);
		setBounds(5, 5, 940, 755);

		setTitle(title);
		GUIBuilder builder = new GUIBuilder(this);
		JSplitPane splitPane = builder.buildPane();

		layoutScrollsAndPanes(splitPane);
		layoutOrbitChooser();
		layoutEditor();

		dispfield.setText("\n\t Welcome to orbit simulator! "
				+ "Enter X and Y coordinates, mass, and X and Y component velocities and click\r\n\tNew Planet to add it to the array list of Planets. Click simulate to start/stop the simulation. "
				+ "To make a planet\r\n\t ignore the gravity of other planets, enter 'Fixed' into either the X-Velocity or Y-Velocity field.");
	}

	protected void layoutEditor() {
		pEditHolder.setForeground(Color.CYAN);
		pEditHolder.setBackground(Color.DARK_GRAY);

		tabbedPane.add("Editor", pEdit);
		tabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (tabbedPane.getSelectedIndex() == tabbedPane.indexOfComponent(pEdit)) {
					if (!editorIsUp) {
						pEdit = new PlanetEditor();
						editorIsUp = true;
						tabbedPane.setComponentAt(3, pEdit);
					}
				}
			}
		});

	}

	protected void layoutScrollsAndPanes(JSplitPane splitPane) {
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
		PlanetListTab.setLayout(new FormLayout(new ColumnSpec[]{
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("661px:grow"),},
				new RowSpec[]{
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
	}

	protected void layoutOrbitChooser() {
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
		CircularOrbit.setIcon(new ImageIcon(GUIMenu.class.getResource("/CircularOrbit.png")));
		CircularOrbit.setBounds(10, 11, 51, 51);
		PresetOrbitChooser.add(CircularOrbit);

		JButton LinearOrbit = new JButton("");
		LinearOrbit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				restartindex = 2;
				recallConfig();
			}
		});
		LinearOrbit.setIcon(new ImageIcon(GUIMenu.class.getResource("/LinearOrbit.png")));
		LinearOrbit.setBounds(10, 73, 51, 51);
		PresetOrbitChooser.add(LinearOrbit);

		JButton QuadOrbit = new JButton("");
		QuadOrbit.setBounds(10, 135, 51, 51);
		QuadOrbit.setIcon(new ImageIcon(GUIMenu.class.getResource("/QuadOrbit.png")));
		QuadOrbit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				restartindex = 3;
				recallConfig();
			}
		});
		PresetOrbitChooser.add(QuadOrbit);
	}

	protected void recallConfig() {
		Simulation sim = Simulation.getInstance();
		if (restartindex == 0){
			String d = "";
			for(int i = 0; i < restartPlArrList.size(); i++) {
				sim.setPlanet(i, new Planet(restartPlArrList.get(i)));
				String temp = "  Index: " + (i+1) + "\t" + Simulation.getInstance().getDrawPlanet(i).toString() + "\n";
				d += temp;
			}
			dispfield.setText(d);
			isRunning = false;
			tabbedPane.setSelectedIndex(3);
			tabbedPane.setEnabled(true);
		}
		else if (restartindex == 1) {
			sim.restart();
			sim.buildPlanet(0, 0, 5000, 0, 0, true);
			sim.buildPlanet(100, 0, 0, 0, 22, false);
			if(!presetFirstrun) {
				isRunning = false;
				tabbedPane.setSelectedIndex(3);
			} else {
				isRunning = true;
				tabbedPane.setSelectedIndex(1);
				presetFirstrun = false;
			}
		} else if (restartindex == 2) {
			sim.restart();
			sim.buildPlanet(-20, 0, 2500, 0, 0, true);
			sim.buildPlanet(20, 0, 2500, 0, 0, true);
			sim.buildPlanet(0, 0, 0, 0, 66, false);
			if(!presetFirstrun) {
				isRunning = false;
				tabbedPane.setSelectedIndex(3);
			} else {
				isRunning = true;
				tabbedPane.setSelectedIndex(1);
				presetFirstrun = false;
			}
		} else if (restartindex == 3) {
			sim.restart();
			sim.buildPlanet(200, 0, 10000, 0, 10, false);
			sim.buildPlanet(-200, 0, 10000, 0, -10, false);
			sim.buildPlanet(0, 200, 10000, -10, 0, false);
			sim.buildPlanet(0, -200, 10000, 10, 0, false);
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
		Simulation sim = Simulation.getInstance();
		if (sim.getHandle().tabbedPane.getSelectedIndex() != 3) {

			//TODO: mouse listener for tabbedPane at index 1 which is the board

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

			List<Planet> planets = sim.getDrawPlanets();
			if (planets.size() == 0) {
				empty = true;
			}

			int errorIndex = -1;

			while(!empty){
				int i = 0;
				for (Planet p: planets) {
					if ((Math.sqrt(Math.pow(p.x() - tryX, 2) + Math.pow(p.y() - tryY, 2))
							< 10)) {
						tryX += 11;
						errorIndex = i;
					} else {
						empty = true;
					}
					++i;
				}
			}


			if ((errorIndex != -1)&&((!xpos.getText().equalsIgnoreCase("X-Position")))
					&& (!(ypos.getText().equalsIgnoreCase("Y-Position")))){
				Planet p = sim.getDrawPlanet(errorIndex);
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

			if(empty) {
				//Simulation.getDrawPlanets().add(new Planet(tryX, tryY, tryMass, tryDX, tryDY, tryFixed));
				sim.buildPlanet(tryX, tryY, tryMass, tryDX, tryDY, tryFixed);
			}
			xpos.setText("X-Coordinate");
			ypos.setText("Y-Coordinate");
			mass.setText("Mass");
			xvel.setText("X-Velocity");
			yvel.setText("Y-Velocity");
			sim.getHandle().tabbedPane.setSelectedIndex(0);
			sim.getHandle().tabbedPane.setSelectedIndex(3);
		}
	}


}
