package presentation;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import application.DBConnection;

public class Presentation {
    private Container cp;
    private DBConnection dbc;

    public Presentation(){

        dbc = new DBConnection();

        JPanel panel = new LoginPanel(this);
        JFrame frame = new JFrame();
        cp = frame.getContentPane();
        cp.add(panel);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
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
}
