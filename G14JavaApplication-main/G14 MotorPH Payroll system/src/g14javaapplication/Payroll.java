package g14javaapplication;

public class Payroll {
    private Employee employee;
    private double baseSalary;
    private double allowance;
    private double overtimeRate;
    private double overtimeHours;
    private double grossPay;
    private double netPay;

    public double calculateOvertimePay() {
        return overtimeHours * overtimeRate;
    }

    public double calculateGrossPay() {
        grossPay = baseSalary + allowance + calculateOvertimePay();
        return grossPay;
    }

    public double calculateNetPay() {
        // Assuming deductions are handled elsewhere
        netPay = grossPay; // Placeholder for actual net pay calculation
        return netPay;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public double getAllowance() {
        return allowance;
    }

    public void setAllowance(double allowance) {
        this.allowance = allowance;
    }

    public double getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public double getOvertimeRate() {
        return overtimeRate;
    }

    public void setOvertimeRate(double overtimeRate) {
        this.overtimeRate = overtimeRate;
    }
}
