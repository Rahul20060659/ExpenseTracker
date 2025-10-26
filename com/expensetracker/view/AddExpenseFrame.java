package view;

import dao.ExpenseDAO;
import model.Expense;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * Add Expense Window
 * Form to add new expenses
 */
public class AddExpenseFrame extends JFrame {
    private User currentUser;
    private DashboardFrame dashboardFrame;
    private ExpenseDAO expenseDAO;

    private JTextField titleField;
    private JTextField amountField;
    private JComboBox<String> categoryCombo;
    private JTextField dateField;
    private JTextArea descriptionArea;

    public AddExpenseFrame(User user, DashboardFrame dashboard) {
        this.currentUser = user;
        this.dashboardFrame = dashboard;
        this.expenseDAO = new ExpenseDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("Add New Expense");
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
        JLabel headerLabel = new JLabel("Add New Expense");
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
        dateField = new JTextField(LocalDate.now().toString());
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

        JButton saveButton = new JButton("Save Expense");
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setBackground(new Color(46, 204, 113));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.setPreferredSize(new Dimension(150, 35));
        saveButton.addActionListener(e -> saveExpense());
        buttonPanel.add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.setBackground(new Color(149, 165, 166));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setPreferredSize(new Dimension(150, 35));
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
    }

    private void saveExpense() {
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

        // Create expense object
        Expense expense = new Expense(
                currentUser.getUserId(),
                title,
                amount,
                category,
                expenseDate,
                description
        );

        // Save to database
        boolean success = expenseDAO.addExpense(expense);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Expense added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Refresh dashboard
            dashboardFrame.refreshDashboard();

            // Close window
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to add expense. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}