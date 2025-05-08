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
    private JPanel listPanel;
    private JButton backButton;
    private ArrayList<ShopEntryPanel> shopLabels;
    public ShopPanel(Presentation p) {
        super(p);

        this.setLayout(new GridLayout(3,1));
        ocTitleLabel = new JLabel("Shop");
        listPanel = new JPanel(new GridLayout(10,1));
        this.add(listPanel);
        listPanel.add(ocTitleLabel);
        shopLabels = new ArrayList<ShopEntryPanel>();


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
        while (!shopLabels.isEmpty()){
            shopLabels.remove(0);
        }

        try {
            ArrayList<StringIntTuple> a = getMain().getDbc().GetUnOwnedChampions(getMain().getPlayer_id());
            for (int i = 0; i < a.size(); i++){
                ShopEntryPanel label = new ShopEntryPanel(a.get(i).getStr(), a.get(i).getNum());
                this.add(label);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        this.repaint();
    }
}
