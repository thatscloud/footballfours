package com.footballfours.model.admin;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;

import com.footballfours.model.admin.common.PlayerModel;
import com.footballfours.util.PairedList;

public class ResultsModel
{
    private UUID myMatchId;
    private String myHomeTeamName;
    private String myAwayTeamName;
    private List<PlayerModel> myHomeTeamPlayers;
    private List<PlayerModel> myAwayTeamPlayers;
    private Integer myRoundNumber;
    private String myPlayedDateTime;
    private String myScheduledDateTime;

    public UUID getMatchId()
    {
        return myMatchId;
    }

    public void setMatchId( final UUID matchId )
    {
        myMatchId = matchId;
    }

    public String getHomeTeamName()
    {
        return myHomeTeamName;
    }

    public void setHomeTeamName( final String homeTeamName )
    {
        myHomeTeamName = homeTeamName;
    }

    public String getAwayTeamName()
    {
        return myAwayTeamName;
    }

    public void setAwayTeamName( final String awayTeamName )
    {
        myAwayTeamName = awayTeamName;
    }

    public List<PlayerModel> getHomeTeamPlayers()
    {
        return myHomeTeamPlayers;
    }

    public void setHomeTeamPlayers( final List<PlayerModel> homeTeamPlayers )
    {
        myHomeTeamPlayers = homeTeamPlayers;
    }

    public List<PlayerModel> getAwayTeamPlayers()
    {
        return myAwayTeamPlayers;
    }

    public List<Pair<PlayerModel, PlayerModel>> getPairedPlayers()
    {
        return new PairedList<>( myHomeTeamPlayers, myAwayTeamPlayers );
    }

    public void setAwayTeamPlayers( final List<PlayerModel> awayTeamPlayers )
    {
        myAwayTeamPlayers = awayTeamPlayers;
    }

    public Integer getRoundNumber()
    {
        return myRoundNumber;
    }

    public void setRoundNumber( final Integer roundNumber )
    {
        myRoundNumber = roundNumber;
    }

    public String getPlayedDateTime()
    {
        return myPlayedDateTime;
    }

    public void setPlayedDateTime( final String playedDateTime )
    {
        myPlayedDateTime = playedDateTime;
    }

    public String getScheduledDateTime()
    {
        return myScheduledDateTime;
    }

    public void setScheduledDateTime( final String scheduledDateTime )
    {
        myScheduledDateTime = scheduledDateTime;
    }
}
