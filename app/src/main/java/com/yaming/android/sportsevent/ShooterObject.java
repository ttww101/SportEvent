package com.yaming.android.sportsevent;

public class ShooterObject {
    private String eventName,sportName,season,seasonCn,eventId,sportId,teamName,playerName,rank,goal,penalty;

    public ShooterObject() {
        this.eventName = "";
        this.sportName = "";
        this.season = "";
        this.seasonCn = "";
        this.eventId = "";
        this.sportId = "";
        this.teamName = "";
        this.playerName = "";
        this.rank = "";
        this.goal = "";
        this.penalty = "";
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getSeasonCn() {
        return seasonCn;
    }

    public void setSeasonCn(String seasonCn) {
        this.seasonCn = seasonCn;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getSportId() {
        return sportId;
    }

    public void setSportId(String sportId) {
        this.sportId = sportId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getPenalty() {
        return penalty;
    }

    public void setPenalty(String penalty) {
        this.penalty = penalty;
    }
}
