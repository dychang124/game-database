package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerPanel extends UIPanel {

    private JLabel username;

    private JButton viewLeaderboard;

    public PlayerPanel(Presentation p, String username)
    {
        super(p);

        this.setLayout(new GridLayout(2,1));

        this.username = new JLabel("Welcome, " + username + "!");
        this.add(this.username);

        viewLeaderboard = new JButton("View Leaderboard");
        viewLeaderboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                p.SwitchPanel(new LeaderboardPanel(p));
            }
        });
        this.add(viewLeaderboard);
    }

}
