package presentation;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends UIPanel {
    private JTextField username;
    private JButton loginButton;
    private JButton registerButton;

    /**
     * Constructs login page with login and register buttons
     * @param p
     */
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

        //Format Login button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 18));
        loginButton.setPreferredSize(new Dimension(150, 50));
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                //Attempt to login as username when button clicked
                String input = username.getText();
                int id = -1;
                try {
                    //Database query
                    id = getPresentation().getDbc().tryLogin(input);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LoginPanel.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                if (id != -1) {
                    p.setPlayer_id(id);
                    p.setUsername(input);
                    p.SwitchPanel(new PlayerPanel(p));
                }
            }
        });
        formPanel.add(loginButton);

        //Format Register button
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 18));
        registerButton.setPreferredSize(new Dimension(150, 50));
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                //Attempt to register a new account as username when button clicked
                String input = username.getText();
                try {
                    //Database query
                    p.getDbc().insertPlayer(input);
                    p.setUsername(input);
                    JOptionPane.showMessageDialog(LoginPanel.this, "Username "+ input +" has been registered.");
                    //username.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LoginPanel.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        formPanel.add(registerButton);
        this.add(formPanel, BorderLayout.CENTER);
    }
}
