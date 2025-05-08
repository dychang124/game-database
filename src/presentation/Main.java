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

        Presentation p = new Presentation();
    }

}
