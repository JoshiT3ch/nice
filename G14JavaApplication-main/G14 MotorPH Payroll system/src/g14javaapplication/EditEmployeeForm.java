package g14javaapplication;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class EditEmployeeForm extends JFrame {
    private JTextField[] fields = new JTextField[19];
    private String[] labels = {
        "Employee ID", "Last Name", "First Name", "Birthday", "Address", "Phone Number",
        "SSS #", "PhilHealth #", "TIN #", "Pag-ibig #", "Status", "Position", "Immediate Supervisor",
        "Basic Salary", "Rice Subsidy", "Phone Allowance", "Clothing Allowance", "Gross Semi-monthly Rate", "Hourly Rate"
    };

    private String originalEmployeeId;
    private String[] originalData;
    private EmployeeTableFrame tableRef;

    private final File employeesFile = new File("employees.csv");
    private final File tempFile = new File("employees_temp.csv");
    private final File editLogFile = new File("edit_log.csv");

    public EditEmployeeForm(String[] employeeData, EmployeeTableFrame tableRef) {
        this.originalEmployeeId = employeeData[0];
        this.originalData = employeeData.clone();
        this.tableRef = tableRef;

        setTitle("Edit Employee");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(labels.length + 2, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (int i = 0; i < labels.length; i++) {
            panel.add(new JLabel(labels[i] + ":"));
            fields[i] = new JTextField(employeeData[i]);
            panel.add(fields[i]);
        }

        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> {
            if (updateEmployeeCSV()) {
                JOptionPane.showMessageDialog(this, "Employee updated successfully!");
                tableRef.refreshTable();
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update employee.");
            }
        });

        JButton undoButton = new JButton("Undo Last Edit");
        undoButton.addActionListener(e -> {
            if (undoLastEdit()) {
                JOptionPane.showMessageDialog(this, "Last edit undone successfully!");
                tableRef.refreshTable();
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Undo failed. No valid log entry found.");
            }
        });

        panel.add(saveButton);
        panel.add(undoButton);
        add(panel);
    }

    private boolean updateEmployeeCSV() {
        try (
            CSVReader reader = new CSVReader(new FileReader(employeesFile));
            CSVWriter writer = new CSVWriter(new FileWriter(tempFile))
        ) {
            String[] nextLine;
            boolean isHeader = true;
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            while ((nextLine = reader.readNext()) != null) {
                if (isHeader) {
                    writer.writeNext(nextLine);
                    isHeader = false;
                    continue;
                }

                if (nextLine[0].equals(originalEmployeeId)) {
                    String[] updated = new String[fields.length];
                    for (int i = 0; i < fields.length; i++) {
                        updated[i] = fields[i].getText().trim();
                        if (updated[i].isEmpty()) return false;
                    }

                    writer.writeNext(updated);

                    try (CSVWriter logWriter = new CSVWriter(new FileWriter(editLogFile, true))) {
                        for (int i = 0; i < updated.length; i++) {
                            if (!nextLine[i].equals(updated[i])) {
                                String[] logRow = {
                                    timestamp,
                                    originalEmployeeId,
                                    labels[i],
                                    nextLine[i],
                                    updated[i]
                                };
                                logWriter.writeNext(logRow);
                            }
                        }
                    }

                } else {
                    writer.writeNext(nextLine);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (!employeesFile.delete()) return false;
        if (!tempFile.renameTo(employeesFile)) return false;

        return true;
    }

    private boolean undoLastEdit() {
        if (!editLogFile.exists()) return false;

        Map<String, String[]> lastEditMap = new HashMap<>();

        try (CSVReader reader = new CSVReader(new FileReader(editLogFile))) {
            String[] row;
            while ((row = reader.readNext()) != null) {
                if (row.length == 5) {
                    String empId = row[1];
                    String field = row[2];
                    String oldVal = row[3];

                    int index = Arrays.asList(labels).indexOf(field);
                    if (!lastEditMap.containsKey(empId)) {
                        lastEditMap.put(empId, new String[labels.length]);
                    }

                    lastEditMap.get(empId)[index] = oldVal;
                }
            }

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
            return false;
        }

        if (!lastEditMap.containsKey(originalEmployeeId)) return false;

        String[] revertedData = originalData.clone();
        String[] overrideData = lastEditMap.get(originalEmployeeId);

        for (int i = 0; i < overrideData.length; i++) {
            if (overrideData[i] != null) {
                revertedData[i] = overrideData[i];
            }
        }

        try (
            CSVReader reader = new CSVReader(new FileReader(employeesFile));
            CSVWriter writer = new CSVWriter(new FileWriter(tempFile))
        ) {
            String[] line;
            boolean isHeader = true;

            while ((line = reader.readNext()) != null) {
                if (isHeader) {
                    writer.writeNext(line);
                    isHeader = false;
                    continue;
                }

                if (line[0].equals(originalEmployeeId)) {
                    writer.writeNext(revertedData);
                } else {
                    writer.writeNext(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (!employeesFile.delete()) return false;
        return tempFile.renameTo(employeesFile);
    }
}