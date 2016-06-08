package com.footballfours.model.table;

import javax.persistence.Column;

public class GoldenBallTableRow
{
    private int myPosition;
    private String myPlayerName;
    private String myTeamName;
    private int myVotes;

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
    public int getVotes()
    {
        return myVotes;
    }

    public void setVotes( final int votes )
    {
        myVotes = votes;
    }
}
