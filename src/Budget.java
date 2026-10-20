public class Budget {
    private double monthlyLimit;

    public Budget(double monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }

    public double getMonthlyLimit() {
        return monthlyLimit;
    }

    public void setMonthlyLimit(double monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }

    public void display() {
        System.out.println("Monthly Budget: ₹" + monthlyLimit);
    }

    public double remainingMonthlyBudget(double totalSpent) {
        return monthlyLimit - totalSpent;
    }
}