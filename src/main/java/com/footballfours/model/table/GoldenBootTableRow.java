package com.footballfours.model.table;

import javax.persistence.Column;

public class GoldenBootTableRow
{
    private int myPosition;
    private String myPlayerName;
    private String myTeamName;
    private int myGoals;

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
    public String getPlayerName()
    {
        return myPlayerName;
    }

    public void setPlayerName( final String playerName )
    {
        myPlayerName = playerName;
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
    public int getGoals()
    {
        return myGoals;
    }

    public void setGoals( final int goals )
    {
        myGoals = goals;
    }
}
