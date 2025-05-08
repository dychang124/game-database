package presentation;

import javax.swing.*;
import java.awt.*;

public class Presentation {
    private Container cp;
    public Presentation(){
        JPanel panel = new LoginPanel(this);
        JFrame frame = new JFrame();
        cp = frame.getContentPane();
        cp.add(panel);
        frame.setSize(500, 300);
        frame.setTitle("GameDB");

        //((PlayerPanel)panel).DisplayText();

        frame.setVisible(true);
    }

    public void SwitchPanel(UIPanel uip){
        cp.removeAll();
        cp.add(uip);
        cp.revalidate();
        cp.repaint();
    }
}
