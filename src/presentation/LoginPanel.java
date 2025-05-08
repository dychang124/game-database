package presentation;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginPanel extends UIPanel {
    private JTextField username;
    private JButton loginButton;
    private JButton registerButton;

    public LoginPanel(Presentation p)
    {
        super(p);

        username = new JTextField(15);
        this.add(username);

        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = username.getText();
                boolean hasUser = false;
                try {
                    hasUser = getMain().getDbc().TryLogin(input);
                }catch (SQLException ex){
                    ex.printStackTrace();
                }
                if (hasUser) {
                    p.SwitchPanel(new PlayerPanel(p, input));
                }else {
                    System.out.println("No user found");
                }
            }
        });
        this.add(loginButton);

        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = username.getText();
                try {
                    getMain().getDbc().InsertPlayer(input);
                }catch (SQLException ex){
                    ex.printStackTrace();
                }
                p.SwitchPanel(new PlayerPanel(p, input));
            }
        });
        this.add(registerButton);
    }
}
