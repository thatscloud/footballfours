package com.footballfours.model.admin.results.builder;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.persistence.EntityManager;

import com.footballfours.entity.Match;
import com.footballfours.entity.Player;
import com.footballfours.model.admin.ResultsModel;
import com.footballfours.model.admin.common.PlayerModel;

public class ResultsModelBuilder
{
    public static ResultsModel getResultsModelFromEntityManager( final EntityManager entityManager,
                                                                 final UUID matchId )
    {
        final ResultsModel resultsModel = new ResultsModel();
        final Match match = requireNonNull( entityManager.find( Match.class, matchId ) );
        resultsModel.setMatchId( match.getMatchId() );
        resultsModel.setHomeTeamName( match.getHomeMatchTeam().getTeam().getTeamName() );
        resultsModel.setAwayTeamName( match.getAwayMatchTeam().getTeam().getTeamName() );
        resultsModel.setScheduledDateTime(
            match.getScheduledDateTime()
                .atZone( ZoneId.of( "Australia/NSW" ) )
                .format( DateTimeFormatter.ISO_OFFSET_DATE_TIME ) );
        resultsModel.setPlayedDateTime(
            match.getPlayedDateTime()
                .atZone( ZoneId.of( "Australia/NSW" ) )
                .format( DateTimeFormatter.ISO_OFFSET_DATE_TIME ) );
        resultsModel.setRoundNumber( match.getRound().getRoundNumber() );
        resultsModel.setHomeTeamPlayers(
            match.getHomeMatchTeam().getTeam().getPlayers().stream()
                .map( ResultsModelBuilder::fromPlayer )
                .collect( toList() ) );
        resultsModel.setAwayTeamPlayers(
            match.getAwayMatchTeam().getTeam().getPlayers().stream()
                .map( ResultsModelBuilder::fromPlayer )
                .collect( toList() ) );
        return resultsModel;
    }

    private static PlayerModel fromPlayer( final Player player )
    {
        final PlayerModel playerModel = new PlayerModel();
        playerModel.setPlayerId( player.getPlayerId() );
        playerModel.setPlayerGivenName( player.getGivenName() );
        playerModel.setPlayerFamilyName( player.getFamilyName() );
        return playerModel;
    }
}
