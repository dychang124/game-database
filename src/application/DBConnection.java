package application;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {
    String url = "jdbc:mysql://localhost:3306/gamedb";
    String user = "user";
    String password = "password";

    public DBConnection(){
        PrintPlayers();
    }

    public void InsertPlayer(String Username) throws SQLException{
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO player (username) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, Username);
            stmt.executeUpdate();
        }
    }

    public boolean TryLogin(String username) throws SQLException{
        boolean userFound = false;
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            String sql = "SELECT * FROM player WHERE username = ?";
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, username);
            ResultSet rs = s.executeQuery();

            if (rs.next()) {
                userFound = true;
            }
        }
        return userFound;
    }

    public ArrayList<String> GetLeaderboard() throws SQLException{
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM player ORDER BY player_rank DESC");


            ArrayList<String> leaderboard = new ArrayList<>();

            while (rs.next()) {
                int level = rs.getInt("player_level");
                String username = rs.getString("username");
                String rank = rs.getString("player_rank");

                leaderboard.add((level + " " + username + " " + rank)) ;
            }
            return leaderboard;
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
