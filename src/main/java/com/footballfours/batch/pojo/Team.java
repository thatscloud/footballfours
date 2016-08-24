package com.footballfours.batch.pojo;

import java.util.List;

public class Team
{
    private String myName;
    private List<String> myPlayers;
    private List<Goal> myGoals;
    private Votes myVotes;

    public String getName()
    {
        return myName;
    }

    public void setName( final String name )
    {
        myName = name;
    }

    public List<String> getPlayers()
    {
        return myPlayers;
    }

    public void setPlayers( final List<String> players )
    {
        myPlayers = players;
    }

    public List<Goal> getGoals()
    {
        return myGoals;
    }

    public void setGoals( final List<Goal> goals )
    {
        myGoals = goals;
    }

    public Votes getVotes()
    {
        return myVotes;
    }

    public void setVotes( final Votes votes )
    {
        myVotes = votes;
    }
}
