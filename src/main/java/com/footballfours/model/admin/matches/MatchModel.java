package com.footballfours.model.admin.matches;

public class MatchModel
{
    private String myScheduledDateTimeString;
    private String myPlayedDateTimeString;
    private int myRoundNumber;
    private String myHomeTeam;
    private String myAwayTeam;

    public String getScheduledDateTimeString()
    {
        return myScheduledDateTimeString;
    }

    public void setScheduledDateTimeString( final String scheduledDateTimeString )
    {
        myScheduledDateTimeString = scheduledDateTimeString;
    }

    public String getPlayedDateTimeString()
    {
        return myPlayedDateTimeString;
    }

    public void setPlayedDateTimeString( final String playedDateTimeString )
    {
        myPlayedDateTimeString = playedDateTimeString;
    }

    public int getRoundNumber()
    {
        return myRoundNumber;
    }

    public void setRoundNumber( final int roundNumber )
    {
        myRoundNumber = roundNumber;
    }

    public String getHomeTeam()
    {
        return myHomeTeam;
    }

    public void setHomeTeam( final String homeTeam )
    {
        myHomeTeam = homeTeam;
    }

    public String getAwayTeam()
    {
        return myAwayTeam;
    }

    public void setAwayTeam( final String awayTeam )
    {
        myAwayTeam = awayTeam;
    }
}
