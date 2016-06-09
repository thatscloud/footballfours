package com.footballfours.model.table;

import javax.persistence.Column;

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

    @Column
    public int getPosition()
    {
        return myPosition;
    }

    public void setPosition( final int position )
    {
        myPosition = position;
    }

    @Column
    public String getTeamName()
    {
        return myTeamName;
    }

    public void setTeamName( final String teamName )
    {
        myTeamName = teamName;
    }

    @Column
    public int getMatchesPlayed()
    {
        return myMatchesPlayed;
    }

    public void setMatchesPlayed( final int matchesPlayed )
    {
        myMatchesPlayed = matchesPlayed;
    }

    @Column
    public int getNumberOfWins()
    {
        return myNumberOfWins;
    }

    public void setNumberOfWins( final int numberOfWins )
    {
        myNumberOfWins = numberOfWins;
    }

    @Column
    public int getNumberOfDraws()
    {
        return myNumberOfDraws;
    }

    public void setNumberOfDraws( final int numberOfDraws )
    {
        myNumberOfDraws = numberOfDraws;
    }

    @Column
    public int getNumberOfLosses()
    {
        return myNumberOfLosses;
    }

    public void setNumberOfLosses( final int numberOfLosses )
    {
        myNumberOfLosses = numberOfLosses;
    }

    @Column
    public int getGoalsFor()
    {
        return myGoalsFor;
    }

    public void setGoalsFor( final int goalsFor )
    {
        myGoalsFor = goalsFor;
    }

    @Column
    public int getGoalsAgainst()
    {
        return myGoalsAgainst;
    }

    public void setGoalsAgainst( final int goalsAgainst )
    {
        myGoalsAgainst = goalsAgainst;
    }

    @Column
    public int getGoalDifference()
    {
        return myGoalDifference;
    }

    public void setGoalDifference( final int goalDifference )
    {
        myGoalDifference = goalDifference;
    }

    @Column
    public int getPoints()
    {
        return myPoints;
    }

    @Column
    public void setPoints( final int points )
    {
        myPoints = points;
    }
}
