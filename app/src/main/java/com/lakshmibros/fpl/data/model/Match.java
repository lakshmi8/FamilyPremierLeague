package com.lakshmibros.fpl.data.model;

public class Match {
    private int matchNumber;
    private String date;
    private String team1;
    private String team2;
    private String venue;

    public Match(int matchNumber, String date, String team1, String team2, String venue) {
        this.matchNumber = matchNumber;
        this.date = date;
        this.team1 = team1;
        this.team2 = team2;
        this.venue = venue;
    }

    public int getMatchNumber() {
        return matchNumber;
    }
    public String getDate() {
        return date;
    }
    public String getTeam1() { return team1; }
    public String getTeam2() {
        return team2;
    }
    public String getVenue() { return venue; }
}
