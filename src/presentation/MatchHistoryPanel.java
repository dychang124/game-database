package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class MatchHistoryPanel extends UIPanel
{
    private JLabel matchHistoryTitleLabel;
    private ArrayList<JLabel> matchHistoryLabels;
    private JLabel parentPanel;

    public MatchHistoryPanel(Presentation p) {
        super(p);
        this.setLayout(new BorderLayout());
        matchHistoryTitleLabel = new JLabel("Match History", SwingConstants.CENTER);
        this.add(matchHistoryTitleLabel, BorderLayout.NORTH);
        matchHistoryLabels = new ArrayList<JLabel>();
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
        SetMatchHistory();
    }

    public void SetMatchHistory(){

        while (matchHistoryLabels.size() > 0){
            JLabel label = matchHistoryLabels.remove(0);
            this.remove(label);
        }

        JLabel temp = new JLabel();
        temp.setLayout(new GridLayout(1,6));
        temp.add(new JLabel("Date", SwingConstants.CENTER));
        temp.add(new JLabel("", SwingConstants.CENTER));
        temp.add(new JLabel("Opponent", SwingConstants.CENTER));
        temp.add(new JLabel("K", SwingConstants.CENTER));
        temp.add(new JLabel("D", SwingConstants.CENTER));
        temp.add(new JLabel("A", SwingConstants.CENTER));
        temp.add(new JLabel("Game Time", SwingConstants.CENTER));
        parentPanel.add(temp);
        matchHistoryLabels.add(temp);
/*
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
*/
        this.repaint();
    }
}
