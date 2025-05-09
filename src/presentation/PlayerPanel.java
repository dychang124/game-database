package presentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.sql.SQLException;
import java.awt.*;

public class PlayerPanel extends UIPanel {

    private JLabel usernameLabel;
    private JLabel profileLabel;

    private JButton playButton;

    private JButton viewLeaderboard;

    private JButton viewMatchHistory;

    private JButton viewChampionsOwned;

    private JButton viewShop;

    private JButton logoutButton;

    private JButton deleteButton;

    public PlayerPanel(Presentation p)
    {
        super(p);

        this.setLayout(new GridLayout(8,1));

        usernameLabel = new JLabel("Welcome, " + p.getUsername() + "!");
        this.add(usernameLabel);

        profileLabel = new JLabel();
        this.add(profileLabel);
        refreshProfileLabel();



        playButton = new JButton("Play");
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startMatch();
            }
        });
        this.add(playButton);

        viewLeaderboard = new JButton("View Leaderboard");
        viewLeaderboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getPresentation().SwitchPanel(new LeaderboardPanel(getPresentation()));
            }
        });
        this.add(viewLeaderboard);

        viewMatchHistory = new JButton("View Match History");
        viewMatchHistory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getPresentation().SwitchPanel(new MatchHistoryPanel(getPresentation()));
            }
        });
        this.add(viewMatchHistory);

        viewChampionsOwned = new JButton("Open Collection");
        viewChampionsOwned.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getPresentation().SwitchPanel(new OwnedChampionsPanel(getPresentation()));
            }
        });
        this.add(viewChampionsOwned);

        viewShop = new JButton("Open Shop");
        viewShop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getPresentation().SwitchPanel(new ShopPanel(getPresentation()));
            }
        });
        this.add(viewShop);

        logoutButton = new JButton("Log out");
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getPresentation().setPlayer_id(-1);
                getPresentation().setUsername(null);
                getPresentation().SwitchPanel(new LoginPanel(getPresentation()));
            }
        });
        this.add(logoutButton);

        deleteButton = new JButton("Delete Account");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    getPresentation().getDbc().deletePlayer(getPresentation().getPlayer_id());
                }catch(Exception error)
                {
                    error.getMessage();
                }
                getPresentation().setPlayer_id(-1);
                getPresentation().setUsername(null);
                getPresentation().SwitchPanel(new LoginPanel(getPresentation()));
            }
        });
        this.add(deleteButton);
    }

    public void startMatch(){
        try {
            JOptionPane.showMessageDialog(usernameLabel, getPresentation().getDbc().playMatch(getPresentation().getPlayer_id()));
        }catch (SQLException e){
            JOptionPane.showMessageDialog(usernameLabel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        refreshProfileLabel();
    }

    public void refreshProfileLabel(){
        try {
            this.profileLabel.setText(getPresentation().getDbc().selectPlayerProfile(getPresentation().getPlayer_id()));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
