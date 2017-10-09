package com.footballfours.model.admin.matches;

import java.util.List;

import com.footballfours.model.admin.common.SeasonModel;

public class SeasonMatchesModel
{
    private SeasonModel mySeason;
    private List<MatchModel> myMatches;
    private List<TeamModel> myTeams;
    private List<RoundModel> myRounds;

    public SeasonModel getSeason()
    {
        return mySeason;
    }

    public void setSeason( final SeasonModel season )
    {
        mySeason = season;
    }

    public List<MatchModel> getMatches()
    {
        return myMatches;
    }

    public void setMatches( final List<MatchModel> matches )
    {
        myMatches = matches;
    }

    public List<TeamModel> getTeams()
    {
        return myTeams;
    }

    public void setTeams( final List<TeamModel> teams )
    {
        myTeams = teams;
    }

    public List<RoundModel> getRounds()
    {
        return myRounds;
    }

    public void setRounds( final List<RoundModel> rounds )
    {
        myRounds = rounds;
    }

}
