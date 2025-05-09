package application;

import java.sql.Timestamp;

public class MatchHistoryStruct {
    public Timestamp timestamp;
    public String wl;
    public String opponent;
    public int kills;
    public int deaths;
    public int assists;
    public int gameLength;

    public MatchHistoryStruct(Timestamp timestamp, String wl, String opponent, int kills, int deaths, int assists, int gameLength){
        this.timestamp = timestamp;
        this.wl = wl;
        this.opponent = opponent;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.gameLength = gameLength;
    }
}


