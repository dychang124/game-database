package application;
import java.awt.image.AreaAveragingScaleFilter;
import java.sql.*;
import java.util.ArrayList;

public class DBConnection {
    String url = "jdbc:mysql://localhost:3306/gamedb";
    String user = "user";
    String password = "password";

    public DBConnection(){
        PrintPlayers();
    }

    public int InsertPlayer(String Username) throws SQLException{
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO player (username) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, Username);
            stmt.executeUpdate();

            return 1;
        }
    }

    public int TryLogin(String username) throws SQLException{
        int id = -1;
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            String sql = "SELECT player_id FROM player WHERE username = ?";
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, username);
            ResultSet rs = s.executeQuery();

            if (rs.next()) {
                id = rs.getInt("player_id");
            }
        }
        return id;
    }

    public ArrayList<ArrayList<String>> GetLeaderboard() throws SQLException{
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM player ORDER BY player_rank DESC");


            ArrayList<ArrayList<String>> leaderboard = new ArrayList<>();

            while (rs.next()) {
                String username = rs.getString("username");
                int rank = rs.getInt("player_rank");

                ArrayList<String> temp = new ArrayList<>();
                leaderboard.add(temp);
                temp.add(username);
                temp.add(rank + "");
            }
            return leaderboard;
        }
    }

    public ArrayList<String> GetOwnedChampions(int id) throws SQLException{
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT champion_name FROM ChampionsOwned WHERE player_id = " + id);

            ArrayList<String> owned = new ArrayList<>();

            while (rs.next()) {
                String champ = rs.getString("champion_name");
                owned.add(champ) ;
            }
            return owned;
        }
    }

    public ArrayList<StringIntTuple> GetUnOwnedChampions(int id) throws SQLException{
        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            String sql = "SELECT c.champion_name, c.price FROM Champion c LEFT JOIN ChampionsOwned co ON c.champion_name = co.champion_name AND co.player_id = ? WHERE co.player_id IS NULL";

            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, id);

            ResultSet rs = s.executeQuery();

            ArrayList<StringIntTuple> shop = new ArrayList<StringIntTuple>();

            while (rs.next()) {
                StringIntTuple t = new StringIntTuple(rs.getString("champion_name"), rs.getInt("price"));

                shop.add(t) ;
            }
            return shop;
        }
    }



    public ArrayList<String> PurchaseChampion(String championName){
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
    public void updatePlayerStats(String username, int blueEssenceEarned) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "UPDATE Player " +
                         "SET player_level = player_level + 1, " +
                         "blue_essence = blue_essence + ? " +
                         "WHERE username = ?";
    
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, blueEssenceEarned);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }
    public int createMatch(String gameMode, int gameLength) throws SQLException {
        int matchId = -1;
        String sql = "INSERT INTO MatchHistory (match_date, game_mode, game_length) VALUES (NOW(), ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, gameMode);
            stmt.setInt(2, gameLength);
            stmt.executeUpdate();
    
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                matchId = rs.getInt(1);
            }
        }
    
        return matchId;
    }
    
    public int getPlayerIdByUsername(String username) throws SQLException {
        int playerId = -1;
        String sql = "SELECT player_id FROM Player WHERE username = ?";
    
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                playerId = rs.getInt("player_id");
            }
        }
    
        return playerId;
    }
    
    public void insertMatchParticipant(int matchId, int playerId, int rankAwarded, String winLoss, int kills, int deaths, int assists) throws SQLException {
        String sql = "INSERT INTO MatchParticipant (match_id, player_id, rank_awarded, win_loss, kills, deaths, assists) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, matchId);
            stmt.setInt(2, playerId);
            stmt.setInt(3, rankAwarded);
            stmt.setString(4, winLoss);
            stmt.setInt(5, kills);
            stmt.setInt(6, deaths);
            stmt.setInt(7, assists);
            stmt.executeUpdate();
        }
    }

    public void playMatch(int playerId){
        //get opponent id

    }

    public ArrayList<MatchHistoryStruct> GetMatchHistory(int playerId) throws SQLException {
        ArrayList<MatchHistoryStruct> matchHistoryStructs = new ArrayList<>();

        String sql = """
            SELECT 
                mh.match_date,
                mp1.win_loss,
                p2.username AS opponent_username,
                mp1.kills,
                mp1.deaths,
                mp1.assists,
                mh.game_length
            FROM MatchParticipant mp1
            JOIN MatchHistory mh ON mp1.match_id = mh.match_id
            JOIN MatchParticipant mp2 ON mp1.match_id = mp2.match_id AND mp1.player_id != mp2.player_id
            JOIN Player p2 ON mp2.player_id = p2.player_id
            WHERE mp1.player_id = ?
            ORDER BY mh.match_date DESC
        """;

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, playerId);
            try (ResultSet rs = s.executeQuery()) {
                while (rs.next()) {
                    Timestamp matchDate = rs.getTimestamp("match_date");
                    String winLoss = rs.getString("win_loss");
                    String opponent = rs.getString("opponent_username");
                    int kills = rs.getInt("kills");
                    int deaths = rs.getInt("deaths");
                    int assists = rs.getInt("assists");
                    int gameLength = rs.getInt("game_length");

                    matchHistoryStructs.add(new MatchHistoryStruct(matchDate, winLoss, opponent, kills, deaths, assists, gameLength));
                }

                return matchHistoryStructs;
            }
        }
    }
}
