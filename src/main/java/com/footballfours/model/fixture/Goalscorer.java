package com.footballfours.model.fixture;

import javax.persistence.Column;

public class Goalscorer
{

    private String myName;
    private int myNumberOfGoals;

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
    public int getNumberOfGoals()
    {
        return myNumberOfGoals;
    }

    public void setNumberOfGoals( final int numberOfGoals )
    {
        myNumberOfGoals = numberOfGoals;
    }

    @Column
    public boolean isMultipleGoals()
    {
        return myNumberOfGoals > 1;
    }
}
