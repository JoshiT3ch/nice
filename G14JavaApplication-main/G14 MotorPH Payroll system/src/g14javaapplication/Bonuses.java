package g14javaapplication;

public class Bonuses {
    private double thirteenMonthPay;
    private double performanceBonus;
    private double incentiveBonus;
    private double loyaltyBonus;

    public double getTotalBonuses() {
        return thirteenMonthPay + performanceBonus + incentiveBonus + loyaltyBonus;
    }

    public double getThirteenMonthPay() {
        return thirteenMonthPay;
    }

    public void setThirteenMonthPay(double thirteenMonthPay) {
        this.thirteenMonthPay = thirteenMonthPay;
    }

    public double getIncentiveBonus() {
        return incentiveBonus;
    }

    public void setIncentiveBonus(double incentiveBonus) {
        this.incentiveBonus = incentiveBonus;
    }

    public double getPerformanceBonus() {
        return performanceBonus;
    }

    public void setPerformanceBonus(double performanceBonus) {
        this.performanceBonus = performanceBonus;
    }

    public double getLoyaltyBonus() {
        return loyaltyBonus;
    }

    public void setLoyaltyBonus(double loyaltyBonus) {
        this.loyaltyBonus = loyaltyBonus;
    }
}
