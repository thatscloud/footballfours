package com.footballfours.model.fixture;

public class Goalscorer
{
    private String myName;
    private int myNumberOfGoals;

    public String getName()
    {
        return myName;
    }

    public void setName( final String name )
    {
        myName = name;
    }

    public int getNumberOfGoals()
    {
        return myNumberOfGoals;
    }

    public void setNumberOfGoals( final int numberOfGoals )
    {
        myNumberOfGoals = numberOfGoals;
    }

    public boolean isMultipleGoals()
    {
        return myNumberOfGoals > 1;
    }
}
