package com.lakshmibros.fpl.data.model;

public class Player {
    private String name;
    private String team;
    private String role;
    private String country;
    private int isUncapped;

    public Player(String name, String team, String role, String country, int isUncapped) {
        this.name = name;
        this.team = team;
        this.role = role;
        this.country = country;
        this.isUncapped = isUncapped;
    }

    public String getName() {
        return name;
    }
    public String getTeam() { return team; }
    public String getRole() {
        return role;
    }
    public String getCountry() { return country; }
    public int isUncapped() { return isUncapped; }
}
