package g14javaapplication;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public class EditLogViewer extends JFrame {
    private JTable logTable;
    private DefaultTableModel logModel;
    private static final String[] columns = {"Timestamp", "Employee ID", "Field", "Old Value", "New Value"};
    private static final File logFile = new File("edit_log.csv");

    public EditLogViewer() {
        setTitle("Edit Log Viewer");
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        logModel = new DefaultTableModel(columns, 0);
        logTable = new JTable(logModel);
        JScrollPane scrollPane = new JScrollPane(logTable);

        JButton clearButton = new JButton("Clear Logs");
        clearButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        clearButton.addActionListener(e -> clearLogs());

        // ✅ Call loadLogs safely
        try {
            loadLogs();
        } catch (CsvValidationException e) {
            JOptionPane.showMessageDialog(this, "Invalid CSV format: " + e.getMessage());
            e.printStackTrace();
        }

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(clearButton);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadLogs() throws CsvValidationException {
        logModel.setRowCount(0);
        if (!logFile.exists()) return;

        try (CSVReader reader = new CSVReader(new FileReader(logFile))) {
            String[] row;
            while ((row = reader.readNext()) != null) {
                if (row.length == 5) {
                    logModel.addRow(row);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading edit_log.csv: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearLogs() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear all logs?", "Confirm Clear", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (CSVWriter writer = new CSVWriter(new FileWriter(logFile))) {
                logModel.setRowCount(0); // Clear table
                JOptionPane.showMessageDialog(this, "Logs cleared successfully.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error clearing logs: " + e.getMessage());
            }
        }
    }
}