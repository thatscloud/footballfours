package com.footballfours.model.fixture;

import java.util.List;

import javax.persistence.Column;

public class Round
{
    private int myRoundNumber;
    private List<Match> myMatches;

    @Column
    public int getRoundNumber()
    {
        return myRoundNumber;
    }

    public void setRoundNumber( final int roundNumber )
    {
        myRoundNumber = roundNumber;
    }

    @Column
    public List<Match> getMatches()
    {
        return myMatches;
    }

    public void setMatches( final List<Match> matches )
    {
        myMatches = matches;
    }
}
