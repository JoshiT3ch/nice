package g14javaapplication;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class EmployeeTableFrame extends JFrame {
    private JTable employeeTable;
    private DefaultTableModel tableModel;

    public EmployeeTableFrame(String filterEmployeeId) {
        setTitle("All Employee Records");
        setSize(1000, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

     // === MENU BAR PARA SA EDIT LOG VIEWER ===
        JMenuBar menuBar = new JMenuBar();
        JMenu logMenu = new JMenu("Logs");

        JMenuItem viewEditLogMenuItem = new JMenuItem("View Edit Log");
        viewEditLogMenuItem.addActionListener(e -> new EditLogViewer().setVisible(true));

        JMenuItem viewDeletedLogMenuItem = new JMenuItem("View Deleted Log");
        viewDeletedLogMenuItem.addActionListener(e -> viewDeletedLog());

        logMenu.add(viewEditLogMenuItem);
        logMenu.add(viewDeletedLogMenuItem);

        menuBar.add(logMenu);
        setJMenuBar(menuBar);
        
        String[] columnNames = {
            "EmployeeID", "Last Name", "First Name", "Birthday", "Address", "Phone Number",
            "SSS #", "Philhealth #", "TIN #", "Pag-ibig #", "Status", "Position", "Immediate Supervisor",
            "Basic Salary", "Rice Subsidy", "Phone Allowance", "Clothing Allowance", "Gross Semi-monthly Rate", "Hourly Rate"
        };

        tableModel = new DefaultTableModel(columnNames, 0);
        employeeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(employeeTable);

        JButton viewButton = new JButton("View Employee");
        viewButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        viewButton.addActionListener(e -> viewSelectedEmployee());

        JButton addButton = new JButton("New Employee");
        addButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        addButton.addActionListener(e -> new NewEmployeeForm(this).setVisible(true));

        JButton editButton = new JButton("Edit Employee");
        editButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        editButton.addActionListener(e -> editSelectedEmployee());

        JButton deleteButton = new JButton("Delete Employee");
        deleteButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        deleteButton.addActionListener(e -> deleteSelectedEmployee());

        JButton restoreButton = new JButton("Restore Backup");
        restoreButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        restoreButton.addActionListener(e -> restoreFromBackup());

        JButton viewLogButton = new JButton("View Deleted Log");
        viewLogButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        viewLogButton.addActionListener(e -> viewDeletedLog());

        JButton logButton = new JButton("View Edit Logs");
        logButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        logButton.addActionListener(e -> new EditLogViewer().setVisible(true));

        JButton viewEditLogButton = new JButton("View Edit Log");
        viewEditLogButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        viewEditLogButton.addActionListener(e -> {
            new EditLogViewer().setVisible(true);
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewButton);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(restoreButton);
        buttonPanel.add(viewLogButton);
        buttonPanel.add(logButton);
        buttonPanel.add(viewEditLogButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadEmployeeData(filterEmployeeId);
    }

    private void loadEmployeeData(String filterEmployeeId) {
        try (CSVReader reader = new CSVReader(new FileReader("employees.csv"))) {
            tableModel.setRowCount(0);
            String[] line;
            boolean isHeader = true;

            while ((line = reader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                if (filterEmployeeId == null || filterEmployeeId.isEmpty() || line[0].equals(filterEmployeeId)) {
                    tableModel.addRow(line);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error reading employees.csv: " + e.getMessage());
        }
    }

    private void viewSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee from the table.");
            return;
        }

        String[] employeeData = new String[tableModel.getColumnCount()];
        for (int i = 0; i < employeeData.length; i++) {
            employeeData[i] = tableModel.getValueAt(selectedRow, i).toString();
        }

        new EmployeeDetailsFrame(employeeData).setVisible(true);
    }

    private void editSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee from the table.");
            return;
        }

        String[] employeeData = new String[tableModel.getColumnCount()];
        for (int i = 0; i < employeeData.length; i++) {
            employeeData[i] = tableModel.getValueAt(selectedRow, i).toString();
        }

        new EditEmployeeForm(employeeData, this).setVisible(true);
    }

    private void deleteSelectedEmployee() {
        int[] selectedRows = employeeTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one employee to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + selectedRows.length + " record(s)?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            // Backup first
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            Files.copy(Paths.get("employees.csv"), Paths.get("employees_backup_" + timestamp + ".csv"), StandardCopyOption.REPLACE_EXISTING);

            // Prepare new temp CSV
            File originalFile = new File("employees.csv");
            File tempFile = new File("employees_temp.csv");

            CSVReader reader = new CSVReader(new FileReader(originalFile));
            CSVWriter writer = new CSVWriter(new FileWriter(tempFile));
            BufferedWriter logWriter = new BufferedWriter(new FileWriter("deleted_log.txt", true));

            String[] nextLine;
            boolean isHeader = true;
            Set<String> selectedIds = new HashSet<>();

            for (int row : selectedRows) {
                selectedIds.add(tableModel.getValueAt(row, 0).toString());
            }

            while ((nextLine = reader.readNext()) != null) {
                if (isHeader) {
                    writer.writeNext(nextLine);
                    isHeader = false;
                    continue;
                }

                if (selectedIds.contains(nextLine[0])) {
                    logWriter.write("Deleted: " + Arrays.toString(nextLine) + " at " + new Date() + "\n");
                    continue;
                }
                writer.writeNext(nextLine);
            }

            reader.close();
            writer.close();
            logWriter.close();

            if (!originalFile.delete() || !tempFile.renameTo(originalFile)) {
                JOptionPane.showMessageDialog(this, "Failed to finalize deletion. Temp file couldn't replace original.");
                return;
            }

            JOptionPane.showMessageDialog(this, "Successfully deleted selected record(s).");
            refreshTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error during deletion: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void restoreFromBackup() {
        File dir = new File(".");
        File[] backupFiles = dir.listFiles((d, name) -> name.startsWith("employees_backup_") && name.endsWith(".csv"));

        if (backupFiles == null || backupFiles.length == 0) {
            JOptionPane.showMessageDialog(this, "No backup files found.");
            return;
        }

        Arrays.sort(backupFiles, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));
        File latestBackup = backupFiles[0];

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Restore data from the latest backup?\n" + latestBackup.getName(),
            "Confirm Restore",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        File originalFile = new File("employees.csv");
        try {
            Files.copy(latestBackup.toPath(), originalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            JOptionPane.showMessageDialog(this, "Restore successful from " + latestBackup.getName());
            refreshTable();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to restore: " + e.getMessage());
        }
    }

    private void viewDeletedLog() {
        File logFile = new File("deleted_log.txt");
        if (!logFile.exists()) {
            JOptionPane.showMessageDialog(this, "No deleted log found.");
            return;
        }

        try {
            JTextArea textArea = new JTextArea();
            textArea.read(new FileReader(logFile), null);
            textArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(800, 400));

            JOptionPane.showMessageDialog(this, scrollPane, "Deleted Employee Log", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to read deleted log: " + e.getMessage());
        }
    }

    public void refreshTable() {
        int selectedRow = employeeTable.getSelectedRow();
        String selectedEmployeeId = null;

        if (selectedRow != -1) {
            selectedEmployeeId = tableModel.getValueAt(selectedRow, 0).toString();
        }

        tableModel.setRowCount(0);
        loadEmployeeData(null);

        if (selectedEmployeeId != null) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).toString().equals(selectedEmployeeId)) {
                    employeeTable.setRowSelectionInterval(i, i);
                    break;
                }
            }
        }
    }
}