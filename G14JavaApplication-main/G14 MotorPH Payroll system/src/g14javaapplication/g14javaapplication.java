package g14javaapplication;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class g14javaapplication {
	public static void main(String[] args) {
		new g14javaapplication();
	}

    JFrame frame;

    public g14javaapplication() {
        frame = new JFrame("Payroll System"); //creates a frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exit out of application
        frame.setResizable(true); //making the frame resizable
        frame.setSize(420, 420); //to set the x-dimension, and y-dimension of frame
        frame.setVisible(true); //make frame Visible
        frame.setLocationRelativeTo(null);
        
        ImageIcon image = new ImageIcon(getClass().getResource("MMDC Logo.png")); //create an ImageIcon
        frame.setIconImage(image.getImage()); //change icon of frame
        frame.getContentPane().setBackground(new Color(193,201,219)); //change color of Background

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Welcome to Payroll System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel userLabel = new JLabel("User :");
        JTextField userField = new JTextField();

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();

        JLabel roleLabel = new JLabel("Role:");
        JTextField roleField = new JTextField();

        JLabel empIdLabel = new JLabel("Employee ID:");
        JTextField empIdField = new JTextField();

        formPanel.add(userLabel);
        formPanel.add(userField);
        formPanel.add(passLabel);
        formPanel.add(passField);
        formPanel.add(roleLabel);
        formPanel.add(roleField);
        formPanel.add(empIdLabel);
        formPanel.add(empIdField);

        JButton enterButton = new JButton("Enter");
        enterButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                showDashboard(empIdField.getText());
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(enterButton);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void showDashboard(String employeeId) {
    	JButton viewAllEmpButton = new JButton("View All Employees");
    	viewAllEmpButton.setFont(new Font("SansSerif", Font.BOLD, 16));
    	viewAllEmpButton.addActionListener(e -> new EmployeeTableFrame(null).setVisible(true));
        JFrame dashFrame = new JFrame("Dashboard");
        dashFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashFrame.setSize(400, 300);
        dashFrame.setLocationRelativeTo(null);

        JPanel dashPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        dashPanel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));
        dashPanel.add(viewAllEmpButton);

        JButton empButton = new JButton("Employee Info");
        JButton attButton = new JButton("Attendance");
        JButton payrollButton = new JButton("Payroll");
        JButton bonusesButton = new JButton("Bonuses");
        JButton allEmpButton = new JButton("All Employees");
        allEmpButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        allEmpButton.addActionListener(e -> showAllEmployeesTable(employeeId));
        
        empButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        attButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        payrollButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        bonusesButton.setFont(new Font("SansSerif", Font.BOLD, 16));

        empButton.addActionListener(e -> showEmployeeInfo(employeeId));
        attButton.addActionListener(e -> showAttendanceForm(employeeId));
        payrollButton.addActionListener(e -> showPayrollForm(employeeId));
        bonusesButton.addActionListener(e -> showBonusesForm(employeeId));

        dashPanel.add(empButton);
        dashPanel.add(attButton);
        dashPanel.add(payrollButton);
        dashPanel.add(bonusesButton);
        dashPanel.add(allEmpButton); // Add this

        dashFrame.add(dashPanel);
        dashFrame.setVisible(true);
    }

    private void showEmployeeInfo(String employeeId) {
        // Logic to display employee information
        // This would typically involve fetching employee data based on employeeId
        JOptionPane.showMessageDialog(frame, "Displaying info for Employee ID: " + employeeId);
    }

    private void showAttendanceForm(String employeeId) {
        // Logic to show attendance form
        JFrame attendanceFrame = new JFrame("Attendance Form");
        attendanceFrame.setSize(300, 200);
        attendanceFrame.setLocationRelativeTo(null);
        attendanceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField logIdField = new JTextField();
        JTextField timestampField = new JTextField();
        JTextField activityField = new JTextField();

        panel.add(new JLabel("Log ID:"));
        panel.add(logIdField);
        panel.add(new JLabel("Timestamp:"));
        panel.add(timestampField);
        panel.add(new JLabel("Activity:"));
        panel.add(activityField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
        	//Input Validation and Exception Handling for 
        	try {
        		int logId = Integer.parseInt (logIdField.getText());
        		String timestamp = timestampField.getText();
        		String activity = activityField.getText();
        		
        		if (timestamp.isEmpty() || activity.isEmpty()) {
        			throw new IllegalArgumentException("Timestamp and Activity cannot be empty.");
        		}
        	
            // Create Attendance object and handle logic
            Attendance attendance = new Attendance();
            attendance.setLogId(Integer.parseInt(logIdField.getText()));
            attendance.setTimestamp(timestampField.getText());
            attendance.setActivity(activityField.getText());
            JOptionPane.showMessageDialog(attendanceFrame, "Attendance logged for Employee ID: " + employeeId);
            attendanceFrame.dispose();
        } catch (NumberFormatException ex) {
        	JOptionPane.showMessageDialog(attendanceFrame,  "Please enter a valid Log ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
        	JOptionPane.showMessageDialog(attendanceFrame, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
        
    });

        panel.add(submitButton);
        attendanceFrame.add(panel);
        attendanceFrame.setVisible(true);
    }

    private void showPayrollForm(String employeeId) {
        // Logic to show payroll form
        JFrame payrollFrame = new JFrame("Payroll Form");
        payrollFrame.setSize(300, 300);
        payrollFrame.setLocationRelativeTo(null);
        payrollFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 2));
        JTextField baseSalaryField = new JTextField();
        JTextField allowanceField = new JTextField();
        JTextField overtimeHoursField = new JTextField();
        JTextField overtimeRateField = new JTextField();

        panel.add(new JLabel("Base Salary:"));
        panel.add(baseSalaryField);
        panel.add(new JLabel("Allowance:"));
        panel.add(allowanceField);
        panel.add(new JLabel("Overtime Hours:"));
        panel.add(overtimeHoursField);
        panel.add(new JLabel("Overtime Rate:"));
        panel.add(overtimeRateField);

        JButton submitButton = new JButton("Calculate Payroll");
        submitButton.addActionListener(e -> {
            // Create Payroll object and handle logic
            Payroll payroll = new Payroll();
            payroll.setBaseSalary(Double.parseDouble(baseSalaryField.getText()));
            payroll.setAllowance(Double.parseDouble(allowanceField.getText()));
            payroll.setOvertimeHours(Double.parseDouble(overtimeHoursField.getText()));
            payroll.setOvertimeRate(Double.parseDouble(overtimeRateField.getText()));
            double grossPay = payroll.calculateGrossPay();
            JOptionPane.showMessageDialog(payrollFrame, "Gross Pay for Employee ID: " + employeeId + " is: " + grossPay);
            payrollFrame.dispose();
        });

        panel.add(submitButton);
        payrollFrame.add(panel);
        payrollFrame.setVisible(true);
    }

    private void showBonusesForm(String employeeId) {
        // Logic to show bonuses form
        JFrame bonusesFrame = new JFrame("Bonuses Form");
        bonusesFrame.setSize(300, 300);
        bonusesFrame.setLocationRelativeTo(null);
        bonusesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 2));
        JTextField thirteenMonthPayField = new JTextField();
        JTextField performanceBonusField = new JTextField();
        JTextField incentiveBonusField = new JTextField();
        JTextField loyaltyBonusField = new JTextField();

        panel.add(new JLabel("13th Month Pay:"));
        panel.add(thirteenMonthPayField);
        panel.add(new JLabel("Performance Bonus:"));
        panel.add(performanceBonusField);
        panel.add(new JLabel("Incentive Bonus:"));
        panel.add(incentiveBonusField);
        panel.add(new JLabel("Loyalty Bonus:"));
        panel.add(loyaltyBonusField);

        JButton submitButton = new JButton("Calculate Total Bonuses");
        submitButton.addActionListener(e -> {
            // Create Bonuses object and handle logic
            Bonuses bonuses = new Bonuses();
            bonuses.setThirteenMonthPay(Double.parseDouble(thirteenMonthPayField.getText()));
            bonuses.setPerformanceBonus(Double.parseDouble(performanceBonusField.getText()));
            bonuses.setIncentiveBonus(Double.parseDouble(incentiveBonusField.getText()));
            bonuses.setLoyaltyBonus(Double.parseDouble(loyaltyBonusField.getText()));
            double totalBonuses = bonuses.getTotalBonuses();
            JOptionPane.showMessageDialog(bonusesFrame, "Total Bonuses for Employee ID: " + employeeId + " is: " + totalBonuses);
            bonusesFrame.dispose();
        });

        panel.add(submitButton);
        bonusesFrame.add(panel);
        bonusesFrame.setVisible(true);
    }
    private void showAllEmployeesTable(String employeeId) {
        new EmployeeTableFrame(employeeId).setVisible(true);
    }
}


