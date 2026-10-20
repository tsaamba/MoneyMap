import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

public class ExpenseDAO {

    public void addExpense(Expense expense) {

        String sql = """
                INSERT INTO expenses
                (amount, category, description, mode_of_payment, expense_date)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setDouble(1, expense.getAmount());
            statement.setString(2, expense.getCategory());
            statement.setString(3, expense.getDescription());
            statement.setString(4, expense.getModeOfPayment());
            statement.setDate(
                    5,
                    java.sql.Date.valueOf(expense.getDate())
            );

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Expense saved to MySQL successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Failed to save expense.");
            System.out.println(e.getMessage());
        }
    }

    public List<Expense> getAllExpenses() {

        List<Expense> expenses = new ArrayList<>();

        String sql = "SELECT * FROM expenses";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Expense expense = new Expense(
                        resultSet.getInt("id"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("category"),
                        resultSet.getString("description"),
                        resultSet.getString("mode_of_payment"),
                        resultSet.getDate("expense_date").toLocalDate()
                );

                expenses.add(expense);
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve expenses.");
            System.out.println(e.getMessage());
        }

        return expenses;
    }

    public boolean updateExpense(Expense expense) {
        String sql = """
                UPDATE expenses
                SET amount = ?, category = ?, description = ?,
                    mode_of_payment = ?, expense_date = ?
                WHERE id = ?
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setDouble(1, expense.getAmount());
            statement.setString(2, expense.getCategory());
            statement.setString(3, expense.getDescription());
            statement.setString(4, expense.getModeOfPayment());
            statement.setDate(5, java.sql.Date.valueOf(expense.getDate()));
            statement.setInt(6, expense.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Failed to update expense: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteExpense(int id) {
        String sql = "DELETE FROM expenses WHERE id = ?";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Failed to delete expense: " + e.getMessage());
            return false;
        }
    }

    public List<Expense> searchByCategory(String category) {
        List<Expense> results = new ArrayList<>();

        String sql = """
                SELECT * FROM expenses
                WHERE LOWER(category) = LOWER(?)
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, category);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Expense expense = new Expense(
                            resultSet.getInt("id"),
                            resultSet.getDouble("amount"),
                            resultSet.getString("category"),
                            resultSet.getString("description"),
                            resultSet.getString("mode_of_payment"),
                            resultSet.getDate("expense_date").toLocalDate()
                    );

                    results.add(expense);
                }
            }

        } catch (SQLException e) {
            System.out.println("Failed to search expenses: " + e.getMessage());
        }

        return results;
    }

    public double getTotalSpending() {
        String sql = "SELECT COALESCE(SUM(amount), 0) AS total FROM expenses";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            if (resultSet.next()) {
                return resultSet.getDouble("total");
            }

        } catch (SQLException e) {
            System.out.println("Failed to calculate spending: " + e.getMessage());
        }

        return 0;
    }


    public double getCurrentMonthSpending() {
        String sql = """
                SELECT COALESCE(SUM(amount), 0) AS total
                FROM expenses
                WHERE expense_date >= ?
                  AND expense_date < ?
                """;

        LocalDate today = LocalDate.now();

        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate startOfNextMonth = startOfMonth.plusMonths(1);

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setDate(1, java.sql.Date.valueOf(startOfMonth));
            statement.setDate(2, java.sql.Date.valueOf(startOfNextMonth));

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("total");
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Failed to calculate current month's spending: "
                            + e.getMessage()
            );
        }

        return 0;
    }

    public double getMonthlyBudget() {
        String sql = "SELECT monthly_limit FROM budgets WHERE id = 1";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            if (resultSet.next()) {
                return resultSet.getDouble("monthly_limit");
            }

            System.out.println("No budget found in the database.");

        } catch (SQLException e) {
            System.out.println("Failed to retrieve budget: " + e.getMessage());
        }

        return 0;
    }
    public boolean updateMonthlyBudget(double monthlyLimit) {
        if (!Double.isFinite(monthlyLimit) || monthlyLimit <= 0) {
            System.out.println("Budget must be greater than zero.");
            return false;
        }

        String sql = """
            UPDATE budgets
            SET monthly_limit = ?
            WHERE id = 1
            """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setDouble(1, monthlyLimit);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                return true;
            }

            System.out.println("No budget record found to update.");

        } catch (SQLException e) {
            System.out.println("Failed to update budget: " + e.getMessage());
        }

        return false;
    }
}