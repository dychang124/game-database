package presentation;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LeaderboardPanel extends UIPanel
{
    private JLabel leaderboardTitleLabel;
    private ArrayList<JLabel> leaderboardLabels;
    public LeaderboardPanel(Presentation p) {
        super(p);
        this.setLayout(new GridLayout(11,1));
        leaderboardTitleLabel = new JLabel("Leaderboard");
        this.add(leaderboardTitleLabel);
        leaderboardLabels = new ArrayList<JLabel>();
        SetLeaderboard();
    }

    public void SetLeaderboard(){

        while (leaderboardLabels.size() > 0){
            JLabel label = leaderboardLabels.remove(0);
            this.remove(label);
        }

        try {
            ArrayList<String> a = getMain().getDbc().GetLeaderboard();
            for (int i = 0; i < a.size(); i++){
                JLabel label = new JLabel(a.get(i));
                this.add(label);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        this.repaint();
    }
}
