package schoolmanagemnet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class DatabaseConnection {
    static String userName, password, url, driver;
    static Connection con;
    static Statement st;

    public static Connection DB_connect() {
        userName = "root";
        password = "mysql123";
        url = "jdbc:mysql://localhost:3306/sms?zeroDateTimeBehavior=CONVERT_TO_NULL [root on Default schema]";
        driver = "org.mysql.jdbc.Driver";
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, userName, password);
            st = con.createStatement();
            System.out.println("Connection is successful....");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
}