import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class RouteOptimizationGUI extends JFrame {
    private JTextArea deliveryListArea;
    private JComboBox<String> algorithmComboBox;
    private JTextField vehicleCapacityField;
    private JTextField maxDistanceField;
    private JTextArea resultArea;

    public RouteOptimizationGUI() {
        setTitle("Route Optimization for Delivery Service");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Delivery Points (Address, Priority):"));
        deliveryListArea = new JTextArea(10, 50);
        inputPanel.add(new JScrollPane(deliveryListArea));

        inputPanel.add(new JLabel("Select Optimization Algorithm:"));
        String[] algorithms = {"Nearest Neighbor", "Genetic Algorithm"};
        algorithmComboBox = new JComboBox<>(algorithms);
        inputPanel.add(algorithmComboBox);

        inputPanel.add(new JLabel("Vehicle Capacity:"));
        vehicleCapacityField = new JTextField();
        inputPanel.add(vehicleCapacityField);

        inputPanel.add(new JLabel("Maximum Driving Distance:"));
        maxDistanceField = new JTextField();
        inputPanel.add(maxDistanceField);

        JButton optimizeButton = new JButton("Optimize Route");
        optimizeButton.addActionListener(new OptimizeButtonListener());
        inputPanel.add(optimizeButton);

        add(inputPanel, BorderLayout.NORTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
    }

    private class OptimizeButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] deliveries = deliveryListArea.getText().split("\n");
            List<DeliveryPoint> deliveryPoints = new ArrayList<>();
            for (String delivery : deliveries) {
                String[] parts = delivery.split(",");
                if (parts.length == 2) {
                    String address = parts[0].trim();
                    int priority = Integer.parseInt(parts[1].trim());
                    deliveryPoints.add(new DeliveryPoint(address, priority));
                }
            }

            String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
            int vehicleCapacity = Integer.parseInt(vehicleCapacityField.getText().trim());
            int maxDistance = Integer.parseInt(maxDistanceField.getText().trim());

            String result = optimizeRoute(deliveryPoints, selectedAlgorithm, vehicleCapacity, maxDistance);
            resultArea.setText(result);
        }
    }

    private String optimizeRoute(List<DeliveryPoint> deliveryPoints, String algorithm, int capacity, int maxDistance) {
        return "Optimized Route:\n" + deliveryPoints.toString() + "\nUsing Algorithm: " + algorithm + "\nVehicle Capacity: " + capacity + "\nMax Distance: " + maxDistance;
    }

    private static class DeliveryPoint {
        String address;
        int priority;

        DeliveryPoint(String address, int priority) {
            this.address = address;
            this.priority = priority;
        }

        @Override
        public String toString() {
            return address + " (Priority: " + priority + ")";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RouteOptimizationGUI().setVisible(true);
        });
    }
}
