package presentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.sql.SQLException;
import java.awt.*;

public class PlayerPanel extends UIPanel {

    private JLabel usernameLabel;
    private JLabel levelLabel;

    private JButton playButton;

    private JButton viewLeaderboard;

    private JButton viewMatchHistory;

    private JButton viewChampionsOwned;

    private JButton viewShop;

    private JButton logoutButton;

    public PlayerPanel(Presentation p)
    {
        super(p);

        this.setLayout(new GridLayout(8,1));

        usernameLabel = new JLabel("Welcome, " + p.getUsername() + "!");
        this.add(usernameLabel);

        levelLabel = new JLabel();
        this.add(levelLabel);
        refreshLevelLabel();

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

    public void startMatch(){
        try {
            JOptionPane.showMessageDialog(usernameLabel, GetPresentation().getDbc().playMatch(GetPresentation().getPlayer_id()));
        }catch (SQLException e){
            JOptionPane.showMessageDialog(usernameLabel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        refreshLevelLabel();
    }

    public void refreshLevelLabel(){
        try {
            this.levelLabel.setText("Level " + GetPresentation().getDbc().selectLevel(GetPresentation().getPlayer_id()));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
