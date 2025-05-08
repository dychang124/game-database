package application.src.main.java.com.example;
import java.sql.*;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        DBConnection dbc = new DBConnection();
        dbc.InsertPlayer("playerTwo");
        dbc.PrintPlayers();
    }

}
