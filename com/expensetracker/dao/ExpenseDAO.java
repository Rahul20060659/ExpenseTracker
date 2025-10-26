package dao;

import model.Expense;
import util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Expense Data Access Object
 * Handles all database operations for Expense entity
 */
public class ExpenseDAO {

    /**
     * Add a new expense
     * @param expense Expense object
     * @return true if successful
     */
    public boolean addExpense(Expense expense) {
        String sql = "INSERT INTO expenses (user_id, title, amount, category, expense_date, description) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, expense.getUserId());
            pstmt.setString(2, expense.getTitle());
            pstmt.setDouble(3, expense.getAmount());
            pstmt.setString(4, expense.getCategory());
            pstmt.setString(5, expense.getExpenseDate().toString());
            pstmt.setString(6, expense.getDescription());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding expense: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all expenses for a user
     * @param userId User ID
     * @return List of expenses
     */
    public List<Expense> getAllExpenses(int userId) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE user_id = ? ORDER BY expense_date DESC";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Expense expense = extractExpenseFromResultSet(rs);
                expenses.add(expense);
            }

        } catch (SQLException e) {
            System.err.println("Error getting expenses: " + e.getMessage());
        }

        return expenses;
    }

    /**
     * Update an existing expense
     * @param expense Expense object with updated data
     * @return true if successful
     */
    public boolean updateExpense(Expense expense) {
        String sql = "UPDATE expenses SET title = ?, amount = ?, category = ?, " +
                "expense_date = ?, description = ? WHERE expense_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, expense.getTitle());
            pstmt.setDouble(2, expense.getAmount());
            pstmt.setString(3, expense.getCategory());
            pstmt.setString(4, expense.getExpenseDate().toString());
            pstmt.setString(5, expense.getDescription());
            pstmt.setInt(6, expense.getExpenseId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating expense: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete an expense
     * @param expenseId Expense ID
     * @return true if successful
     */
    public boolean deleteExpense(int expenseId) {
        String sql = "DELETE FROM expenses WHERE expense_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, expenseId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting expense: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get total expenses for current month
     * @param userId User ID
     * @return Total amount
     */
    public double getMonthlyTotal(int userId) {
        LocalDate now = LocalDate.now();
        String sql = "SELECT SUM(amount) FROM expenses WHERE user_id = ? " +
                "AND strftime('%Y-%m', expense_date) = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, String.format("%04d-%02d", now.getYear(), now.getMonthValue()));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            System.err.println("Error getting monthly total: " + e.getMessage());
        }

        return 0.0;
    }

    /**
     * Get total expenses for today
     * @param userId User ID
     * @return Total amount
     */
    public double getTodayTotal(int userId) {
        String sql = "SELECT SUM(amount) FROM expenses WHERE user_id = ? AND expense_date = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, LocalDate.now().toString());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            System.err.println("Error getting today's total: " + e.getMessage());
        }

        return 0.0;
    }

    /**
     * Get expenses grouped by category
     * @param userId User ID
     * @return Map of category to total amount
     */
    public Map<String, Double> getCategoryTotals(int userId) {
        Map<String, Double> categoryTotals = new HashMap<>();
        String sql = "SELECT category, SUM(amount) as total FROM expenses " +
                "WHERE user_id = ? GROUP BY category";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                categoryTotals.put(rs.getString("category"), rs.getDouble("total"));
            }

        } catch (SQLException e) {
            System.err.println("Error getting category totals: " + e.getMessage());
        }

        return categoryTotals;
    }

    /**
     * Filter expenses by month and category
     * @param userId User ID
     * @param month Month (1-12) or 0 for all
     * @param category Category or "All"
     * @return Filtered list of expenses
     */
    public List<Expense> filterExpenses(int userId, int month, String category) {
        List<Expense> expenses = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM expenses WHERE user_id = ?");

        if (month > 0) {
            sql.append(" AND strftime('%m', expense_date) = ?");
        }
        if (!category.equals("All")) {
            sql.append(" AND category = ?");
        }
        sql.append(" ORDER BY expense_date DESC");

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            pstmt.setInt(paramIndex++, userId);

            if (month > 0) {
                pstmt.setString(paramIndex++, String.format("%02d", month));
            }
            if (!category.equals("All")) {
                pstmt.setString(paramIndex++, category);
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Expense expense = extractExpenseFromResultSet(rs);
                expenses.add(expense);
            }

        } catch (SQLException e) {
            System.err.println("Error filtering expenses: " + e.getMessage());
        }

        return expenses;
    }

    /**
     * Helper method to extract Expense from ResultSet
     */
    private Expense extractExpenseFromResultSet(ResultSet rs) throws SQLException {
        Expense expense = new Expense();
        expense.setExpenseId(rs.getInt("expense_id"));
        expense.setUserId(rs.getInt("user_id"));
        expense.setTitle(rs.getString("title"));
        expense.setAmount(rs.getDouble("amount"));
        expense.setCategory(rs.getString("category"));
        expense.setExpenseDate(LocalDate.parse(rs.getString("expense_date")));
        expense.setDescription(rs.getString("description"));
        return expense;
    }
}