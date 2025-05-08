package presentation;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends UIPanel {
    private JTextField username;

    public LoginPanel(Presentation p)
    {
        super(p);

        username = new JTextField(15);
        username.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = username.getText();
                System.out.println(input);
                p.SwitchPanel(new PlayerPanel(p, input));
            }
        });

        this.add(username);
    }
}
