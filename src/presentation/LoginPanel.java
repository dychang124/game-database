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
                int id = -1;
                try {
                    id = getMain().getDbc().TryLogin(input);
                }catch (SQLException ex){
                    ex.printStackTrace();
                }
                if (id != -1) {
                    p.setPlayer_id(id);
                    p.setUsername(input);
                    p.SwitchPanel(new PlayerPanel(p));
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

                    //p.setPlayer_id(id);
                    p.setUsername(input);
                    p.SwitchPanel(new PlayerPanel(p));

                }catch (SQLException ex){
                    ex.printStackTrace();
                }

            }
        });
        this.add(registerButton);
    }
}
