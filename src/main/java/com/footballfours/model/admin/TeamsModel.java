package com.footballfours.model.admin;

import java.util.List;

import com.footballfours.model.admin.common.SeasonModel;
import com.footballfours.model.admin.teams.SeasonTeamsModel;

public class TeamsModel
{
    private List<SeasonModel> mySeasons;
    private SeasonTeamsModel mySeasonTeams;

    public List<SeasonModel> getSeasons()
    {
        return mySeasons;
    }

    public void setSeasons( final List<SeasonModel> seasons )
    {
        mySeasons = seasons;
    }

    public SeasonTeamsModel getSeasonTeams()
    {
        return mySeasonTeams;
    }

    public void setSeasonTeams( final SeasonTeamsModel seasonTeams )
    {
        mySeasonTeams = seasonTeams;
    }


}
