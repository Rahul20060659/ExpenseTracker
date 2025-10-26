package view;



import model.User;
import model.Expense;
import dao.ExpenseDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * View Expenses Window
 * Displays all expenses in a table with edit/delete options
 */
public class ViewExpensesFrame extends JFrame {
    private User currentUser;
    private DashboardFrame dashboardFrame;
    private ExpenseDAO expenseDAO;

    private JTable expenseTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> monthFilterCombo;
    private JComboBox<String> categoryFilterCombo;
    private JLabel totalLabel;

    public ViewExpensesFrame(User user, DashboardFrame dashboard) {
        this.currentUser = user;
        this.dashboardFrame = dashboard;
        this.expenseDAO = new ExpenseDAO();
        initComponents();
        loadExpenses();
    }

    private void initComponents() {
        setTitle("View All Expenses");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JLabel headerLabel = new JLabel("All Expenses");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerLabel.setForeground(new Color(41, 128, 185));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(new Color(240, 240, 245));

        JLabel monthLabel = new JLabel("Month:");
        monthLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        filterPanel.add(monthLabel);

        String[] months = {"All", "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        monthFilterCombo = new JComboBox<>(months);
        monthFilterCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        monthFilterCombo.addActionListener(e -> applyFilters());
        filterPanel.add(monthFilterCombo);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        filterPanel.add(categoryLabel);

        String[] categories = {"All", "Food", "Travel", "Shopping", "Bills", "Entertainment", "Health", "Other"};
        categoryFilterCombo = new JComboBox<>(categories);
        categoryFilterCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        categoryFilterCombo.addActionListener(e -> applyFilters());
        filterPanel.add(categoryFilterCombo);

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(new Color(240, 240, 245));
        tablePanel.add(filterPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Title", "Amount", "Category", "Date", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        expenseTable = new JTable(tableModel);
        expenseTable.setFont(new Font("Arial", Font.PLAIN, 13));
        expenseTable.setRowHeight(30);
        expenseTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        expenseTable.getTableHeader().setBackground(new Color(52, 152, 219));
        expenseTable.getTableHeader().setForeground(Color.WHITE);
        expenseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Hide ID column
        expenseTable.getColumnModel().getColumn(0).setMinWidth(0);
        expenseTable.getColumnModel().getColumn(0).setMaxWidth(0);
        expenseTable.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(expenseTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Total panel
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(new Color(240, 240, 245));
        JLabel totalTextLabel = new JLabel("Total: ");
        totalTextLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPanel.add(totalTextLabel);

        totalLabel = new JLabel("₹0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setForeground(new Color(231, 76, 60));
        totalPanel.add(totalLabel);

        tablePanel.add(totalPanel, BorderLayout.SOUTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(240, 240, 245));

        JButton editButton = new JButton("Edit Selected");
        editButton.setFont(new Font("Arial", Font.BOLD, 14));
        editButton.setBackground(new Color(241, 196, 15));
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editButton.setPreferredSize(new Dimension(150, 35));
        editButton.addActionListener(e -> editSelectedExpense());
        buttonPanel.add(editButton);

        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 14));
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.setPreferredSize(new Dimension(150, 35));
        deleteButton.addActionListener(e -> deleteSelectedExpense());
        buttonPanel.add(deleteButton);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Arial", Font.PLAIN, 14));
        refreshButton.setBackground(new Color(52, 152, 219));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.setPreferredSize(new Dimension(120, 35));
        refreshButton.addActionListener(e -> loadExpenses());
        buttonPanel.add(refreshButton);

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        closeButton.setBackground(new Color(149, 165, 166));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.setPreferredSize(new Dimension(120, 35));
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadExpenses() {
        List<Expense> expenses = expenseDAO.getAllExpenses(currentUser.getUserId());
        displayExpenses(expenses);
    }

    private void applyFilters() {
        int monthIndex = monthFilterCombo.getSelectedIndex();
        String category = (String) categoryFilterCombo.getSelectedItem();

        List<Expense> expenses = expenseDAO.filterExpenses(
                currentUser.getUserId(),
                monthIndex,
                category
        );
        displayExpenses(expenses);
    }

    private void displayExpenses(List<Expense> expenses) {
        tableModel.setRowCount(0);
        double total = 0.0;

        for (Expense expense : expenses) {
            tableModel.addRow(new Object[]{
                    expense.getExpenseId(),
                    expense.getTitle(),
                    String.format("₹%.2f", expense.getAmount()),
                    expense.getCategory(),
                    expense.getExpenseDate().toString(),
                    expense.getDescription()
            });
            total += expense.getAmount();
        }

        totalLabel.setText(String.format("₹%.2f", total));
    }

    private void editSelectedExpense() {
        int selectedRow = expenseTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an expense to edit",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int expenseId = (int) tableModel.getValueAt(selectedRow, 0);

        // Find the expense
        List<Expense> allExpenses = expenseDAO.getAllExpenses(currentUser.getUserId());
        Expense selectedExpense = null;
        for (Expense exp : allExpenses) {
            if (exp.getExpenseId() == expenseId) {
                selectedExpense = exp;
                break;
            }
        }

        if (selectedExpense != null) {
            EditExpenseFrame editFrame = new EditExpenseFrame(selectedExpense, this, dashboardFrame);
            editFrame.setVisible(true);
        }
    }

    private void deleteSelectedExpense() {
        int selectedRow = expenseTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an expense to delete",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this expense?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            int expenseId = (int) tableModel.getValueAt(selectedRow, 0);
            boolean success = expenseDAO.deleteExpense(expenseId);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Expense deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                loadExpenses();
                dashboardFrame.refreshDashboard();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to delete expense",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refreshTable() {
        loadExpenses();
    }
}