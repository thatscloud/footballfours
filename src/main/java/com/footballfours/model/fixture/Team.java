package com.footballfours.model.fixture;

import java.util.List;

import javax.persistence.Column;

public class Team
{
    private String myName;
    private int myGoals;
    private List<Goalscorer> myGoalscorers;

    @Column
    public String getName()
    {
        return myName;
    }

    public void setName( final String name )
    {
        myName = name;
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

    @Column
    public List<Goalscorer> getGoalscorers()
    {
        return myGoalscorers;
    }

    public void setGoalscorers( final List<Goalscorer> goalscorers )
    {
        myGoalscorers = goalscorers;
    }
}
