package view;

import dao.ExpenseDAO;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;
/**
 * Dashboard Window
 * Shows expense summary and navigation options
 */
public class DashboardFrame extends JFrame {
    private User currentUser;
    private ExpenseDAO expenseDAO;

    private JLabel monthlyTotalLabel;
    private JLabel todayTotalLabel;
    private JTable categoryTable;
    private DefaultTableModel tableModel;

    public DashboardFrame(User user) {
        this.currentUser = user;
        this.expenseDAO = new ExpenseDAO();
        initComponents();
        loadDashboardData();
    }

    private void initComponents() {
        setTitle("Expense Tracker - Dashboard");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 245));

        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getUsername() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(41, 128, 185));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 12));
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> logout());
        headerPanel.add(logoutButton, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Summary panel
        JPanel summaryPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        summaryPanel.setBackground(new Color(240, 240, 245));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Monthly total card
        JPanel monthlyCard = createSummaryCard("This Month's Expenses", "₹0.00");
        monthlyTotalLabel = (JLabel) ((JPanel) monthlyCard.getComponent(1)).getComponent(0);
        summaryPanel.add(monthlyCard);

        // Today's total card
        JPanel todayCard = createSummaryCard("Today's Expenses", "₹0.00");
        todayTotalLabel = (JLabel) ((JPanel) todayCard.getComponent(1)).getComponent(0);
        summaryPanel.add(todayCard);

        mainPanel.add(summaryPanel, BorderLayout.CENTER);

        // Category table panel
        JPanel categoryPanel = new JPanel(new BorderLayout(5, 5));
        categoryPanel.setBackground(Color.WHITE);
        categoryPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel categoryLabel = new JLabel("Expenses by Category");
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 16));
        categoryLabel.setForeground(new Color(52, 73, 94));
        categoryPanel.add(categoryLabel, BorderLayout.NORTH);

        String[] columnNames = {"Category", "Total Amount"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        categoryTable = new JTable(tableModel);
        categoryTable.setFont(new Font("Arial", Font.PLAIN, 13));
        categoryTable.setRowHeight(30);
        categoryTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        categoryTable.getTableHeader().setBackground(new Color(52, 152, 219));
        categoryTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(categoryTable);
        scrollPane.setPreferredSize(new Dimension(0, 150));
        categoryPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(categoryPanel, BorderLayout.SOUTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setBackground(new Color(240, 240, 245));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton addExpenseButton = new JButton("➕ Add Expense");
        addExpenseButton.setFont(new Font("Arial", Font.BOLD, 14));
        addExpenseButton.setBackground(new Color(46, 204, 113));
        addExpenseButton.setForeground(Color.WHITE);
        addExpenseButton.setFocusPainted(false);
        addExpenseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addExpenseButton.addActionListener(e -> openAddExpense());
        buttonPanel.add(addExpenseButton);

        JButton viewExpensesButton = new JButton("🗒️ View All Expenses");
        viewExpensesButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewExpensesButton.setBackground(new Color(52, 152, 219));
        viewExpensesButton.setForeground(Color.WHITE);
        viewExpensesButton.setFocusPainted(false);
        viewExpensesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewExpensesButton.addActionListener(e -> openViewExpenses());
        buttonPanel.add(viewExpensesButton);

        // Add button panel above category panel
        JPanel centerContainer = new JPanel(new BorderLayout(0, 15));
        centerContainer.setBackground(new Color(240, 240, 245));
        centerContainer.add(summaryPanel, BorderLayout.NORTH);
        centerContainer.add(buttonPanel, BorderLayout.CENTER);
        centerContainer.add(categoryPanel, BorderLayout.SOUTH);

        mainPanel.add(centerContainer, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createSummaryCard(String title, String amount) {
        JPanel card = new JPanel(new BorderLayout(5, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(127, 140, 141));
        card.add(titleLabel, BorderLayout.NORTH);

        JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        amountPanel.setBackground(Color.WHITE);
        JLabel amountLabel = new JLabel(amount);
        amountLabel.setFont(new Font("Arial", Font.BOLD, 28));
        amountLabel.setForeground(new Color(231, 76, 60));
        amountPanel.add(amountLabel);
        card.add(amountPanel, BorderLayout.CENTER);

        return card;
    }

    private void loadDashboardData() {
        // Load monthly total
        double monthlyTotal = expenseDAO.getMonthlyTotal(currentUser.getUserId());
        monthlyTotalLabel.setText(String.format("₹%.2f", monthlyTotal));

        // Load today's total
        double todayTotal = expenseDAO.getTodayTotal(currentUser.getUserId());
        todayTotalLabel.setText(String.format("₹%.2f", todayTotal));

        // Load category totals
        Map<String, Double> categoryTotals = expenseDAO.getCategoryTotals(currentUser.getUserId());
        tableModel.setRowCount(0);

        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            tableModel.addRow(new Object[]{
                    entry.getKey(),
                    String.format("₹%.2f", entry.getValue())
            });
        }
    }

    public void refreshDashboard() {
        loadDashboardData();
    }

    private void openAddExpense() {
        AddExpenseFrame addExpenseFrame = new AddExpenseFrame(currentUser, this);
        addExpenseFrame.setVisible(true);
    }

    private void openViewExpenses() {
        ViewExpensesFrame viewExpensesFrame = new ViewExpensesFrame(currentUser, this);
        viewExpensesFrame.setVisible(true);
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            dispose();
        }
    }
}