package presentation;
import application.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Main
{
    public static void main( String[] args )
    {
        DBConnection dbc = new DBConnection();
        dbc.InsertPlayer("playerTwo");
        dbc.PrintPlayers();

        JPanel panel = new PlayerPanel();
        JFrame frame = new JFrame();
        Container cp = frame.getContentPane();
        cp.add(panel);
        frame.setSize(500, 300);
        frame.setTitle("GameDB");

        ((PlayerPanel)panel).DisplayText();

        frame.setVisible(true);
    }

}
