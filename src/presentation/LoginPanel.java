package presentation;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginPanel extends UIPanel {
    private JTextField username;
    private JButton loginButton;
    private JButton registerButton;

    public LoginPanel(Presentation p) {
        super(p);

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("GameDB", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 60));
        titleLabel.setPreferredSize(new Dimension(400, 100));
        this.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        username = new JTextField(15);
        username.setFont(new Font("Arial", Font.PLAIN, 18));
        username.setPreferredSize(new Dimension(300, 40));
        username.setToolTipText("Enter your username");
        formPanel.add(username);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 18));
        loginButton.setPreferredSize(new Dimension(150, 50));
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = username.getText();
                int id = -1;
                try {
                    id = GetPresentation().getDbc().tryLogin(input);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                if (id != -1) {
                    p.setPlayer_id(id);
                    p.setUsername(input);
                    p.SwitchPanel(new PlayerPanel(p));
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this, "User not found", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        formPanel.add(loginButton);

        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 18));
        registerButton.setPreferredSize(new Dimension(150, 50));
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = username.getText();
                try {
                    GetPresentation().getDbc().insertPlayer(input);
                    p.setUsername(input);
                    JOptionPane.showMessageDialog(null, "Username "+ input +" has been registered.");
                    username.setText("");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(LoginPanel.this, "Registration failed", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        formPanel.add(registerButton);
        this.add(formPanel, BorderLayout.CENTER);
    }
}
