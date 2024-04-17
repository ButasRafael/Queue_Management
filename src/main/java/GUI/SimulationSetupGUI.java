package GUI;

import Logic.SelectionPolicy;
import Logic.SimulationManager;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import java.awt.*;

public class SimulationSetupGUI extends JFrame {
    private final JTextField timeLimitField;
    private final JTextField numServersField;
    private final JTextField numClientsField;
    private final JTextField minArrivalTimeField;
    private final JTextField maxArrivalTimeField;
    private final JTextField minProcessingTimeField;
    private final JTextField maxProcessingTimeField;
    private final JComboBox<SelectionPolicy> selectionPolicyComboBox;

    public SimulationSetupGUI() {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        setTitle("Simulation Setup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 2));
        getContentPane().setBackground(Color.WHITE);

        addFieldWithLabel("Time Limit:", timeLimitField = new JTextField());
        addFieldWithLabel("Number of Servers:", numServersField = new JTextField());
        addFieldWithLabel("Number of Clients:", numClientsField = new JTextField());
        addFieldWithLabel("Min Arrival Time:", minArrivalTimeField = new JTextField());
        addFieldWithLabel("Max Arrival Time:", maxArrivalTimeField = new JTextField());
        addFieldWithLabel("Min Processing Time:", minProcessingTimeField = new JTextField());
        addFieldWithLabel("Max Processing Time:", maxProcessingTimeField = new JTextField());

        addFieldWithLabel("Selection Policy:", selectionPolicyComboBox = new JComboBox<>(SelectionPolicy.values()));

        JButton startButton = new JButton("Start Simulation");
        startButton.setBackground(new Color(30, 144, 255));
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(e -> startSimulation());
        add(startButton);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addFieldWithLabel(String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setForeground(new Color(30, 144, 255));
        add(label);
        add(field);
    }

    private void startSimulation() {
        if (isEmptyField(timeLimitField) || isEmptyField(numServersField) || isEmptyField(numClientsField) ||
                isEmptyField(minArrivalTimeField) || isEmptyField(maxArrivalTimeField) ||
                isEmptyField(minProcessingTimeField) || isEmptyField(maxProcessingTimeField)) {
            JOptionPane.showMessageDialog(this, "Missing data!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int timeLimit = Integer.parseInt(timeLimitField.getText());
        int numServers = Integer.parseInt(numServersField.getText());
        int numClients = Integer.parseInt(numClientsField.getText());
        int minArrivalTime = Integer.parseInt(minArrivalTimeField.getText());
        int maxArrivalTime = Integer.parseInt(maxArrivalTimeField.getText());
        int minProcessingTime = Integer.parseInt(minProcessingTimeField.getText());
        int maxProcessingTime = Integer.parseInt(maxProcessingTimeField.getText());
        SelectionPolicy selectionPolicy = (SelectionPolicy) selectionPolicyComboBox.getSelectedItem();

        SimulationManager simulationManager = new SimulationManager(
                timeLimit, numServers, numClients,
                minArrivalTime, maxArrivalTime,
                maxProcessingTime, minProcessingTime,
                selectionPolicy
        );
        Thread simulationThread = new Thread(simulationManager);
        simulationThread.start();
        dispose();
    }

    private boolean isEmptyField(JTextField field) {
        return field.getText().trim().isEmpty();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimulationSetupGUI::new);
    }
}
