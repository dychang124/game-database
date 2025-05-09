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
            public void actionPerformed(ActionEvent e) {
                p.SwitchPanel(new PlayerPanel(p));
            }
        });
        this.add(backButton);

        RefreshShop();
    }

    public void RefreshShop() {
        listPanel.removeAll();

        try {
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

    public void SetBlueEssence(){
        try {
            blueEssenceInventory.setText("Blue Essence: " + getPresentation().getDbc().getBlueEssence(getPresentation().getPlayer_id()));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(ShopPanel.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
