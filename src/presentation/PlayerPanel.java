package presentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.lang.Math;
import java.sql.SQLException;
import java.awt.*;

public class PlayerPanel extends UIPanel {

    private JLabel usernameLabel;
    private JButton playButton;
    private int matchId = -1;
    private int kills, deaths, assists, gametime;
    private String winLoss;

    private JButton viewLeaderboard;

    private JButton viewMatchHistory;

    private JButton viewChampionsOwned;

    private JButton viewShop;

    private JButton logoutButton;

    public PlayerPanel(Presentation p)
    {
        super(p);

        this.setLayout(new GridLayout(7,1));

        usernameLabel = new JLabel("Welcome, " + p.getUsername() + "!");
        this.add(usernameLabel);


        playButton = new JButton("Play");
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame(p.getUsername());
                if(matchId != -1)
                {
                    /*
                    System.out.println("Game Over:");
                    System.out.println("Win/Loss: " + winLoss);
                    System.out.println("Kills: " + kills);
                    System.out.println("Deaths: " + deaths);
                    System.out.println("Assists: " + assists);
                    System.out.println("Game length: " + gametime + " seconds");*/
                    JOptionPane.showMessageDialog(usernameLabel, "Match Completed\n" + winLoss +
                            "\nKills: " + kills + "\nDeaths: " + deaths + "\nAssists: " + assists + "\nGame length: " + gametime / 60 + " minutes " + gametime % 60 + " seconds");
                }
            }
        });
        this.add(playButton);

        viewLeaderboard = new JButton("View Leaderboard");
        viewLeaderboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GetPresentation().SwitchPanel(new LeaderboardPanel(GetPresentation()));
            }
        });
        this.add(viewLeaderboard);

        viewMatchHistory = new JButton("View Match History");
        viewMatchHistory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GetPresentation().SwitchPanel(new MatchHistoryPanel(GetPresentation()));
            }
        });
        this.add(viewMatchHistory);

        viewChampionsOwned = new JButton("Open Collection");
        viewChampionsOwned.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GetPresentation().SwitchPanel(new OwnedChampionsPanel(GetPresentation()));
            }
        });
        this.add(viewChampionsOwned);

        viewShop = new JButton("Open Shop");
        viewShop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GetPresentation().SwitchPanel(new ShopPanel(GetPresentation()));
            }
        });
        this.add(viewShop);

        logoutButton = new JButton("Log out");
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GetPresentation().setPlayer_id(-1);
                GetPresentation().setUsername(null);
                GetPresentation().SwitchPanel(new LoginPanel(GetPresentation()));
            }
        });
        this.add(logoutButton);
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
            matchId = GetPresentation().getDbc().createMatch("Ranked Solo", gametime);
            int playerId = GetPresentation().getDbc().getPlayerIdByUsername(username);
            GetPresentation().getDbc().insertMatchParticipant(matchId, playerId, rankAwarded, winLoss, kills, deaths, assists);
            System.out.println("Match and stats saved successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
