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

    /**
     * Constructs main landing page after logging in, with buttons for various gameplay functions. Displays some player information.
     * @param p
     */
    public PlayerPanel(Presentation p)
    {
        super(p);

        this.setLayout(new GridLayout(9,1));

        usernameLabel = new JLabel("Welcome, " + p.getUsername() + "!");
        this.add(usernameLabel);

        profileLabel = new JLabel();
        this.add(profileLabel);
        refreshProfileLabel();

        playButton = new JButton("Play");
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Simulate playing a match and updates database accordingly
                startMatch();
            }
        });
        this.add(playButton);

        viewLeaderboard = new JButton("View Leaderboard");
        viewLeaderboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Go to Leaderboard Panel on button click
                getPresentation().SwitchPanel(new LeaderboardPanel(getPresentation()));
            }
        });
        this.add(viewLeaderboard);

        viewMatchHistory = new JButton("View Match History");
        viewMatchHistory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Go to Match History Panel on button click
                getPresentation().SwitchPanel(new MatchHistoryPanel(getPresentation()));
            }
        });
        this.add(viewMatchHistory);

        viewChampionsOwned = new JButton("Open Collection");
        viewChampionsOwned.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Go to Owned Champions Panel on button click
                getPresentation().SwitchPanel(new OwnedChampionsPanel(getPresentation()));
            }
        });
        this.add(viewChampionsOwned);

        viewShop = new JButton("Open Shop");
        viewShop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Go to Shop Panel on button click
                getPresentation().SwitchPanel(new ShopPanel(getPresentation()));
            }
        });
        this.add(viewShop);

        logoutButton = new JButton("Log Out");
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Log out and go to Login Panel on button click
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
                    //Deletes player from database
                    getPresentation().getDbc().deletePlayer(getPresentation().getPlayer_id());

                    //Go to Login Panel
                    getPresentation().setPlayer_id(-1);
                    getPresentation().setUsername(null);
                    getPresentation().SwitchPanel(new LoginPanel(getPresentation()));

                }catch(Exception error)
                {
                    JOptionPane.showMessageDialog(usernameLabel, error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        this.add(deleteButton);
    }

    /**
     * Simulates a played match with a random opponent and updates database accordingly. Displays a message of match results.
     */
    public void startMatch(){
        try {
            //Send request to backend
            JOptionPane.showMessageDialog(this, getPresentation().getDbc().playMatch(getPresentation().getPlayer_id()));
        }catch (SQLException e){
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        refreshProfileLabel();
    }

    /**
     * Updates UI to show player profile data
     */
    public void refreshProfileLabel(){
        try {
            //Queries for profile data and updates UI
            this.profileLabel.setText(getPresentation().getDbc().selectPlayerProfile(getPresentation().getPlayer_id()));
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
