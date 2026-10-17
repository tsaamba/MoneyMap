public class Budget {
    private int monthlyLimit;
  public  Budget(int monthlyLimit){
        this.monthlyLimit = monthlyLimit;
    }
    public void display(){
        System.out.println("Monthly Limit : "+ monthlyLimit);
    }
    public double remainingMonthlyBudget(double totalSpent){
      return  monthlyLimit - totalSpent;

    }
}
