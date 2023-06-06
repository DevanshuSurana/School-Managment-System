import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class LoginSystem {
    
    // Constants for MySQL database connection
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sms";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "mysql123@";
    
    // Constants for log file
    private static final String LOG_FILE = "login_log.txt";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static void main(String[] args) {
        // Establish MySQL database connection
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            System.out.println("Failed to connect to MySQL database.");
            e.printStackTrace();
            System.exit(1);
        }
        
        // Prompt user for login credentials
        Scanner scanner = new Scanner(System.in);
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        // Validate login credentials against MySQL database
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean isValidLogin = false;
        try {
            stmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ? AND password = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if (count > 0) {
                isValidLogin = true;
            }
        } catch (SQLException e) {
            System.out.println("Failed to validate login credentials.");
            e.printStackTrace();
            System.exit(1);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                System.out.println("Failed to close database resources.");
                e.printStackTrace();
                System.exit(1);
            }
        }
        
        // Log login activity to file
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(LOG_FILE, true));
            String timestamp = DATE_FORMAT.format(new Date());
            String logMessage = String.format("%s - %s: %s", timestamp, username, isValidLogin ? "Login successful" : "Login failed");
            writer.write(logMessage);
            writer.newLine();
            System.out.println(logMessage);
        } catch (IOException e) {
            System.out.println("Failed to write to log file.");
            e.printStackTrace();
            System.exit(1);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                System.out.println("Failed to close log file.");
                e.printStackTrace();
                System.exit(1);
            }
        }
        
        // Close MySQL database connection
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Failed to close MySQL database connection.");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
