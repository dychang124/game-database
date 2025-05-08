package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerPanel extends UIPanel {

    private JLabel username;

    private JButton viewLeaderboard;

    private JButton viewChampionsOwned;

    private JButton viewShop;

    public PlayerPanel(Presentation p)
    {
        super(p);

        this.setLayout(new GridLayout(4,1));

        this.username = new JLabel("Welcome, " + p.getUsername() + "!");
        this.add(this.username);

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
    }

}
