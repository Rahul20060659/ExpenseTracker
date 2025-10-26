package view;

import dao.ExpenseDAO;
import model.Expense;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
/**
 * Edit Expense Window
 * Form to edit existing expenses
 */
public class EditExpenseFrame extends JFrame {
    private Expense expense;
    private ViewExpensesFrame viewExpensesFrame;
    private DashboardFrame dashboardFrame;
    private ExpenseDAO expenseDAO;

    private JTextField titleField;
    private JTextField amountField;
    private JComboBox<String> categoryCombo;
    private JTextField dateField;
    private JTextArea descriptionArea;

    public EditExpenseFrame(Expense expense, ViewExpensesFrame viewFrame, DashboardFrame dashboard) {
        this.expense = expense;
        this.viewExpensesFrame = viewFrame;
        this.dashboardFrame = dashboard;
        this.expenseDAO = new ExpenseDAO();
        initComponents();
        populateFields();
    }

    private void initComponents() {
        setTitle("Edit Expense");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 240, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        // Title
        JLabel headerLabel = new JLabel("Edit Expense");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(new Color(41, 128, 185));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 20, 5);
        mainPanel.add(headerLabel, gbc);

        // Expense Title
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.gridy = 1;
        JLabel titleLabel = new JLabel("Expense Title:");
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(titleLabel, gbc);

        gbc.gridx = 1;
        titleField = new JTextField(20);
        titleField.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(titleField, gbc);

        // Amount
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel amountLabel = new JLabel("Amount (₹):");
        amountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(amountLabel, gbc);

        gbc.gridx = 1;
        amountField = new JTextField(20);
        amountField.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(amountField, gbc);

        // Category
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(categoryLabel, gbc);

        gbc.gridx = 1;
        String[] categories = {"Food", "Travel", "Shopping", "Bills", "Entertainment", "Health", "Other"};
        categoryCombo = new JComboBox<>(categories);
        categoryCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(categoryCombo, gbc);

        // Date
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(dateLabel, gbc);

        gbc.gridx = 1;
        dateField = new JTextField();
        dateField.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(dateField, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(descriptionLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        mainPanel.add(scrollPane, gbc);

        // Button panel
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(new Color(240, 240, 245));

        JButton updateButton = new JButton("Update Expense");
        updateButton.setFont(new Font("Arial", Font.BOLD, 14));
        updateButton.setBackground(new Color(241, 196, 15));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateButton.setPreferredSize(new Dimension(160, 35));
        updateButton.addActionListener(e -> updateExpense());
        buttonPanel.add(updateButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.setBackground(new Color(149, 165, 166));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setPreferredSize(new Dimension(120, 35));
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
    }

    private void populateFields() {
        titleField.setText(expense.getTitle());
        amountField.setText(String.valueOf(expense.getAmount()));
        categoryCombo.setSelectedItem(expense.getCategory());
        dateField.setText(expense.getExpenseDate().toString());
        descriptionArea.setText(expense.getDescription());
    }

    private void updateExpense() {
        String title = titleField.getText().trim();
        String amountStr = amountField.getText().trim();
        String category = (String) categoryCombo.getSelectedItem();
        String dateStr = dateField.getText().trim();
        String description = descriptionArea.getText().trim();

        // Validation
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter expense title",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid positive amount",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate expenseDate;
        try {
            expenseDate = LocalDate.parse(dateStr);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter date in YYYY-MM-DD format",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Update expense object
        expense.setTitle(title);
        expense.setAmount(amount);
        expense.setCategory(category);
        expense.setExpenseDate(expenseDate);
        expense.setDescription(description);

        // Update in database
        boolean success = expenseDAO.updateExpense(expense);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Expense updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Refresh tables
            viewExpensesFrame.refreshTable();
            dashboardFrame.refreshDashboard();

            // Close window
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to update expense. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}