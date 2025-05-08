package application.src.main.java.com.example;
import java.sql.*;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        String url = "jdbc:mysql://localhost:3306/gamedb";
        String user = "user";
        String password = "password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to MySQL database!");
            /*
            String sql = "INSERT INTO player (username) VALUES ('playerOne')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            */
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM player");

            System.out.println("player_id | username   | player_level | player_rank");
            System.out.println("----------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("player_id");
                String username = rs.getString("username");
                int level = rs.getInt("player_level");
                String rank = rs.getString("player_rank");

                System.out.printf("%9d | %-10s | %5d | %-10s%n", id, username, level, rank);
            }

        } catch (SQLException e) {
            System.out.println("Connection failed:");
            e.printStackTrace();
        }


    }

}
