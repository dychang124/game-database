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

    /**
     * Constructs page that displays a list of champions the user owns
     * @param p
     */
    public OwnedChampionsPanel(Presentation p) {
        super(p);

        this.setLayout(new GridLayout(3,1));
        ocTitleLabel = new JLabel("Owned Champions");
        this.add(ocTitleLabel);

        listPanel = new JPanel(new GridLayout(8,1));
        this.add(listPanel);

        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Go to Player Panel when button clicked
                p.SwitchPanel(new PlayerPanel(p));
            }
        });
        this.add(backButton);


        RefreshOwnedChampions();
    }

    /**
     * Updates UI to display list of owned champions
     */
    public void RefreshOwnedChampions(){
        try {
            //Queries for champion names owned by current user
            ArrayList<String> a = getPresentation().getDbc().getOwnedChampions(getPresentation().getPlayer_id());
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
