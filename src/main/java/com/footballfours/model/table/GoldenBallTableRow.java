package com.footballfours.model.table;

public class GoldenBallTableRow
{
    private int myPosition;
    private String myPlayerName;
    private String myTeamName;
    private int myVotes;

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

    public int getVotes()
    {
        return myVotes;
    }

    public void setVotes( final int votes )
    {
        myVotes = votes;
    }
}
