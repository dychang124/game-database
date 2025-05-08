package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class LeaderboardPanel extends UIPanel
{
    private JLabel leaderboardTitleLabel;
    private ArrayList<JLabel> leaderboardLabels;
    private JLabel parentPanel;

    public LeaderboardPanel(Presentation p) {
        super(p);
        this.setLayout(new BorderLayout());
        leaderboardTitleLabel = new JLabel("Leaderboard", SwingConstants.CENTER);
        this.add(leaderboardTitleLabel, BorderLayout.NORTH);
        leaderboardLabels = new ArrayList<JLabel>();
        parentPanel = new JLabel();
        parentPanel.setLayout(new GridLayout(21,1));
        this.add(parentPanel, BorderLayout.CENTER);

        JButton back = new JButton("Back");
        this.add(back, BorderLayout.SOUTH);
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                p.SwitchPanel(new PlayerPanel(p));
            }
        });
        SetLeaderboard();
    }

    public void SetLeaderboard(){

        while (leaderboardLabels.size() > 0){
            JLabel label = leaderboardLabels.remove(0);
            this.remove(label);
        }

        JLabel temp = new JLabel();
        temp.setLayout(new GridLayout(1,2));
        temp.add(new JLabel("Username", SwingConstants.CENTER));
        temp.add(new JLabel("Rank Points", SwingConstants.CENTER));
        parentPanel.add(temp);
        leaderboardLabels.add(temp);

        try {
            ArrayList<ArrayList<String>> a = GetPresentation().getDbc().GetLeaderboard();
            for (int i = 0; i < a.size(); i++){
                temp = new JLabel();
                temp.setLayout(new GridLayout(1,2));
                temp.add(new JLabel(a.get(i).get(0), SwingConstants.CENTER));
                temp.add(new JLabel(a.get(i).get(1), SwingConstants.CENTER));
                parentPanel.add(temp);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        this.repaint();
    }
}
