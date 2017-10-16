package com.footballfours.model.admin;

import java.util.List;

import com.footballfours.model.admin.common.SeasonModel;
import com.footballfours.model.admin.common.TeamModel;
import com.footballfours.model.admin.players.SeasonTeamPlayersModel;

public class PlayersModel
{
    private List<SeasonModel> mySeasons;
    private List<TeamModel> myTeams;
    private SeasonTeamPlayersModel mySeasonTeamPlayers;

    public List<SeasonModel> getSeasons()
    {
        return mySeasons;
    }

    public void setSeasons( final List<SeasonModel> seasons )
    {
        mySeasons = seasons;
    }

    public List<TeamModel> getTeams()
    {
        return myTeams;
    }

    public void setTeams( final List<TeamModel> teams )
    {
        myTeams = teams;
    }

    public SeasonTeamPlayersModel getSeasonTeamPlayers()
    {
        return mySeasonTeamPlayers;
    }

    public void setSeasonTeamPlayers( final SeasonTeamPlayersModel seasonTeamPlayers )
    {
        mySeasonTeamPlayers = seasonTeamPlayers;
    }

    public SeasonModel getSelectedSeason()
    {
        return mySeasons.stream().filter( SeasonModel::isSelected ).findAny().orElse( null );
    }
}
