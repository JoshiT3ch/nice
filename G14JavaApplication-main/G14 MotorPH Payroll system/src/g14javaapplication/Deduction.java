package g14javaapplication;

public class Deduction {
    private String employeeId;
    private double tax;
    private double sss;
    private double philHealth;
    private double totalDeductions;

    public double calculateTotalDeductions() {
        totalDeductions = tax + sss + philHealth;
        return totalDeductions;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public void setSSS(double sss) {
        this.sss = sss;
    }

    public void setPhilHealth(double philHealth) {
        this.philHealth = philHealth;
    }

    public double getTax() {
        return tax;
    }

    public double getSSS() {
        return sss;
    }

    public double getPhilHealth() {
        return philHealth;
    }

    public double getTotalDeductions() {
        return totalDeductions;
    }

    public String getEmployeeID() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
