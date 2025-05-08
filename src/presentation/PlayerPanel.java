package presentation;

import javax.swing.*;

public class PlayerPanel extends JPanel {

    private JLabel username;

    public PlayerPanel()
    {
        super();
        username = new JLabel();
        this.add(username);
    }

    public void DisplayText() {
        this.username.setText("Welcome ");
        this.repaint();
    }
}
