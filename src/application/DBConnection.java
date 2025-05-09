package application;

import java.sql.*;
import java.util.ArrayList;

public class DBConnection {
    String url = "jdbc:mysql://localhost:3306/gamedb";
    String user = "user";
    String password = "password";

    public DBConnection() {
        printPlayers();
    }

    public void insertPlayer(String username) throws SQLException, CustomException {

        if (username.isEmpty()){
            throw new CustomException("Username too short");
        }

        Connection conn = DriverManager.getConnection(url, user, password);
        try {
            conn.setAutoCommit(false);

            String select = "SELECT player_id FROM player WHERE username = ?";
            PreparedStatement s = conn.prepareStatement(select);
            s.setString(1, username);
            ResultSet rs = s.executeQuery();

            if (rs.next()) {
                throw new CustomException("Username already exists");
            }

            String sql = "INSERT INTO player (username) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.executeUpdate();
        }catch (Exception e){
            conn.rollback();
            throw e;
        }
    }

    public int tryLogin(String username) throws SQLException, CustomException {
        int id = -1;
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT player_id FROM player WHERE username = ?";
            PreparedStatement s = conn.prepareStatement(sql);
            s.setString(1, username);
            ResultSet rs = s.executeQuery();

            if (rs.next()) {
                id = rs.getInt("player_id");
            }else{
                throw new CustomException("Username does not exist");
            }
        }catch (Exception e){
            throw e;
        }
        return id;
    }

    public ArrayList<ArrayList<String>> getLeaderboard() throws SQLException {
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

    public ArrayList<String> getOwnedChampions(int id) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT champion_name FROM ChampionsOwned WHERE player_id = " + id);

            ArrayList<String> owned = new ArrayList<>();

            while (rs.next()) {
                String champ = rs.getString("champion_name");
                owned.add(champ);
            }
            return owned;
        }
    }

    public ArrayList<StringIntTuple> getUnOwnedChampions(int id) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            String sql = "SELECT c.champion_name, c.price FROM Champion c LEFT JOIN ChampionsOwned co ON c.champion_name = co.champion_name AND co.player_id = ? WHERE co.player_id IS NULL";

            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, id);

            ResultSet rs = s.executeQuery();

            ArrayList<StringIntTuple> shop = new ArrayList<StringIntTuple>();

            while (rs.next()) {
                StringIntTuple t = new StringIntTuple(rs.getString("champion_name"), rs.getInt("price"));

                shop.add(t);
            }
            return shop;
        }
    }

    public int getBlueEssence(int playerId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT blue_essence FROM Player WHERE player_id = " + playerId;
            PreparedStatement s = conn.prepareStatement(sql);
            ResultSet rs = s.executeQuery();
            rs.next();
            return rs.getInt("blue_essence");
        }
    }

    public void purchaseChampion(int playerId, String championName) throws SQLException, CustomException {
        Connection conn = DriverManager.getConnection(url, user, password);
        try {
            String selectQuery = """
                        SELECT p.blue_essence, c.price 
                        FROM Player p, Champion c
                        WHERE p.player_id = ? AND c.champion_name = ?
                    """;

            String insertOwnership = "INSERT INTO ChampionsOwned (player_id, champion_name) VALUES (?, ?)";
            String updateEssence = "UPDATE Player SET blue_essence = blue_essence - ? WHERE player_id = ?";

            conn.setAutoCommit(false);

            PreparedStatement selectPS = conn.prepareStatement(selectQuery);
            PreparedStatement insertPS = conn.prepareStatement(insertOwnership);
            PreparedStatement updatePS = conn.prepareStatement(updateEssence);

            selectPS.setInt(1, playerId);
            selectPS.setString(2, championName);
            ResultSet rs = selectPS.executeQuery();
            rs.next();
            int blueEssence = rs.getInt("blue_essence");
            int price = rs.getInt("price");

            if (blueEssence < price) {
                throw new CustomException("Not enough blue essence!");
            }

            insertPS.setInt(1, playerId);
            insertPS.setString(2, championName);
            insertPS.executeUpdate();

            updatePS.setInt(1, price);
            updatePS.setInt(2, playerId);
            updatePS.executeUpdate();

            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        }
    }

    public void printPlayers() {
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

    public String playMatch(int playerId) throws SQLException {
        String output = null;
        Connection conn = DriverManager.getConnection(url, user, password);
        try {
            conn.setAutoCommit(false);

            String selectOpponent = "SELECT player_id FROM Player WHERE player_id != ? ORDER BY RAND() LIMIT 1";
            String insertMatchHistory = "INSERT INTO MatchHistory (match_date, game_mode, game_length) VALUES (NOW(), ?, ?)";
            String insertMatchParticipant =
                    """
                    INSERT INTO MatchParticipant (match_id, player_id, rank_awarded, win_loss, kills, deaths, assists)
                    VALUES (?, ?, ?, ?, ?, ?, ?)""";
            String updateRewards =
                                """
                                UPDATE Player SET player_level = player_level + 1,
                                blue_essence = blue_essence + ?,
                                player_rank = GREATEST(player_rank + ?, 0)
                                WHERE player_id = ?""";

            PreparedStatement selectPS = conn.prepareStatement(selectOpponent);
            selectPS.setInt(1, playerId);
            ResultSet rs = selectPS.executeQuery();

            rs.next();
            int opponentId = rs.getInt("player_id");

            PreparedStatement insertPS = conn.prepareStatement(insertMatchHistory, Statement.RETURN_GENERATED_KEYS);
            insertPS.setString(1, "Ranked Solo");
            int gameLength = (int) (Math.random() * 7200);
            insertPS.setInt(2, gameLength);
            insertPS.executeUpdate();

            ResultSet rs2 = insertPS.getGeneratedKeys();
            rs2.next();
            int matchId = rs2.getInt(1);

            PreparedStatement insertParticipantPS = conn.prepareStatement(insertMatchParticipant);

            int[] pids = {playerId, opponentId};
            int winningPlayer = -1;

            for (int i = 0; i < 2; i++) {
                int kills = (int) (Math.random() * 10);
                int deaths = (int) (Math.random() * 10);
                int assists = (int) (Math.random() * 10);

                if (winningPlayer == -1){
                    winningPlayer = kills > deaths ? 0 : 1;
                    output = "Match Completed\n" + (i == winningPlayer ? "Win" : "Loss") + "\nKills: " + kills + "\nDeaths: " + deaths + "\nAssists: " + assists +
                            "\nGame length: " + gameLength / 60 + ":" + gameLength % 60;
                }
                String winLoss = i == winningPlayer ? "Win" : "Loss";
                int rankAwarded = i == winningPlayer ? 1 : -1;

                insertParticipantPS.setInt(1, matchId);
                insertParticipantPS.setInt(2, pids[i]);
                insertParticipantPS.setInt(3, rankAwarded);
                insertParticipantPS.setString(4, winLoss);
                insertParticipantPS.setInt(5, kills);
                insertParticipantPS.setInt(6, deaths);
                insertParticipantPS.setInt(7, assists);
                insertParticipantPS.executeUpdate();
            }

            PreparedStatement updateRewardPS = conn.prepareStatement(updateRewards);
            for (int i = 0; i < 2; i++){
                int rankAwarded = i == winningPlayer ? 1 : -1;
                updateRewardPS.setInt(1, 5000);
                updateRewardPS.setInt(2, rankAwarded);
                updateRewardPS.setInt(3, pids[i]);
                updateRewardPS.executeUpdate();
            }
            conn.commit();
            return output;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    public ArrayList<MatchHistoryStruct> getMatchHistory(int playerId) throws SQLException {
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

    public int selectLevel(int playerId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT player_level FROM Player WHERE player_id = " + playerId;
            PreparedStatement s = conn.prepareStatement(sql);
            ResultSet rs = s.executeQuery();
            rs.next();
            return rs.getInt("player_level");
        }
    }

    public String selectPlayerProfile(int playerId) throws SQLException{
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT player_level, player_rank, blue_essence FROM Player WHERE player_id = " + playerId;
            PreparedStatement s = conn.prepareStatement(sql);
            ResultSet rs = s.executeQuery();
            rs.next();
            return "Level " + rs.getInt("player_level") + "   Rank Points: " + rs.getInt("player_rank") + "   Blue Essence: " + rs.getInt("blue_essence");
        }
    }
}
