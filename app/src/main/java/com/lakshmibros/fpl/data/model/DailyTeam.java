package com.lakshmibros.fpl.data.model;

import java.util.ArrayList;

public class DailyTeam {
    private int matchNumber;
    private ArrayList<String> team;
    private String captain;
    private String powerPlayer;
    private String winningTeam;

    public DailyTeam(int matchNumber, ArrayList<String> team, String captain, String powerPlayer, String winningTeam) {
        this.matchNumber = matchNumber;
        this.team = team;
        this.captain = captain;
        this.powerPlayer = powerPlayer;
        this.winningTeam = winningTeam;
    }

    public int getMatchNumber() {
        return matchNumber;
    }
    public ArrayList<String> getTeam() { return team; }
    public String getCaptain() {
        return captain;
    }
    public String getPowerPlayer() {
        return powerPlayer;
    }
    public String getWinningTeam() {
        return winningTeam;
    }
}
