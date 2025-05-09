package presentation;

import application.CustomException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ShopEntryPanel extends JPanel {
    public ShopEntryPanel(String championName, int cost, ShopPanel parent) {
        super();
        this.setBackground(Color.gray);
        this.setLayout(new GridLayout(1,2));


        JLabel championNameLabel = new JLabel(championName);
        championNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(championNameLabel);
        JButton purchaseLabel = new JButton("Cost: " + cost);
        purchaseLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(purchaseLabel);
        purchaseLabel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    parent.GetPresentation().getDbc().purchaseChampion(parent.GetPresentation().getPlayer_id(), championName);
                    parent.RefreshShop();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(championNameLabel, ex.getMessage(), "Purchase Failed", JOptionPane.ERROR_MESSAGE);
                } catch (CustomException ex){
                    JOptionPane.showMessageDialog(championNameLabel, ex.getMessage(), "Purchase Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
