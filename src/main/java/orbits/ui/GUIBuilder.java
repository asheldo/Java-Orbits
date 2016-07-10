package orbits.ui;

import orbits.Simulation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by amunzer on 7/4/16.
 */
public class GUIBuilder {

    private JPanel contentPane;

    private GUIMenu window;

    private int gridY = 0;

    public GUIBuilder(GUIMenu window) {
        this.window = window;
        contentPane = new JPanel();
    }

    public JSplitPane buildPane() {

        JSplitPane splitPane = new JSplitPane();
        splitPane.setBackground(Color.CYAN);
        splitPane.setEnabled(false);
        splitPane.setContinuousLayout(true);
        splitPane.setForeground(Color.DARK_GRAY);

        contentPane.setForeground(Color.CYAN);
        contentPane.setBackground(Color.CYAN);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        window.setContentPane(contentPane);
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
        contentPane.add(splitPane);
        buildControlBox(splitPane);

        return splitPane;
    }

    protected void buildControlBox(JSplitPane splitPane) {
        JPanel ControlBox = new JPanel();
        ControlBox.setBorder(null);
        ControlBox.setLocation(new Point(5, 0));
        ControlBox.setBackground(Color.CYAN);
        splitPane.setLeftComponent(ControlBox);
        GridBagLayout gbl_ControlBox = new GridBagLayout();
        gbl_ControlBox.columnWidths = new int[]{135, 5};
        gbl_ControlBox.rowHeights = new int[]{
                33, 33, 33, 33, 0, 0, 0, 0, 0, 33, 33, 33, 306, 33, 0};
        gbl_ControlBox.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_ControlBox.rowWeights = new double[]{
                0.0, 0.0, 0.0, 0,0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        ControlBox.setLayout(gbl_ControlBox);

        buildNewPlanetButton(ControlBox);
        buildCenterMajorBodyButton(ControlBox);
        buildCenterMinorBodyButton(ControlBox);
        buildDistributeMinorButton(ControlBox);
        buildXPositionBox(ControlBox);
        buildYPositionBox(ControlBox);
        buildMassBox(ControlBox);
        buildXVelocityBox(ControlBox);
        buildYVelocityBox(ControlBox);
        buildTextField(ControlBox);
        buildRestartButton(ControlBox);
        buildSimulateButton(ControlBox);
        buildTextField_1(ControlBox);
        buildResetButton(ControlBox);
    }

        // New Planet button
    protected void buildNewPlanetButton(JPanel controlBox) {
        window.makeplanet = new JButton("New Planet");

        JButton makeplanet = window.makeplanet;
        makeplanet.setFont(new Font("Tahoma", Font.BOLD, 12));
        makeplanet.setBackground(Color.BLACK);
        makeplanet.setForeground(Color.BLUE);
        GridBagConstraints gbc_makeplanet = new GridBagConstraints();
        gbc_makeplanet.fill = GridBagConstraints.BOTH;
        gbc_makeplanet.insets = new Insets(0, 0, 5, 0);
        gbc_makeplanet.gridx = 0;
        gbc_makeplanet.gridy = 0;
        controlBox.add(makeplanet, gbc_makeplanet);
        makeplanet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                window.creationOfPlanet();
            }
        });
    }

    protected void buildCenterMajorBodyButton(JPanel controlBox) {
        window.centermajor = new JButton("Center Major P");

        JButton b = window.centermajor;
        b.setFont(new Font("Tahoma", Font.BOLD, 11));
        b.setBackground(Color.BLACK);
        b.setForeground(Color.BLUE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.gridx = 0;
        gbc.gridy = ++gridY;
        controlBox.add(b, gbc);
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                window.centerMajorBody();
            }
        });
    }

    protected void buildCenterMinorBodyButton(JPanel controlBox) {
        window.centermajor = new JButton("Center Minor P");

        JButton b = window.centermajor;
        b.setFont(new Font("Tahoma", Font.BOLD, 11));
        b.setBackground(Color.BLACK);
        b.setForeground(Color.BLUE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.gridx = 0;
        gbc.gridy = ++gridY;
        controlBox.add(b, gbc);
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                window.centerMinorBody();
            }
        });
    }

    // New Planet button
    protected void buildDistributeMinorButton(JPanel controlBox) {
        window.distributeminor = new JButton("Distribute Minor P");

        JButton b = window.distributeminor;
        b.setFont(new Font("Tahoma", Font.BOLD, 11));
        b.setBackground(Color.BLACK);
        b.setForeground(Color.BLUE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.gridx = 0;
        gbc.gridy = ++gridY;
        controlBox.add(b, gbc);
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                window.distributeMinorBodies();
            }
        });
    }

    // X position text box
    protected void buildXPositionBox(JPanel controlBox) {
        final TextField xpos = window.xpos;
        final JTabbedPane tabbedPane = window.tabbedPane;
        xpos.setForeground(Color.CYAN);
        xpos.setBackground(Color.DARK_GRAY);
        xpos.setFont(new Font("Consolas", Font.ITALIC, 12));
        xpos.setText("X-Coordinate");
        GridBagConstraints gbc_xpos = new GridBagConstraints();
        gbc_xpos.fill = GridBagConstraints.HORIZONTAL;
        gbc_xpos.insets = new Insets(0, 0, 5, 0);
        gbc_xpos.gridx = 0;
        gbc_xpos.gridy = ++gridY;
        controlBox.add(xpos, gbc_xpos);
        xpos.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent f) {
                tabbedPane.setSelectedIndex(tabbedPane.indexOfComponent(window.pEdit));
                if (xpos.getText().equalsIgnoreCase("X-Coordinate"))
                    xpos.setText("");
            }

            public void focusLost(FocusEvent f) {
                if (xpos.getText().equalsIgnoreCase(""))
                    xpos.setText("X-Coordinate");
            }
        });
    }


    protected void buildYPositionBox(JPanel controlBox)
    // Y position text box
    {
        final TextField ypos = window.ypos;

        ypos.setForeground(Color.CYAN);
        ypos.setBackground(Color.DARK_GRAY);
        ypos.setFont(new Font("Consolas", Font.ITALIC, 12));
        ypos.setText("Y-Coordinate");
        GridBagConstraints gbc_ypos = new GridBagConstraints();
        gbc_ypos.fill = GridBagConstraints.HORIZONTAL;
        gbc_ypos.insets = new Insets(0, 0, 5, 0);
        gbc_ypos.gridx = 0;
        gbc_ypos.gridy = ++gridY;
        controlBox.add(ypos, gbc_ypos);
        ypos.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent f) {
                if (ypos.getText().equalsIgnoreCase("Y-Coordinate"))
                    ypos.setText("");
            }

            public void focusLost(FocusEvent f) {
                if (ypos.getText().equalsIgnoreCase(""))
                    ypos.setText("Y-Coordinate");
            }
        });
    }

    protected void buildMassBox(JPanel controlBox)
    // Mass text box
    {
        final TextField mass = window.mass;

        mass.setBackground(Color.DARK_GRAY);
        mass.setForeground(Color.CYAN);
        mass.setFont(new Font("Consolas", Font.ITALIC, 12));
        mass.setText("Mass");
        GridBagConstraints gbc_mass = new GridBagConstraints();
        gbc_mass.fill = GridBagConstraints.HORIZONTAL;
        gbc_mass.insets = new Insets(0, 0, 5, 0);
        gbc_mass.gridx = 0;
        gbc_mass.gridy = ++gridY;
        controlBox.add(mass, gbc_mass);
        mass.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent f) {
                if (mass.getText().equalsIgnoreCase("Mass"))
                    mass.setText("");
            }

            public void focusLost(FocusEvent f) {
                if (mass.getText().equalsIgnoreCase(""))
                    mass.setText("Mass");
            }
        });
    }

    protected void buildXVelocityBox(JPanel controlBox)
    // X-Velocity text box
    {
        final TextField xvel = window.xvel;

        xvel.setForeground(Color.CYAN);
        xvel.setBackground(Color.DARK_GRAY);
        xvel.setFont(new Font("Consolas", Font.ITALIC, 12));
        xvel.setText("X-Velocity");
        GridBagConstraints gbc_xvel = new GridBagConstraints();
        gbc_xvel.fill = GridBagConstraints.HORIZONTAL;
        gbc_xvel.insets = new Insets(0, 0, 5, 0);
        gbc_xvel.gridx = 0;
        gbc_xvel.gridy = ++gridY;
        controlBox.add(xvel, gbc_xvel);
        xvel.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent f) {
                if (xvel.getText().equalsIgnoreCase("X-Velocity"))
                    xvel.setText("");
            }

            public void focusLost(FocusEvent f) {
                if (xvel.getText().equalsIgnoreCase(""))
                    xvel.setText("X-Velocity");
            }
        });
    }

    protected void buildYVelocityBox(JPanel controlBox)
    // Y-Velocity text box
    {
        final TextField yvel = window.yvel;

        yvel.setBackground(Color.DARK_GRAY);
        yvel.setForeground(Color.CYAN);
        yvel.setFont(new Font("Consolas", Font.ITALIC, 12));
        yvel.setText("Y-Velocity");
        GridBagConstraints gbc_yvel = new GridBagConstraints();
        gbc_yvel.insets = new Insets(0, 0, 5, 0);
        gbc_yvel.fill = GridBagConstraints.HORIZONTAL;
        gbc_yvel.gridx = 0;
        gbc_yvel.gridy = ++gridY;
        controlBox.add(yvel, gbc_yvel);
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
                    window.creationOfPlanet();
                }
            }
        });
        yvel.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent f) {
                if (yvel.getText().equalsIgnoreCase("Y-Velocity"))
                    yvel.setText("");
            }

            public void focusLost(FocusEvent f) {
                if (yvel.getText().equalsIgnoreCase(""))
                    yvel.setText("Y-Velocity");
            }
        });
    }

    protected void buildRestartButton(JPanel controlBox) {
        final JButton btnRestartSimulation = window.btnRestartSimulation;

        GridBagConstraints gbc_btnRestartSimulation = new GridBagConstraints();
        gbc_btnRestartSimulation.fill = GridBagConstraints.BOTH;
        gbc_btnRestartSimulation.insets = new Insets(0, 0, 5, 0);
        gbc_btnRestartSimulation.gridx = 0;
        gbc_btnRestartSimulation.gridy = ++gridY;
        btnRestartSimulation.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnRestartSimulation.setForeground(Color.BLUE);
        btnRestartSimulation.setBackground(Color.BLACK);
        btnRestartSimulation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                window.recallConfig();
                Simulation.getInstance().getHandle().repaint();
            }
        });
        controlBox.add(btnRestartSimulation, gbc_btnRestartSimulation);
    }

    protected void buildTextField(JPanel controlBox) {
        final JTextField textField = new JTextField();

        textField.setBackground(Color.GRAY);
        textField.setEditable(false);
        textField.setColumns(10);

        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.insets = new Insets(0, 0, 5, 0);
        gbc_textField.fill = GridBagConstraints.BOTH;
        gbc_textField.gridx = 0;
        gbc_textField.gridy = ++gridY;
        controlBox.add(textField, gbc_textField);
    }

    protected void buildSimulateButton(JPanel controlBox)
    // Simulate button
    {
        JButton btnSimulate = new JButton("Simulate");

        btnSimulate.setForeground(Color.BLUE);
        btnSimulate.setBackground(Color.BLACK);
        GridBagConstraints gbc_btnSimulate = new GridBagConstraints();
        gbc_btnSimulate.fill = GridBagConstraints.BOTH;
        gbc_btnSimulate.insets = new Insets(0, 0, 5, 0);
        gbc_btnSimulate.gridx = 0;
        gbc_btnSimulate.gridy = ++gridY;
        controlBox.add(btnSimulate, gbc_btnSimulate);
        btnSimulate.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent h) {
                        Simulation sim = Simulation.getInstance();
                        sim.setRunning(!sim.isRunning());
                        window.tabbedPane.setSelectedIndex(1);
                        // draw
                        sim.getHandle().repaint();
                    }
                }
        );
    }

    protected void buildTextField_1(JPanel controlBox) {
        final JTextField textField_1 = new JTextField();

        textField_1.setForeground(new Color(128, 128, 128));
        textField_1.setBackground(Color.GRAY);
        textField_1.setEditable(false);
        textField_1.setColumns(10);

        GridBagConstraints gbc_textField_1 = new GridBagConstraints();
        gbc_textField_1.insets = new Insets(0, 0, 5, 0);
        gbc_textField_1.fill = GridBagConstraints.BOTH;
        gbc_textField_1.gridx = 0;
        gbc_textField_1.gridy = ++gridY;
        controlBox.add(textField_1, gbc_textField_1);
/*
        controlBox.add(btnResetSimulation, gbc_btnResetSimulation);
*/
    }

    protected void buildResetButton(JPanel controlBox) {
        final JButton btnResetSimulation = window.btnResetSimulation;

        GridBagConstraints gbc_btnResetSimulation = new GridBagConstraints();
        gbc_btnResetSimulation.fill = GridBagConstraints.BOTH;
        gbc_btnResetSimulation.gridx = 0;
        gbc_btnResetSimulation.gridy = ++gridY;
        btnResetSimulation.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnResetSimulation.setForeground(Color.BLUE);
        btnResetSimulation.setBackground(Color.BLACK);
        btnResetSimulation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                window.crdb = new ConfirmReset();
            }
        });
        controlBox.add(btnResetSimulation, gbc_btnResetSimulation);
    }
}
