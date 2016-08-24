package com.footballfours.batch.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Votes
{
    private String myThreeVotesPlayer;
    private String myTwoVotesPlayer;
    private String myOneVotePlayer;

    @JsonProperty( "3" )
    public String getThreeVotesPlayer()
    {
        return myThreeVotesPlayer;
    }

    public void setThreeVotesPlayer( final String threeVotesPlayer )
    {
        myThreeVotesPlayer = threeVotesPlayer;
    }

    @JsonProperty( "2" )
    public String getTwoVotesPlayer()
    {
        return myTwoVotesPlayer;
    }

    public void setTwoVotesPlayer( final String twoVotesPlayer )
    {
        myTwoVotesPlayer = twoVotesPlayer;
    }

    @JsonProperty( "1" )
    public String getOneVotePlayer()
    {
        return myOneVotePlayer;
    }

    public void setOneVotePlayer( final String oneVotePlayer )
    {
        myOneVotePlayer = oneVotePlayer;
    }
}
