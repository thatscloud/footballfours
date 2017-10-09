package com.footballfours.model.admin.teams;

import java.util.List;

import com.footballfours.model.admin.common.SeasonModel;

public class SeasonTeamsModel
{
    private SeasonModel mySeason;
    private List<String> myTeams;

    public SeasonModel getSeason()
    {
        return mySeason;
    }
    public void setSeason( final SeasonModel season )
    {
        mySeason = season;
    }
    public List<String> getTeams()
    {
        return myTeams;
    }
    public void setTeams( final List<String> teams )
    {
        myTeams = teams;
    }
}
