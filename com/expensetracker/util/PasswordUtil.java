package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Password Utility Class
 * Handles password hashing using SHA-256
 */
public class PasswordUtil {

    /**
     * Hash password using SHA-256
     * @param password Plain text password
     * @return Hashed password as hex string
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            // Convert bytes to hex string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Verify password against stored hash
     * @param password Plain text password
     * @param hashedPassword Stored hashed password
     * @return true if passwords match
     */
    public static boolean verifyPassword(String password, String hashedPassword) {
        String hashedInput = hashPassword(password);
        return hashedInput.equals(hashedPassword);
    }
}