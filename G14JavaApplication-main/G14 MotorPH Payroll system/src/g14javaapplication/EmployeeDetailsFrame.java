package g14javaapplication;

import javax.swing.*;
import java.awt.*;

public class EmployeeDetailsFrame extends JFrame {
    private JTextArea salaryArea;

    public EmployeeDetailsFrame(String[] employeeData) {
        setTitle("Employee Details");
        setSize(500, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        String[] labels = {
            "Employee ID", "Last Name", "First Name", "Birthday", "Address", "Phone Number",
            "SSS #", "PhilHealth #", "TIN #", "Pag-IBIG #", "Status", "Position", "Immediate Supervisor",
            "Basic Salary", "Rice Subsidy", "Phone Allowance", "Clothing Allowance", "Gross Semi-monthly Rate", "Hourly Rate"
        };

        for (int i = 0; i < employeeData.length; i++) {
            JLabel label = new JLabel(labels[i] + ": " + employeeData[i]);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(label);
        }

        panel.add(Box.createRigidArea(new Dimension(0, 10))); // spacing

        panel.add(new JLabel("Select Month for Salary Computation:"));
        String[] months = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        };
        JComboBox<String> monthComboBox = new JComboBox<>(months);
        monthComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(monthComboBox);

        JButton computeButton = new JButton("Compute");
        computeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(computeButton);

        salaryArea = new JTextArea(6, 30);
        salaryArea.setLineWrap(true);
        salaryArea.setWrapStyleWord(true);
        salaryArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(salaryArea);
        scrollPane.setPreferredSize(new Dimension(450, 150));
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(scrollPane);

        computeButton.addActionListener(e -> {
            computeSalary(employeeData, (String) monthComboBox.getSelectedItem());
        });

        add(panel);
    }

    private void computeSalary(String[] employeeData, String selectedMonth) {
        try {
            double basic = parseCurrency(employeeData[13]);
            double rice = parseCurrency(employeeData[14]);
            double phone = parseCurrency(employeeData[15]);
            double clothing = parseCurrency(employeeData[16]);

            double total = basic + rice + phone + clothing;

            StringBuilder result = new StringBuilder();
            result.append("Month Selected: ").append(selectedMonth).append("\n");
            result.append("------------------------------------\n");
            result.append(String.format("Basic Salary        : ₱%.2f\n", basic));
            result.append(String.format("Rice Subsidy        : ₱%.2f\n", rice));
            result.append(String.format("Phone Allowance     : ₱%.2f\n", phone));
            result.append(String.format("Clothing Allowance  : ₱%.2f\n", clothing));
            result.append("------------------------------------\n");
            result.append(String.format("Total Monthly Salary: ₱%.2f", total));

            salaryArea.setText(result.toString());

        } catch (NumberFormatException ex) {
            salaryArea.setText("⚠️ Error computing salary. Invalid number format in CSV.");
        } catch (ArrayIndexOutOfBoundsException ex) {
            salaryArea.setText("⚠️ Error: Missing data fields in employee record.");
        }
    }

    private double parseCurrency(String value) throws NumberFormatException {
        if (value == null || value.isEmpty()) return 0.0;
        return Double.parseDouble(value.replace(",", "").replace("\"", "").trim());
    }
}
