package g14javaapplication;

import com.opencsv.CSVWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;

public class NewEmployeeForm extends JFrame {
    private JTextField[] fields = new JTextField[19];
    private String[] labels = {
        "Employee ID", "Last Name", "First Name", "Birthday", "Address", "Phone Number",
        "SSS #", "PhilHealth #", "TIN #", "Pag-ibig #", "Status", "Position", "Immediate Supervisor",
        "Basic Salary", "Rice Subsidy", "Phone Allowance", "Clothing Allowance", "Gross Semi-monthly Rate", "Hourly Rate"
    };

    public NewEmployeeForm(EmployeeTableFrame tableFrameRef) {
        setTitle("New Employee");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(labels.length + 1, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (int i = 0; i < labels.length; i++) {
            panel.add(new JLabel(labels[i] + ":"));
            fields[i] = new JTextField();
            panel.add(fields[i]);
        }

        JButton saveButton = new JButton("Save");
        panel.add(saveButton);

        saveButton.addActionListener(e -> {
            if (saveToCSV()) {
                JOptionPane.showMessageDialog(this, "Employee added successfully!");
                tableFrameRef.refreshTable(); // Refresh JTable in EmployeeTableFrame
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save employee. Please check inputs.");
            }
        });

        add(panel);
    }

    private boolean saveToCSV() {
        try (CSVWriter writer = new CSVWriter(new FileWriter("employees.csv", true))) {
            String[] newRow = new String[labels.length];
            for (int i = 0; i < fields.length; i++) {
                newRow[i] = fields[i].getText().trim();
                if (newRow[i].isEmpty()) return false; // basic validation
            }
            writer.writeNext(newRow);
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
