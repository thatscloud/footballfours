package com.footballfours.batch.pojo;

public class Match
{
    private String mySeason;
    private Integer myRound;
    private Team myHomeTeam;
    private Team myAwayTeam;

    public String getSeason()
    {
        return mySeason;
    }

    public void setSeason( final String season )
    {
        mySeason = season;
    }

    public Integer getRound()
    {
        return myRound;
    }

    public void setRound( final Integer round )
    {
        myRound = round;
    }

    public Team getHomeTeam()
    {
        return myHomeTeam;
    }

    public void setHomeTeam( final Team homeTeam )
    {
        myHomeTeam = homeTeam;
    }

    public Team getAwayTeam()
    {
        return myAwayTeam;
    }

    public void setAwayTeam( final Team awayTeam )
    {
        myAwayTeam = awayTeam;
    }
}
