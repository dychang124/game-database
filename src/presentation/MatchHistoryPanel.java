package presentation;

import application.MatchHistoryStruct;

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

    /**
     * Constructs page to display a user's match history
     * @param p
     */
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
            //Go to Player Panel when button clicked
            public void actionPerformed(ActionEvent e) {
                p.SwitchPanel(new PlayerPanel(p));
            }
        });
        SetMatchHistory();
    }

    /**
     * Updates UI to display a user's match history
     */
    public void SetMatchHistory(){

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

        try {
            //Queries for match history of current user
            ArrayList<MatchHistoryStruct> a = getPresentation().getDbc().getMatchHistory(getPresentation().getPlayer_id());
            for (int i = 0; i < a.size(); i++){
                temp = new JLabel();
                temp.setLayout(new GridLayout(1,6));
                temp.add(new JLabel(a.get(i).timestamp.toString(), SwingConstants.CENTER));
                temp.add(new JLabel(a.get(i).wl, SwingConstants.CENTER));
                temp.add(new JLabel(a.get(i).opponent, SwingConstants.CENTER));
                temp.add(new JLabel(a.get(i).kills + "", SwingConstants.CENTER));
                temp.add(new JLabel(a.get(i).kills + "", SwingConstants.CENTER));
                temp.add(new JLabel(a.get(i).kills + "", SwingConstants.CENTER));
                temp.add(new JLabel(a.get(i).gameLength / 60 + ":" + a.get(i).gameLength % 60, SwingConstants.CENTER));
                parentPanel.add(temp);
                matchHistoryLabels.add(temp);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        this.repaint();
    }
}
