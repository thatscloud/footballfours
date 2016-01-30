package com.footballfours.model.fixture;

import java.util.List;

public class Round
{
    private int myRoundNumber;
    private List<Match> myMatches;

    public int getRoundNumber()
    {
        return myRoundNumber;
    }

    public void setRoundNumber( final int roundNumber )
    {
        myRoundNumber = roundNumber;
    }

    public List<Match> getMatches()
    {
        return myMatches;
    }

    public void setMatches( final List<Match> matches )
    {
        myMatches = matches;
    }
}
