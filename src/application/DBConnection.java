package application;

import java.sql.*;
import java.util.ArrayList;

/**
 * Connection where presentation layer interacts with application layer
 */
public class DBConnection {
    String url = "jdbc:mysql://localhost:3306/gamedb";
    String user = "user";
    String password = "password";

    public DBConnection() {
        //printPlayers();
    }

    /**
     * Inserts player with specified username into database
     * @param username
     * @throws SQLException
     * @throws CustomException if username is too short, long, or already exists
     */
    public void insertPlayer(String username) throws SQLException, CustomException {

        if (username.isEmpty()){
            throw new CustomException("Username too short");
        }else if (username.length() > 50){
            throw new CustomException("Username too long");
        }

        Connection conn = DriverManager.getConnection(url, user, password);
        try {
            conn.setAutoCommit(false);

            //Checks if player with username already exists
            String select = "SELECT player_id FROM player WHERE username = ?";
            PreparedStatement s = conn.prepareStatement(select);
            s.setString(1, username);
            ResultSet rs = s.executeQuery();

            if (rs.next()) {
                throw new CustomException("Username already exists");
            }

            //Inserts player with username and default values into database
            String sql = "INSERT INTO player (username) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.executeUpdate();
            conn.commit();
        }catch (Exception e){
            conn.rollback();
            throw e;
        }
    }

    /**
     * Deletes specified player and related data from the database
     * @param playerID
     * @throws SQLException
     * @throws CustomException
     */
    public void deletePlayer(int playerID) throws SQLException, CustomException {
        Connection conn = DriverManager.getConnection(url, user, password);
        try  {
            conn.setAutoCommit(false);

            //Deletes player
            String deleteSQL = "DELETE FROM Player WHERE player_id = ?";
            PreparedStatement ps = conn.prepareStatement(deleteSQL);
            ps.setInt(1, playerID);
            int affected = ps.executeUpdate();

            if (affected == 0) {
                throw new CustomException("Player ID not found");
            }

            conn.commit();
            ps.close();

        } catch (Exception e) {
            conn.rollback();
            throw e;  // Connection is still auto-closed
        }
    }


    /**
     * Tries to login with given username
     * @param username
     * @return player_id of username
     * @throws SQLException
     * @throws CustomException if username does not exist
     */
    public int tryLogin(String username) throws SQLException, CustomException {
        int id = -1;
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            //Selects player_id of player with username
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

    /**
     * Queries for username and player rank of top 15 ranked players
     * @return list of username, player rank pairs
     * @throws SQLException
     */
    public ArrayList<ArrayList<String>> getLeaderboard() throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            Statement s = conn.createStatement();

            //selects data of top 15 ranked players
            ResultSet rs = s.executeQuery("SELECT username, player_rank FROM player ORDER BY player_rank DESC LIMIT 15");

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

    /**
     * Queries for champions owned by player with specified id
     * @param id
     * @return list of champion names
     * @throws SQLException
     */
    public ArrayList<String> getOwnedChampions(int id) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            Statement s = conn.createStatement();

            //Selects champions owned by player
            ResultSet rs = s.executeQuery("SELECT champion_name FROM ChampionsOwned WHERE player_id = " + id);

            ArrayList<String> owned = new ArrayList<>();

            while (rs.next()) {
                String champ = rs.getString("champion_name");
                owned.add(champ);
            }
            return owned;
        }
    }

    /**
     * Queries for champions not owned by player with specified id and their prices
     * @param id
     * @return list of champion price tuples
     * @throws SQLException
     */
    public ArrayList<StringIntTuple> getUnOwnedChampions(int id) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {

            //Selects name and price of champions not owned by player_id
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

    /**
     * Queries for blue essence count of specified player_id
     * @param playerId
     * @return amount of blue essence
     * @throws SQLException
     */
    public int getBlueEssence(int playerId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            //Selects blue_essence of player_id
            String sql = "SELECT blue_essence FROM Player WHERE player_id = " + playerId;
            PreparedStatement s = conn.prepareStatement(sql);
            ResultSet rs = s.executeQuery();
            rs.next();
            return rs.getInt("blue_essence");
        }
    }

    /**
     * Attempts to purchase champion for specified player
     * @param playerId
     * @param championName
     * @throws SQLException
     * @throws CustomException if player does not have enough currency to purchase champion
     */
    public void purchaseChampion(int playerId, String championName) throws SQLException, CustomException {
        Connection conn = DriverManager.getConnection(url, user, password);
        try {

            //Selects amount of blue essence player has and price of champion
            String selectQuery = """
                        SELECT p.blue_essence, c.price 
                        FROM Player p, Champion c
                        WHERE p.player_id = ? AND c.champion_name = ?
                    """;

            //Inserts champion into player's ownership
            String insertOwnership = "INSERT INTO ChampionsOwned (player_id, champion_name) VALUES (?, ?)";

            //Subtracts cost of champion from player's balance
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

            //check for sufficient currency
            if (blueEssence < price) {
                throw new CustomException("Not enough blue essence!");
            }

            //grants ownership
            insertPS.setInt(1, playerId);
            insertPS.setString(2, championName);
            insertPS.executeUpdate();

            //update balance
            updatePS.setInt(1, price);
            updatePS.setInt(2, playerId);
            updatePS.executeUpdate();

            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        }
    }

    /**
     * Prints list of players and basic information
     */
    public void printPlayers() {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            Statement s = conn.createStatement();
            //Select all players and information
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

    /**
     * Simulates a game between player and a randomly selected opponent, updating database for both with corresponding information
     * @param playerId
     * @return basic message containing match results
     * @throws SQLException
     */
    public String playMatch(int playerId) throws SQLException {
        String output = null;
        Connection conn = DriverManager.getConnection(url, user, password);
        try {
            //start transaction
            conn.setAutoCommit(false);

            //Selects a random opponent
            String selectOpponent = "SELECT player_id FROM Player WHERE player_id != ? ORDER BY RAND() LIMIT 1";
            //Inserts match information into database
            String insertMatchHistory = "INSERT INTO MatchHistory (match_date, game_mode, game_length) VALUES (NOW(), ?, ?)";
            //Inserts participation of match and performance of a specified player into database
            String insertMatchParticipant =
                    """
                    INSERT INTO MatchParticipant (match_id, player_id, rank_awarded, win_loss, kills, deaths, assists)
                    VALUES (?, ?, ?, ?, ?, ?, ?)""";
            //Rewards specified player with levels and blue essence and updates rank
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
            //Get opponent
            int opponentId = rs.getInt("player_id");

            //Generate match data and insert into database
            PreparedStatement insertPS = conn.prepareStatement(insertMatchHistory, Statement.RETURN_GENERATED_KEYS);
            insertPS.setString(1, "Ranked Solo");
            int gameLength = (int) (Math.random() * 7200);
            insertPS.setInt(2, gameLength);
            insertPS.executeUpdate();

            ResultSet rs2 = insertPS.getGeneratedKeys();
            rs2.next();
            int matchId = rs2.getInt(1);

            //Have both players participate in generated match_id
            PreparedStatement insertParticipantPS = conn.prepareStatement(insertMatchParticipant);

            int[] pids = {playerId, opponentId};
            int winningPlayer = -1;

            //Generate both player's match performance
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

            //Distribute rewards and ranking to both players
            PreparedStatement updateRewardPS = conn.prepareStatement(updateRewards);
            for (int i = 0; i < 2; i++){
                int rankAwarded = i == winningPlayer ? 1 : -1;
                updateRewardPS.setInt(1, 5000);
                updateRewardPS.setInt(2, rankAwarded);
                updateRewardPS.setInt(3, pids[i]);
                updateRewardPS.executeUpdate();
            }

            //End transaction
            conn.commit();
            return output;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        }
    }

    /**
     * Queries for match history of specified player
     * @param playerId
     * @return list of objects each containing data for a match
     * @throws SQLException
     */
    public ArrayList<MatchHistoryStruct> getMatchHistory(int playerId) throws SQLException {
        ArrayList<MatchHistoryStruct> matchHistoryStructs = new ArrayList<>();

        //Selects data from player's match history
        //Date of match, win or loss, opponent's username, kills, deaths, assists, and game duration
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
                    //Add match data to output list
                    matchHistoryStructs.add(new MatchHistoryStruct(matchDate, winLoss, opponent, kills, deaths, assists, gameLength));
                }

                return matchHistoryStructs;
            }
        }
    }

    /**
     * Queries for level of specified player
     * @param playerId
     * @return player's level
     * @throws SQLException
     */
    public int selectLevel(int playerId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT player_level FROM Player WHERE player_id = " + playerId;
            PreparedStatement s = conn.prepareStatement(sql);
            ResultSet rs = s.executeQuery();
            rs.next();
            return rs.getInt("player_level");
        }
    }

    /**
     * Queries for profile information of specified player
     * @param playerId
     * @return formatted display of player level, rank, and blue essence
     * @throws SQLException
     */
    public String selectPlayerProfile(int playerId) throws SQLException{//Get player information
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT player_level, player_rank, blue_essence FROM Player WHERE player_id = " + playerId;
            PreparedStatement s = conn.prepareStatement(sql);
            ResultSet rs = s.executeQuery();
            rs.next();
            return "Level " + rs.getInt("player_level") + "   Rank Points: " + rs.getInt("player_rank") + "   Blue Essence: " + rs.getInt("blue_essence");
        }
    }
}
