package com.footballfours.model.admin.players;

import java.util.List;

import com.footballfours.model.admin.common.SeasonModel;
import com.footballfours.model.admin.common.TeamModel;

public class SeasonTeamPlayersModel
{
    private SeasonModel mySeason;
    private TeamModel myTeam;
    private List<String> myPlayerNames;

    public SeasonModel getSeason()
    {
        return mySeason;
    }

    public void setSeason( final SeasonModel season )
    {
        mySeason = season;
    }

    public TeamModel getTeam()
    {
        return myTeam;
    }

    public void setTeam( final TeamModel team )
    {
        myTeam = team;
    }

    public List<String> getPlayerNames()
    {
        return myPlayerNames;
    }

    public void setPlayerNames( final List<String> playerNames )
    {
        myPlayerNames = playerNames;
    }
}
