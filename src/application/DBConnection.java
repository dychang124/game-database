package application;
import java.sql.*;

public class DBConnection {
    String url = "jdbc:mysql://localhost:3306/gamedb";
    String user = "user";
    String password = "password";

    public DBConnection(){

    }

    public void InsertPlayer(String Username){
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO player (username) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, Username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void PrintPlayers(){
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
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
            e.printStackTrace();
        }
    }
}
