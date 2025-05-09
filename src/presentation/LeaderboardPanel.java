package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class LeaderboardPanel extends UIPanel
{
    private JLabel leaderboardTitleLabel;
    private JLabel parentPanel;

    public LeaderboardPanel(Presentation p) {
        super(p);
        this.setLayout(new BorderLayout());
        leaderboardTitleLabel = new JLabel("Leaderboard", SwingConstants.CENTER);
        this.add(leaderboardTitleLabel, BorderLayout.NORTH);

        parentPanel = new JLabel();
        parentPanel.setLayout(new GridLayout(16,1));
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

        JLabel temp = new JLabel();
        temp.setLayout(new GridLayout(1,2));
        temp.add(new JLabel("Username", SwingConstants.CENTER));
        temp.add(new JLabel("Rank Points", SwingConstants.CENTER));
        parentPanel.add(temp);

        try {
            ArrayList<ArrayList<String>> a = getPresentation().getDbc().getLeaderboard();
            for (int i = 0; i < a.size() && i < 15; i++){
                temp = new JLabel();
                temp.setLayout(new GridLayout(1,2));
                JLabel t1 = new JLabel(a.get(i).get(0), SwingConstants.CENTER);
                JLabel t2 = new JLabel(a.get(i).get(1), SwingConstants.CENTER);
                if (Objects.equals(getPresentation().getUsername(), t1.getText())){
                    t1.setForeground(Color.BLUE);
                    t2.setForeground(Color.BLUE);
                }
                temp.add(t1);
                temp.add(t2);
                parentPanel.add(temp);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        this.repaint();
    }
}
