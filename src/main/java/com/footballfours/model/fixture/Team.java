package com.footballfours.model.fixture;

import java.util.List;

public class Team
{
    private String myName;
    private int myGoals;
    private List<Goalscorer> myGoalscorers;

    public String getName()
    {
        return myName;
    }

    public void setName( final String name )
    {
        myName = name;
    }

    public int getGoals()
    {
        return myGoals;
    }

    public void setGoals( final int goals )
    {
        myGoals = goals;
    }

    public List<Goalscorer> getGoalscorers()
    {
        return myGoalscorers;
    }

    public void setGoalscorers( final List<Goalscorer> goalscorers )
    {
        myGoalscorers = goalscorers;
    }
}
