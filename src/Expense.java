import java.time.LocalDate;
import java.util.ArrayList;

public class Expense {
private int id;
private double amount;
private String category;
private String description;
private String modeOfPayment;
private LocalDate date;

public Expense(int id, double amount, String category, String description,String modeOfPayment, LocalDate date){
    this.id = id;
    this.amount =amount;
    this.category = category;
    this.description = description;
    this.modeOfPayment = modeOfPayment;
    this.date = date;
}
public int getId(){
    return id;
}

public double getAmount(){
    return amount;
}
public String getCategory() {
        return category;
    }
    public String getDescription() {
        return description;
    }
    public String getModeOfPayment() {
        return modeOfPayment;
    }
    public LocalDate getDate(){
    return date;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
public void displayExpense(){
    System.out.println("ID: "+ id);
    System.out.println("Amount: "+ amount);
    System.out.println("Category: "+ category);
    System.out.println("Description: "+ description);
    System.out.println("Mode of Payment: "+ modeOfPayment);
    System.out.println("Date (YYYY/MM/DD): "+ date);
    System.out.println("------------------------------------");
}
}