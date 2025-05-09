package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class OwnedChampionsPanel extends UIPanel{
    private JLabel ocTitleLabel;
    private JPanel listPanel;
    private JButton backButton;
    private ArrayList<JLabel> ocLabels;
    public OwnedChampionsPanel(Presentation p) {
        super(p);

        this.setLayout(new GridLayout(3,1));
        ocTitleLabel = new JLabel("Owned Champions");
        this.add(ocTitleLabel);

        listPanel = new JPanel(new GridLayout(8,1));
        this.add(listPanel);
        ocLabels = new ArrayList<JLabel>();


        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                p.SwitchPanel(new PlayerPanel(p));
            }
        });
        this.add(backButton);


        RefreshOwnedChampions();
    }

    public void RefreshOwnedChampions(){

        while (!ocLabels.isEmpty()){
            JLabel label = ocLabels.remove(0);
            this.remove(label);
        }

        try {
            ArrayList<String> a = GetPresentation().getDbc().getOwnedChampions(GetPresentation().getPlayer_id());
            for (int i = 0; i < a.size(); i++){
                JLabel label = new JLabel(a.get(i));
                listPanel.add(label);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        this.repaint();
    }


}
