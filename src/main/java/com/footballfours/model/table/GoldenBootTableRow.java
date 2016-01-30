package com.footballfours.model.table;

public class GoldenBootTableRow
{
    private int myPosition;
    private String myPlayerName;
    private String myTeamName;
    private int myGoals;

    public int getPosition()
    {
        return myPosition;
    }

    public void setPosition( final int position )
    {
        myPosition = position;
    }

    public String getPlayerName()
    {
        return myPlayerName;
    }

    public void setPlayerName( final String playerName )
    {
        myPlayerName = playerName;
    }

    public String getTeamName()
    {
        return myTeamName;
    }

    public void setTeamName( final String teamName )
    {
        myTeamName = teamName;
    }

    public int getGoals()
    {
        return myGoals;
    }

    public void setGoals( final int goals )
    {
        myGoals = goals;
    }
}
