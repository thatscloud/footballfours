package com.footballfours.model.admin.matches.builder;

import static java.util.stream.Collectors.toList;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.footballfours.entity.Match;
import com.footballfours.entity.Match_;
import com.footballfours.entity.Round;
import com.footballfours.entity.Round_;
import com.footballfours.entity.Season;
import com.footballfours.entity.Season_;
import com.footballfours.entity.Team;
import com.footballfours.entity.Team_;
import com.footballfours.model.admin.MatchesModel;
import com.footballfours.model.admin.common.SeasonModel;
import com.footballfours.model.admin.common.TeamModel;
import com.footballfours.model.admin.matches.MatchModel;
import com.footballfours.model.admin.matches.RoundModel;
import com.footballfours.model.admin.matches.SeasonMatchesModel;

public class MatchesModelBuilder
{
    public static MatchesModel getMatchesModelFromEntityManager(
        final EntityManager entityManager,
        final UUID seasonId )
    {
        final MatchesModel matchesModel = new MatchesModel();

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Season> seasonCriteriaQuery =
            criteriaBuilder.createQuery( Season.class );
        final Root<Season> seasonRoot = seasonCriteriaQuery.from( Season.class );
        seasonCriteriaQuery.orderBy(
            criteriaBuilder.asc( criteriaBuilder.upper( seasonRoot.get( Season_.seasonName ) ) ) );
        matchesModel.setSeasons(
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
            final SeasonMatchesModel seasonMatches = new SeasonMatchesModel();
            final SeasonModel seasonModel = new SeasonModel();
            seasonModel.setSeasonId( season.getSeasonId() );
            seasonModel.setSeasonName( season.getSeasonName() );
            seasonModel.setSelected( true );
            seasonMatches.setSeason( seasonModel );

            final CriteriaQuery<Match> matchCriteriaQuery =
                criteriaBuilder.createQuery( Match.class );
            final Root<Match> matchRoot = matchCriteriaQuery.from( Match.class );
            matchCriteriaQuery.where(
                criteriaBuilder.equal( matchRoot.get( Match_.season ), season ) );
            matchCriteriaQuery.orderBy(
                criteriaBuilder.asc( matchRoot.get( Match_.scheduledDateTime ) ) );

            seasonMatches.setMatches(
                entityManager.createQuery( matchCriteriaQuery ).getResultList().stream()
                    .map( match ->
                    {
                        final MatchModel matchModel = new MatchModel();
                        matchModel.setRoundNumber( match.getRound().getRoundNumber() );
                        matchModel.setScheduledDateTimeString(
                            match.getScheduledDateTime()
                                .atZone( ZoneId.of( "Australia/NSW" ) )
                                .format( DateTimeFormatter.ISO_LOCAL_DATE_TIME ) );
                        matchModel.setPlayedDateTimeString(
                            match.getPlayedDateTime()
                                .atZone( ZoneId.of( "Australia/NSW" ) )
                                .format( DateTimeFormatter.ISO_LOCAL_DATE_TIME ) );
                        matchModel.setHomeTeam( match.getHomeMatchTeam().getTeam().getTeamName() );
                        matchModel.setAwayTeam( match.getAwayMatchTeam().getTeam().getTeamName() );
                        return matchModel;
                    } )
                    .collect( toList() ));

            final CriteriaQuery<Team> teamCriteriaQuery =
                criteriaBuilder.createQuery( Team.class );
            final Root<Team> teamRoot = teamCriteriaQuery.from( Team.class );
            teamCriteriaQuery.where(
                criteriaBuilder.equal( teamRoot.get( Team_.season ), season ) );
            teamCriteriaQuery.orderBy(
                criteriaBuilder.asc( criteriaBuilder.upper( teamRoot.get( Team_.teamName ) ) ) );

            seasonMatches.setTeams(
                entityManager.createQuery( teamCriteriaQuery ).getResultList().stream()
                    .map( team ->
                    {
                        final TeamModel teamModel = new TeamModel();
                        teamModel.setTeamId( team.getTeamId() );
                        teamModel.setTeamName( team.getTeamName() );
                        return teamModel;
                    } )
                    .collect( toList() ) );

            final CriteriaQuery<Round> roundCriteriaQuery =
                criteriaBuilder.createQuery( Round.class );
            final Root<Round> roundRoot = roundCriteriaQuery.from( Round.class );
            roundCriteriaQuery.where(
                criteriaBuilder.equal( roundRoot.get( Round_.season ), season ) );
            roundCriteriaQuery.orderBy(
                criteriaBuilder.asc( roundRoot.get( Round_.roundNumber ) ) );

            seasonMatches.setRounds(
                entityManager.createQuery( roundCriteriaQuery ).getResultList().stream()
                    .map( round ->
                    {
                        final RoundModel roundModel = new RoundModel();
                        roundModel.setRoundId( round.getRoundId() );
                        roundModel.setRoundNumber( round.getRoundNumber() );
                        return roundModel;
                    } )
                    .collect( toList() ) );

            matchesModel.setSeasonMatches( seasonMatches );
        }
        return matchesModel;
    }
}
