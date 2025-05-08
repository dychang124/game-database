package application.src.main.java.com.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        String url = "jdbc:mysql://localhost:3306/gamedb"; // DB name here
        String user = "user"; // your username
        String password = "password"; // your password

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to MySQL database!");
        } catch (SQLException e) {
            System.out.println("Connection failed:");
            e.printStackTrace();
        }
    }

}
