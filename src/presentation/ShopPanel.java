package presentation;

import application.StringIntTuple;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;


public class ShopPanel extends UIPanel{
    private JLabel ocTitleLabel;
    private JLabel blueEssenceInventory;
    private JPanel listPanel;
    private JButton backButton;

    /**
     * Constructs page that displays a shop where unowned champions can be purchased
     * @param p
     */
    public ShopPanel(Presentation p) {
        super(p);

        this.setLayout(new GridLayout(4,1));
        ocTitleLabel = new JLabel("Shop");
        this.add(ocTitleLabel);

        blueEssenceInventory = new JLabel();
        this.add(blueEssenceInventory);

        listPanel = new JPanel(new GridLayout(10,1));
        this.add(listPanel);

        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            //Go to Player Panel on button click
            public void actionPerformed(ActionEvent e) {
                p.SwitchPanel(new PlayerPanel(p));
            }
        });
        this.add(backButton);

        RefreshShop();
    }

    /**
     * Updates UI to display unowned champions
     */
    public void RefreshShop() {
        listPanel.removeAll();

        try {
            //Queries for list of champions unowned by current user
            ArrayList<StringIntTuple> a = getPresentation().getDbc().getUnOwnedChampions(getPresentation().getPlayer_id());
            for (int i = 0; i < a.size(); i++){
                ShopEntryPanel label = new ShopEntryPanel(a.get(i).getStr(), a.get(i).getNum(), this);
                listPanel.add(label);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        SetBlueEssence();

        this.repaint();
    }

    /**
     * Updates UI to display currency owned by user
     */
    public void SetBlueEssence(){
        try {
            blueEssenceInventory.setText("Blue Essence: " + getPresentation().getDbc().getBlueEssence(getPresentation().getPlayer_id()));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(ShopPanel.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
