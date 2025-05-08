package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShopEntryPanel extends JPanel {
    public ShopEntryPanel(String championName, int cost) {
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
                //
            }
        });
    }
}
