package com.footballfours.batch.pojo;

public class Goal
{
    private Boolean myOpponentOwnGoal;
    private String myScorer;

    public Boolean getOpponentOwnGoal()
    {
        return myOpponentOwnGoal;
    }

    public void setOpponentOwnGoal( final Boolean opponentOwnGoal )
    {
        myOpponentOwnGoal = opponentOwnGoal;
    }

    public String getScorer()
    {
        return myScorer;
    }

    public void setScorer( final String scorer )
    {
        myScorer = scorer;
    }
}
