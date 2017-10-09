package com.footballfours.model.admin;

import java.util.List;

import com.footballfours.model.admin.common.SeasonModel;
import com.footballfours.model.admin.matches.SeasonMatchesModel;

public class MatchesModel
{
    private List<SeasonModel> mySeasons;
    private SeasonMatchesModel mySeasonMatches;

    public List<SeasonModel> getSeasons()
    {
        return mySeasons;
    }

    public void setSeasons( final List<SeasonModel> seasons )
    {
        mySeasons = seasons;
    }

    public SeasonMatchesModel getSeasonMatches()
    {
        return mySeasonMatches;
    }

    public void setSeasonMatches( final SeasonMatchesModel seasonMatches )
    {
        mySeasonMatches = seasonMatches;
    }
}
