package com.footballfours.model.admin.teams.builder;

import static java.util.stream.Collectors.toList;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.footballfours.entity.Season;
import com.footballfours.entity.Season_;
import com.footballfours.entity.Team;
import com.footballfours.entity.Team_;
import com.footballfours.model.admin.TeamsModel;
import com.footballfours.model.admin.common.SeasonModel;
import com.footballfours.model.admin.teams.SeasonTeamsModel;

public class TeamsModelBuilder
{
    public static TeamsModel getTeamsModelFromEntityManager( final EntityManager entityManager,
                                                             final UUID seasonId )
    {
        final TeamsModel teamsModel = new TeamsModel();

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Season> seasonCriteriaQuery =
            criteriaBuilder.createQuery( Season.class );
        final Root<Season> seasonRoot = seasonCriteriaQuery.from( Season.class );
        seasonCriteriaQuery.orderBy(
            criteriaBuilder.asc( criteriaBuilder.upper( seasonRoot.get( Season_.seasonName ) ) ) );
        teamsModel.setSeasons(
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
            final SeasonTeamsModel seasonTeams = new SeasonTeamsModel();
            final SeasonModel seasonModel = new SeasonModel();
            seasonModel.setSeasonId( season.getSeasonId() );
            seasonModel.setSeasonName( season.getSeasonName() );
            seasonModel.setSelected( true );
            seasonTeams.setSeason( seasonModel );

            final CriteriaQuery<String> teamNameCriteriaQuery =
                criteriaBuilder.createQuery( String.class );
            final Root<Team> teamRoot = teamNameCriteriaQuery.from( Team.class );
            teamNameCriteriaQuery.where(
                criteriaBuilder.equal( teamRoot.get( Team_.season ), season ) );
            teamNameCriteriaQuery.orderBy(
                criteriaBuilder.asc( criteriaBuilder.upper( teamRoot.get( Team_.teamName ) ) ) );
            teamNameCriteriaQuery.select( teamRoot.get( Team_.teamName ) );

            seasonTeams.setTeams(
                entityManager.createQuery( teamNameCriteriaQuery ).getResultList() );

            teamsModel.setSeasonTeams( seasonTeams );
        }
        return teamsModel;
    }
}
