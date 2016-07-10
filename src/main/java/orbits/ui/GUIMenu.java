package orbits.ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.logging.Level;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import orbits.model.Planet;
import orbits.Simulation;

public class GUIMenu extends JFrame{

	private static final long serialVersionUID = 1L;

	private boolean presetFirstrun = true;
	
	public WarningBox warning;
	
	public String warningtext;
	
	public TextField xpos = new TextField();
	public TextField ypos = new TextField();
	public TextField mass = new TextField();
	public TextField xvel = new TextField();
	public TextField yvel = new TextField();
	
	private Board board = new Board();

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
	public JButton makeplanet, centermajor, distributeminor;
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

		welcome();

	}

	protected void welcome() {
		changeDisplayText("\n\t Welcome to orbit simulator! "
				+ "Enter X and Y coordinates, mass, and X and Y component velocities and click\r\n\t"
				+ "New Planet to add it to the array list of Planets. Click simulate to start/stop the simulation. "
				+ "To make a planet\r\n\t ignore the gravity of other planets, enter 'Fixed' into either the X-Velocity or Y-Velocity field.");
	}

	protected void changeDisplayText(String t) {
		dispfield.setText(t);
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
		getBoard().setSize(panel_2.getSize());
		panel_2.add(getBoard());
		getBoard().setBackground(Color.DARK_GRAY);
		getBoard().setLayout(new BoxLayout(getBoard(), BoxLayout.X_AXIS));
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

		layoutMoreCircularOrbits(50);
	}

	protected void layoutMoreCircularOrbits(int dupes) {
		for (int more = 0; more < dupes; ++more) {
			JButton CircularOrbit = new JButton("");
			final int n = more;
			CircularOrbit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					restartindex = 100 + n;
					recallConfig();
					}
			});
			CircularOrbit.setIcon(new ImageIcon(GUIMenu.class.getResource("/CircularOrbit.png")));
			CircularOrbit.setBounds(
					70 + 60 * (more / 5),
					11 + 60 * (more % 5),
					51, 51);
			PresetOrbitChooser.add(CircularOrbit);
		}
	}

	protected void recallConfig() {
		Simulation sim = Simulation.getInstance();
		sim.logStateForced("recallConfig: " + restartindex);

		if (restartindex == 0){
			String d = "Planets=" + sim.getPlanetCount();
			sim.logState(d, Level.WARNING);
			for(int i = 0; i < restartPlArrList.size(); i++) {
				sim.setPlanet(i, new Planet(restartPlArrList.get(i)));
				String temp = "  Index: " + (i+1) + "\t" + Simulation.getInstance().getDrawPlanet(i).toString() + "\n";
				d += temp;
			}
			changeDisplayText(d);
			sim.setRunning(false);
			tabbedPane.setSelectedIndex(3);
			tabbedPane.setEnabled(true);
		}
		else if (restartindex == 1) {
			sim.restart();

			sim.buildPlanet(0, 0, 5000, 0, 0, true);
			sim.buildPlanet(100, 0, 0, 0,
					// factorX *
					11, // 22
					false);

			if(!presetFirstrun) {
				sim.setRunning(false);
				tabbedPane.setSelectedIndex(3);
			} else {
				sim.setRunning(true);
				tabbedPane.setSelectedIndex(1);
				presetFirstrun = false;
			}
		}
		else if (restartindex >= 100) {
			sim.restart();
			Planet center = sim.buildPlanet(0, 0, 200000, 0, 0, true);
			double dim = sim.getOptions().getHalfMaxDimension();
			int numberBodies = restartindex - 100;
			double count = Math.pow((double) numberBodies, 0.75);
			// Top button in column of 5 generates many more small bodies
			if (numberBodies % 5 == 0) {
				for (int more = 0; more <= numberBodies; ++more) {
					Planet p1 = sim.buildPlanet(
							// (50 + 10 * more),
							center.getRadius() * 2.5
									+ (0.25 * dim * Math.pow((double) (more) / (double) (numberBodies), .33)),
							0 * more, // y
							1 + 4 * numberBodies, // mass
							0, // dx
							// dy
							center.getRadius() + (4 * Math.sqrt((double) (more) / (double) (numberBodies))),
							// 4 + 0.5 more,
							false);
					for (int distributed = 0; distributed < count; ++distributed) {
						Planet p2 = new Planet(p1);
						p2.setMass(p2.getMass() * .5 * ((double) distributed/(double) count));
						p2.randomizeOnCircle(getBoard().getConsts().dt);
						sim.addPlanet(p2);
					}
				}
				// Bottom 4 buttons in column of 5 each generates 1 more, increasing-size body
			} else {
				for (int more = 0; more <= numberBodies; ++more) {
					sim.buildPlanet(
							(50 + 3 * more),
							0 * more, // y
							5 * more, // mass
							0, // dx
							// dy
							4 + 0.2 * more, false);
				}
			}
			if (!presetFirstrun) {
				sim.setRunning(false);
				tabbedPane.setSelectedIndex(3);
			} else {
				sim.setRunning(true);
				tabbedPane.setSelectedIndex(1);
				presetFirstrun = false;
			}
		}
		else if (restartindex == 2) {
			sim.restart();

			sim.buildPlanet(-20, 0, 250, 0, 0, true);
			sim.buildPlanet(20, 0, 250, 0, 0, true);
			sim.buildPlanet(0, 0, 0, 0, 5, false);

			if(!presetFirstrun) {
				sim.setRunning(false);
				tabbedPane.setSelectedIndex(3);
			} else {
				sim.setRunning(true);
				tabbedPane.setSelectedIndex(1);
				presetFirstrun = false;
			}
		} else if (restartindex == 3) {
			sim.restart();

			sim.buildPlanet(200, 0, 1000, 0, 3, false);
			sim.buildPlanet(-200, 0, 1000, 0, -3, false);
			sim.buildPlanet(0, 200, 1000, -3, 0, false);
			sim.buildPlanet(0, -200, 1000, 3, 0, false);

			if(!presetFirstrun) {
				sim.setRunning(false);
				tabbedPane.setSelectedIndex(3);
			} else {
				sim.setRunning(true);
				tabbedPane.setSelectedIndex(1);
				presetFirstrun = false;
			}
		}
	}

	int centerPlanet = 0;

	public void centerMinorBody() {
		Simulation sim = Simulation.getInstance();
		int count = sim.getPlanetCount();
		if (centerPlanet >= count) {
			centerOne(centerPlanet = 0);
		} else {
			centerOne(centerPlanet += (count > 100 ? 5 : 1));
		}
	}

	public void centerMajorBody() {
		centerOne(centerPlanet = 0);
	}

	protected void centerOne(int n) {
		Simulation sim = Simulation.getInstance();

		Iterator<Planet> planets = sim.planetSizeIterator();
		if (!planets.hasNext()) {
			warningtext = "\n   No planets to center.";
			try {
				warning.dispose();
			} catch (NullPointerException npe) {
			}
			warning = new WarningBox();
			return;
		}
		int i = 0;
		Planet center = null;
		while (planets.hasNext() && i++ <= n) {
			center = planets.next();
			if (i == 1) {
				sim.setFixedCenter(center);
			}
		}

		double dcx = center.x(), dcy = center.y();
		if (i == 1 && !planets.hasNext()) {
			warningtext = "\n   Only 1 planet to center.";
			try{warning.dispose();} catch (NullPointerException npe) {}
			warning = new WarningBox();
		} else {
			// sim.logState("Center: " + center.toString());
		}

		center.setX(0);
		center.setY(0);
		if (n == 0) {
			// center.setFixed(true);
		}
		else {
			planets = sim.planetSizeIterator();
		}
		while (planets.hasNext()) {
			Planet p = planets.next();
			if (!p.equals(center)) {
				p.setX(p.x() - dcx);
				p.setY(p.y() - dcy);
			}
		}
		sim.logState("Centered: " + sim.getDrawPlanets().toString());

		if (sim.getHandle().tabbedPane.getSelectedIndex() == 3) {

			//TODO: mouse listener for tabbedPane at index 1 which is the board
			pEdit = new PlanetEditor();
			editorIsUp = true;
			tabbedPane.setComponentAt(3, pEdit);
		}
	}

	public void distributeMinorBodies() {
		Simulation sim = Simulation.getInstance();
		Iterator<Planet> planets = sim.planetSizeIterator();
		if (!planets.hasNext()) {
			warningtext = "\n   No planets to randomize.";
			try{warning.dispose();} catch (NullPointerException npe) {}
			warning = new WarningBox();
			return;
		}

		Planet center = planets.next();
		sim.setFixedCenter(center);
		double dcx = center.x(), dcy = center.y();
		if (!planets.hasNext()) {
			warningtext = "\n   No minor bodies to randomize.";
			try{warning.dispose();} catch (NullPointerException npe) {}
			warning = new WarningBox();
		} else {
			sim.logState("Randomize around: " + center.toString());
		}

		while (planets.hasNext()) {
			Planet p = planets.next();
			p.randomizeOnCircle(getBoard().getConsts().dt);
		}
		sim.logState("Randomized: " + sim.getDrawPlanets().toString());

		if (sim.getHandle().tabbedPane.getSelectedIndex() == 3) {

			//TODO: mouse listener for tabbedPane at index 1 which is the board
			pEdit = new PlanetEditor();
			editorIsUp = true;
			tabbedPane.setComponentAt(3, pEdit);
		}
	}

	public void creationOfPlanet() {
		Simulation sim = Simulation.getInstance();
		if (sim.getHandle().tabbedPane.getSelectedIndex() != 3) {

			//TODO: mouse listener for tabbedPane at index 1 which is the board

		} else {
			boolean encounteredError = false;
			boolean empty = false;
			List<Planet> planets = sim.getDrawPlanets();
			if (planets.size() == 0) {
				empty = true;
			}
			int errorIndex = -1;


			double tryX = -364;  //boolean xTried = false;
			double tryY = -323;  //boolean yTried = false;
			int tryMass = 0;  //boolean massTried = false;
			double tryDX = 0; //boolean dxTried = false;
			double tryDY = 0; //boolean dyTried = false;
			boolean tryFixed = false;

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

			errorIndex = sim.notEmpty(planets, tryX, tryY);
			if (errorIndex == -1) {
				empty = true;
			}
			else if ((errorIndex != -1)&&((!xpos.getText().equalsIgnoreCase("X-Position")))
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

	public Board getBoard() {
		return board;
	}

}
