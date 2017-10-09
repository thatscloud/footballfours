package com.footballfours.model.admin.matches;

import java.util.UUID;

public class RoundModel
{
    private UUID myRoundId;
    private int myRoundNumber;

    public UUID getRoundId()
    {
        return myRoundId;
    }

    public void setRoundId( final UUID roundId )
    {
        myRoundId = roundId;
    }

    public int getRoundNumber()
    {
        return myRoundNumber;
    }

    public void setRoundNumber( final int roundNumber )
    {
        myRoundNumber = roundNumber;
    }
}
