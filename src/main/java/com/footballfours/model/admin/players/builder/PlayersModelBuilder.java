package com.footballfours.model.admin.players.builder;

import static java.util.stream.Collectors.toList;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.footballfours.entity.Player;
import com.footballfours.entity.Player_;
import com.footballfours.entity.Season;
import com.footballfours.entity.Season_;
import com.footballfours.entity.Team;
import com.footballfours.entity.Team_;
import com.footballfours.model.admin.PlayersModel;
import com.footballfours.model.admin.common.SeasonModel;
import com.footballfours.model.admin.common.TeamModel;
import com.footballfours.model.admin.players.SeasonTeamPlayersModel;

public class PlayersModelBuilder
{
    public static PlayersModel getPlayersModelFromEntityManager(
        final EntityManager entityManager,
        final UUID seasonId,
        final UUID teamId )
    {
        final PlayersModel playersModel = new PlayersModel();

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Season> seasonCriteriaQuery =
            criteriaBuilder.createQuery( Season.class );
        final Root<Season> seasonRoot = seasonCriteriaQuery.from( Season.class );
        seasonCriteriaQuery.orderBy(
            criteriaBuilder.asc( criteriaBuilder.upper( seasonRoot.get( Season_.seasonName ) ) ) );
        playersModel.setSeasons(
            entityManager.createQuery( seasonCriteriaQuery ).getResultList().stream()
                .map( seasonItem ->
                {
                    final SeasonModel seasonModel = new SeasonModel();
                    seasonModel.setSeasonId( seasonItem.getSeasonId() );
                    seasonModel.setSeasonName( seasonItem.getSeasonName() );
                    seasonModel.setSelected( Objects.equals( seasonItem.getSeasonId(),
                                                             seasonId ) );
                    return seasonModel;
                } )
                .collect( toList() ) );

        final Season season =
            seasonId == null ? null : entityManager.find( Season.class, seasonId );
        if( season != null )
        {
            final CriteriaQuery<Team> teamCriteriaQuery =
                criteriaBuilder.createQuery( Team.class );
            final Root<Team> teamRoot = teamCriteriaQuery.from( Team.class );
            teamCriteriaQuery.where(
                criteriaBuilder.equal( teamRoot.get( Team_.season ), season ) );
            teamCriteriaQuery.orderBy(
                criteriaBuilder.asc( criteriaBuilder.upper( teamRoot.get( Team_.teamName ) ) ) );
            playersModel.setTeams(
                entityManager.createQuery( teamCriteriaQuery ).getResultList().stream()
                    .map( teamItem ->
                    {
                        final TeamModel teamModel = new TeamModel();
                        teamModel.setTeamId( teamItem.getTeamId() );
                        teamModel.setTeamName( teamItem.getTeamName() );
                        teamModel.setSelected( Objects.equals( teamModel.getTeamId(),
                                                               teamId ) );
                        return teamModel;
                    } )
                    .collect( toList() ) );

            final Team team =
                teamId == null ? null : entityManager.find( Team.class, teamId );
            if( team != null )
            {
                final SeasonTeamPlayersModel seasonTeamPlayersModel =
                    new SeasonTeamPlayersModel();

                final SeasonModel seasonModel = new SeasonModel();
                seasonModel.setSeasonId( season.getSeasonId() );
                seasonModel.setSeasonName( season.getSeasonName() );
                seasonModel.setSelected( true );
                seasonTeamPlayersModel.setSeason( seasonModel );

                final TeamModel teamModel = new TeamModel();
                teamModel.setTeamId( team.getTeamId() );
                teamModel.setTeamName( team.getTeamName() );
                teamModel.setSelected( true );
                seasonTeamPlayersModel.setTeam( teamModel );

                final CriteriaQuery<String> playerNameCriteriaQuery =
                    criteriaBuilder.createQuery( String.class );
                final Root<Player> playerRoot = playerNameCriteriaQuery.from( Player.class );
                playerNameCriteriaQuery.where(
                    criteriaBuilder.equal( playerRoot.get( Player_.team ), team ) );
                playerNameCriteriaQuery.orderBy(
                    criteriaBuilder.asc( playerRoot.get( Player_.teamPlayerNumber ) ) );
                playerNameCriteriaQuery.select(
                    criteriaBuilder.concat(
                        criteriaBuilder.concat( playerRoot.get( Player_.givenName ), " " ),
                        playerRoot.get( Player_.familyName ) ) );

                seasonTeamPlayersModel.setSeason( seasonModel );
                seasonTeamPlayersModel.setTeam( teamModel );
                seasonTeamPlayersModel.setPlayerNames(
                    entityManager.createQuery( playerNameCriteriaQuery ).getResultList() );
                playersModel.setSeasonTeamPlayers( seasonTeamPlayersModel );
            }
        }
        return playersModel;
    }
}
