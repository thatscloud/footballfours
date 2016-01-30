package com.footballfours.model.table;

public class LeagueTableRow
{
    private int myPosition;
    private String myTeamName;
    private int myMatchesPlayed;
    private int myNumberOfWins;
    private int myNumberOfDraws;
    private int myNumberOfLosses;
    private int myGoalsFor;
    private int myGoalsAgainst;
    private int myGoalDifference;
    private int myPoints;

    public int getPosition()
    {
        return myPosition;
    }

    public void setPosition( final int position )
    {
        myPosition = position;
    }

    public String getTeamName()
    {
        return myTeamName;
    }

    public void setTeamName( final String teamName )
    {
        myTeamName = teamName;
    }

    public int getMatchesPlayed()
    {
        return myMatchesPlayed;
    }

    public void setMatchesPlayed( final int matchesPlayed )
    {
        myMatchesPlayed = matchesPlayed;
    }

    public int getNumberOfWins()
    {
        return myNumberOfWins;
    }

    public void setNumberOfWins( final int numberOfWins )
    {
        myNumberOfWins = numberOfWins;
    }

    public int getNumberOfDraws()
    {
        return myNumberOfDraws;
    }

    public void setNumberOfDraws( final int numberOfDraws )
    {
        myNumberOfDraws = numberOfDraws;
    }

    public int getNumberOfLosses()
    {
        return myNumberOfLosses;
    }

    public void setNumberOfLosses( final int numberOfLosses )
    {
        myNumberOfLosses = numberOfLosses;
    }

    public int getGoalsFor()
    {
        return myGoalsFor;
    }

    public void setGoalsFor( final int goalsFor )
    {
        myGoalsFor = goalsFor;
    }

    public int getGoalsAgainst()
    {
        return myGoalsAgainst;
    }

    public void setGoalsAgainst( final int goalsAgainst )
    {
        myGoalsAgainst = goalsAgainst;
    }

    public int getGoalDifference()
    {
        return myGoalDifference;
    }

    public void setGoalDifference( final int goalDifference )
    {
        myGoalDifference = goalDifference;
    }

    public int getPoints()
    {
        return myPoints;
    }

    public void setPoints( final int points )
    {
        myPoints = points;
    }
}
