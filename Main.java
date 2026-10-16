import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        Budget b1 = new Budget(3000);
        ArrayList<Expense> expenses = new ArrayList<>();
        int nextId =1;
        int choice;
        do {
            System.out.println("===== Expense Tracker =====");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. View Total Spending");
            System.out.println("4. Check Remaining Budget");
            System.out.println("5. Edit Expense");
            System.out.println("6. Delete Expense");
            System.out.println("7. Search Expenses by Category");
            System.out.println("8. Spending by Category");
            System.out.println("9. Exit");

            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number from 1 to 9.");
                sc.nextLine();
                System.out.print("Enter your choice: ");
            }

            choice = sc.nextInt();

            switch(choice){
                case 1: {
                    double amount;

                    while (true) {
                        System.out.print("Enter amount: ");

                        if (sc.hasNextDouble()) {
                            amount = sc.nextDouble();
                            sc.nextLine();

                            if (amount > 0) {
                                break;
                            } else {
                                System.out.println("Amount must be greater than zero.");
                            }
                        } else {
                            System.out.println("Invalid input. Please enter a number.");
                            sc.nextLine();
                        }
                    }

                    String category;

                    while (true) {
                        System.out.print("Enter category: ");
                        category = sc.nextLine().trim();

                        if (!category.isEmpty()) {
                            break;
                        }

                        System.out.println("Category cannot be empty.");
                    }

                    System.out.print("Enter description: ");
                    String description = sc.nextLine();

                    System.out.print("Enter payment method: ");
                    String modeOfPayment = sc.nextLine();

                    LocalDate date;

                    while (true) {
                        try {
                            System.out.print("Enter date (YYYY-MM-DD): ");
                            date = LocalDate.parse(sc.nextLine());
                            break;
                        } catch (java.time.format.DateTimeParseException e) {
                            System.out.println("Invalid date. Please use YYYY-MM-DD.");
                        }
                    }
                    Expense expense = new Expense(
                            nextId++,
                            amount,
                            category,
                            description,
                            modeOfPayment,
                            date
                    );
                    expenses.add(expense);
                    System.out.println("Expense added successfully");
                    break;
            }
                case 2: {
                    if(expenses.isEmpty() ){
                        System.out.println("No expense record found");
                    }
                    else{
                        System.out.println("-------All Expenses---------");
                        for(Expense  expense :expenses){
                            expense.displayExpense();
                        }

                    }
                    break;
                }
                case 3:{
                    double totalAmount = 0;

                    for(Expense expense: expenses){
                        totalAmount += expense.getAmount();
                    }
                    System.out.println("total Spent: "+ totalAmount);
                    break;
                }
                case 4 :{
                    double totalSpent = 0;

                    for (Expense expense : expenses) {
                        totalSpent += expense.getAmount();
                    }
                    double remaining = b1.remainingMonthlyBudget(totalSpent);
                    b1.display();
                    System.out.println("total Spent: "+ totalSpent);
                    System.out.println("remaining budget: "+ remaining );
                    break;


                }
                case 5: {
                    System.out.print("Enter expense ID to edit: ");
                    int searchId = sc.nextInt();
                    sc.nextLine();

                    Expense foundExpense = null;

                    for (Expense expense : expenses) {
                        if (expense.getId() == searchId) {
                            foundExpense = expense;
                            break;
                        }
                    }

                    if (foundExpense == null) {
                        System.out.println("Expense not found!");
                    } else {
                        System.out.println("Current expense details:");
                        foundExpense.displayExpense();

                        System.out.print("Enter new amount: ");
                        double amount = sc.nextDouble();
                        sc.nextLine();

                        System.out.print("Enter new category: ");
                        String category = sc.nextLine();

                        System.out.print("Enter new description: ");
                        String description = sc.nextLine();

                        System.out.print("Enter new payment method: ");
                        String modeOfPayment = sc.nextLine();

                        System.out.print("Enter new date (YYYY-MM-DD): ");
                        LocalDate date = LocalDate.parse(sc.nextLine());

                        foundExpense.setAmount(amount);
                        foundExpense.setCategory(category);
                        foundExpense.setDescription(description);
                        foundExpense.setModeOfPayment(modeOfPayment);
                        foundExpense.setDate(date);

                        System.out.println("Expense updated successfully!");
                    }

                    break;
                }
                case 6: {
                    System.out.print("Enter expense ID to delete: ");
                    int searchId = sc.nextInt();

                    Expense foundExpense = null;

                    for (Expense expense : expenses) {
                        if (expense.getId() == searchId) {
                            foundExpense = expense;
                            break;
                        }
                    }

                    if (foundExpense == null) {
                        System.out.println("Expense not found!");
                    } else {
                        expenses.remove(foundExpense);
                        System.out.println("Expense deleted successfully!");
                    }

                    break;
                }
                case 7: {
                    sc.nextLine();

                    System.out.print("Enter category to search: ");
                    String searchCategory = sc.nextLine();

                    boolean found = false;

                    for (Expense expense : expenses) {
                        if (expense.getCategory().equalsIgnoreCase(searchCategory)) {
                            expense.displayExpense();
                            found = true;
                        }
                    }

                    if (!found) {
                        System.out.println("No expenses found in this category.");
                    }

                    break;
                }
                case 8: {
                    sc.nextLine();

                    System.out.print("Enter category: ");
                    String searchCategory = sc.nextLine();

                    double total = 0;

                    for (Expense expense : expenses) {
                        if (expense.getCategory().equalsIgnoreCase(searchCategory)) {
                            total += expense.getAmount();
                        }
                    }

                    System.out.println(
                            "Total spending in " + searchCategory + ": ₹" + total
                    );

                    break;
                }

                case 9:{
                    System.out.println("Exiting expense tracker");
                    break;
                }
                default:
                    System.out.println("invalid choice, please try again");

                }
        } while(choice != 9);

    }
}
