package presentation;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import application.DBConnection;

public class Presentation {
    private Container cp;
    private DBConnection dbc;

    private int player_id;
    private String username;

    public Presentation(){

        dbc = new DBConnection();

        JPanel panel = new LoginPanel(this);
        JFrame frame = new JFrame();
        cp = frame.getContentPane();
        cp.add(panel);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setTitle("GameDB");

        frame.setVisible(true);
    }

    public void SwitchPanel(UIPanel uip){
        cp.removeAll();
        cp.add(uip);
        cp.revalidate();
        cp.repaint();
    }

    public DBConnection getDbc() {
        return dbc;
    }

    public int getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(int player_id) {
        this.player_id = player_id;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }
}
