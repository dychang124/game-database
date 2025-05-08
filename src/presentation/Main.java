package presentation;
import application.*;


public class Main
{
    public static void main( String[] args )
    {
        DBConnection dbc = new DBConnection();
        dbc.InsertPlayer("playerTwo");
        dbc.PrintPlayers();
    }

}
