package presentation;

import javax.swing.*;

public class PlayerPanel extends UIPanel {

    private JLabel username;

    public PlayerPanel(Presentation p, String username)
    {
        super(p);
        this.username = new JLabel("Welcome, " + username + "!");
        this.add(this.username);
    }

}
