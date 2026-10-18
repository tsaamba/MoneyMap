import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        ExpenseDAO expenseDAO = new ExpenseDAO();
        Budget b1 = new Budget(3000);

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
            sc.nextLine();

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
                            0,
                            amount,
                            category,
                            description,
                            modeOfPayment,
                            date
                    );
                    expenseDAO.addExpense(expense);
                    System.out.println("Expense added successfully");
                    break;
            }
                case 2: {
                    List<Expense> expenses = expenseDAO.getAllExpenses();

                    if (expenses.isEmpty()) {
                        System.out.println("No expense records found.");
                    } else {
                        System.out.println("------- All Expenses -------");

                        for (Expense expense : expenses) {
                            expense.displayExpense();
                        }
                    }

                    break;
                }
                case 3: {
                    double totalAmount = expenseDAO.getTotalSpending();

                    System.out.println("Total Spent: ₹" + totalAmount);
                    break;
                }
                case 4: {
                    double totalSpent = expenseDAO.getTotalSpending();

                    double remaining =
                            b1.remainingMonthlyBudget(totalSpent);

                    b1.display();

                    System.out.println("Total Spent: ₹" + totalSpent);
                    System.out.println("Remaining Budget: ₹" + remaining);

                    break;
                }
                case 5: {
                    System.out.print("Enter expense ID to edit: ");
                    int searchId = sc.nextInt();
                    sc.nextLine();

                    // Find the existing expense in MySQL
                    Expense foundExpense = null;

                    for (Expense expense : expenseDAO.getAllExpenses()) {
                        if (expense.getId() == searchId) {
                            foundExpense = expense;
                            break;
                        }
                    }

                    if (foundExpense == null) {
                        System.out.println("Expense not found!");
                        break;
                    }

                    System.out.println("\nCurrent expense details:");
                    foundExpense.displayExpense();

                    System.out.println("\nWhat would you like to edit?");
                    System.out.println("1. Amount");
                    System.out.println("2. Category");
                    System.out.println("3. Description");
                    System.out.println("4. Payment Method");
                    System.out.println("5. Date");
                    System.out.println("6. Cancel");

                    System.out.print("Enter your choice: ");
                    int editChoice = sc.nextInt();
                    sc.nextLine();

                    switch (editChoice) {

                        case 1: {
                            System.out.print("Enter new amount: ");
                            double amount = sc.nextDouble();
                            sc.nextLine();

                            if (amount <= 0 || !Double.isFinite(amount)) {
                                System.out.println("Amount must be greater than zero.");
                                break;
                            }

                            foundExpense.setAmount(amount);
                            break;
                        }

                        case 2: {
                            System.out.print("Enter new category: ");
                            String category = sc.nextLine().trim();

                            if (category.isEmpty()) {
                                System.out.println("Category cannot be empty.");
                                break;
                            }

                            foundExpense.setCategory(category);
                            break;
                        }

                        case 3: {
                            System.out.print("Enter new description: ");
                            foundExpense.setDescription(sc.nextLine());
                            break;
                        }

                        case 4: {
                            System.out.print("Enter new payment method: ");
                            String payment = sc.nextLine().trim();

                            if (payment.isEmpty()) {
                                System.out.println("Payment method cannot be empty.");
                                break;
                            }

                            foundExpense.setModeOfPayment(payment);
                            break;
                        }

                        case 5: {
                            try {
                                System.out.print("Enter new date (YYYY-MM-DD): ");
                                LocalDate date = LocalDate.parse(sc.nextLine());
                                foundExpense.setDate(date);
                            } catch (java.time.format.DateTimeParseException e) {
                                System.out.println("Invalid date format.");
                                break;
                            }

                            break;
                        }

                        case 6: {
                            System.out.println("Edit cancelled.");
                            break;
                        }

                        default: {
                            System.out.println("Invalid edit choice.");
                            break;
                        }
                    }

                    if (editChoice >= 1 && editChoice <= 5) {
                        if (expenseDAO.updateExpense(foundExpense)) {
                            System.out.println("Expense updated successfully!");
                        } else {
                            System.out.println("Failed to update expense.");
                        }
                    }

                    break;
                }
                case 6: {
                    System.out.print("Enter expense ID to delete: ");
                    int searchId = sc.nextInt();
                    sc.nextLine();

                    if (expenseDAO.deleteExpense(searchId)) {
                        System.out.println("Expense deleted successfully!");
                    } else {
                        System.out.println("Expense not found or could not be deleted.");
                    }

                    break;
                }
                case 7: {
                    System.out.print("Enter category to search: ");
                    String searchCategory = sc.nextLine().trim();

                    List<Expense> results =
                            expenseDAO.searchByCategory(searchCategory);

                    if (results.isEmpty()) {
                        System.out.println("No expenses found in this category.");
                    } else {
                        System.out.println("------- Matching Expenses -------");

                        for (Expense expense : results) {
                            expense.displayExpense();
                        }
                    }

                    break;
                }
                case 8: {
                    System.out.print("Enter category: ");
                    String searchCategory = sc.nextLine().trim();

                    List<Expense> results =
                            expenseDAO.searchByCategory(searchCategory);

                    double total = 0;

                    for (Expense expense : results) {
                        total += expense.getAmount();
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
