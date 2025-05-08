package presentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.lang.Math;
import java.sql.SQLException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerPanel extends UIPanel {

    private JLabel usernameLabel;
    private JButton playButton;
    private int matchId = -1;
    private int kills, deaths, assists, gametime;
    private String winLoss;

    private JButton viewLeaderboard;

    private JButton viewChampionsOwned;

    private JButton viewShop;

    private JButton logoutButton;

    public PlayerPanel(Presentation p, String username)
    {
        super(p);

        this.setLayout(new GridLayout(7,1));

        usernameLabel = new JLabel("Welcome, " + p.getUsername() + "!");
        this.add(usernameLabel);


        playButton = new JButton("Play");
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame(username);
                if(matchId != -1)
                {
                    System.out.println("Game Over:");
                    System.out.println("Win/Loss: " + winLoss);
                    System.out.println("Kills: " + kills);
                    System.out.println("Deaths: " + deaths);
                    System.out.println("Assists: " + assists);
                    System.out.println("Game length: " + gametime + " seconds");
                }
            }
        });
        this.add(playButton);
    }

    private void startGame(String username)
    {
        kills = (int)(Math.random()*10);
        deaths = (int)(Math.random()*10);
        assists = (int)(Math.random()*10);
        gametime = (int)(Math.random()*10000);
        winLoss = kills > deaths ? "Win" : "Loss";
        int rankAwarded = winLoss.equals("Win") ? 1 : -1;

        try {
            matchId = getMain().getDbc().createMatch("Ranked Solo", gametime);
            int playerId = getMain().getDbc().getPlayerIdByUsername(username);
            getMain().getDbc().insertMatchParticipant(matchId, playerId, rankAwarded, winLoss, kills, deaths, assists);
            System.out.println("Match and stats saved successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }


        viewLeaderboard = new JButton("View Leaderboard");
        viewLeaderboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                p.SwitchPanel(new LeaderboardPanel(p));
            }
        });
        this.add(viewLeaderboard);

        viewChampionsOwned = new JButton("Open Collection");
        viewChampionsOwned.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                p.SwitchPanel(new OwnedChampionsPanel(p));
            }
        });
        this.add(viewChampionsOwned);

        viewShop = new JButton("Open Shop");
        viewShop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                p.SwitchPanel(new ShopPanel(p));
            }
        });
        this.add(viewShop);

        logoutButton = new JButton("Log out");
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                p.setPlayer_id(-1);
                p.setUsername(null);
                p.SwitchPanel(new LoginPanel(p));
            }
        });
        this.add(logoutButton);
    }

}
