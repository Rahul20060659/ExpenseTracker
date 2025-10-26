

import view.LoginFrame;
import util.DatabaseUtil;
import javax.swing.*;

/**
 * Main Entry Point for Expense Tracker Application
 * Initializes database and launches login window
 */
public class Main {
    public static void main(String[] args) {
        // Set system look and feel for better UI
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize database tables
        DatabaseUtil.initializeDatabase();

        // Launch login frame on EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}