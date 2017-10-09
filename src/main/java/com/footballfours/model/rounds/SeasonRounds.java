package com.footballfours.model.rounds;

import java.util.UUID;

public class SeasonRounds
{
    private String mySeasonName;
    private UUID mySeasonId;
    private int myRounds;

    public String getSeasonName()
    {
        return mySeasonName;
    }
    public void setSeasonName( final String seasonName )
    {
        mySeasonName = seasonName;
    }
    public UUID getSeasonId()
    {
        return mySeasonId;
    }
    public void setSeasonId( final UUID seasonId )
    {
        mySeasonId = seasonId;
    }
    public int getRounds()
    {
        return myRounds;
    }
    public void setRounds( final int rounds )
    {
        myRounds = rounds;
    }
}
