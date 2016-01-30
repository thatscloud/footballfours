package com.footballfours.model.fixture.builder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.footballfours.model.fixture.Goalscorer;
import com.footballfours.model.fixture.Match;
import com.footballfours.model.fixture.Round;
import com.footballfours.model.fixture.Team;
import com.footballfours.persist.query.FixturesQuery;
import com.footballfours.util.DateTimeUtils;

public class FixturesModelBuilder
{
    public static List<Round> getRoundsFromConnection( final Connection connection )
    {
        try( final Statement statement =
                 FixturesQuery.retrieveFixturesQueryStatement( connection );
             final ResultSet resultSet = statement.getResultSet() )
        {
            final List<Round> rounds = new LinkedList<>();
            Round currentRound = null;
            Match currentMatch = null;
            while( resultSet.next() )
            {
                final int roundNumber = resultSet.getInt( "roundNumber" );
                final String homeTeamName = resultSet.getString( "homeTeamName" );
                final String awayTeamName = resultSet.getString( "awayTeamName" );
                final LocalDateTime scheduledDateTime =
                    DateTimeUtils.convertTimestampToLocalDateTime(
                        resultSet.getTimestamp( "scheduledDateTime" ) );
                final LocalDateTime playedDateTime =
                    DateTimeUtils.convertTimestampToLocalDateTime(
                        resultSet.getTimestamp( "playedDateTime" ) );
                final String matchStatus = resultSet.getString( "matchStatus" );
                final int homeGoals = resultSet.getInt( "homeGoals" );
                final int awayGoals = resultSet.getInt( "awayGoals" );
                final String scoringTeamName = resultSet.getString( "scoringTeamName" );
                final Boolean ownGoal = getBooleanOrNull( resultSet, "ownGoal" );
                final String playerGivenNames =
                    resultSet.getString( "playerGivenNames" );
                final String playerFamilyNames =
                    resultSet.getString( "playerFamilyNames" );
                final Integer playerGoals =
                    getIntegerOrNull( resultSet, "playerGoals" );

                if( currentMatch == null ||
                    currentRound.getRoundNumber() != roundNumber ||
                    currentMatch.getHomeTeam().getName() != homeTeamName ||
                    currentMatch.getAwayTeam().getName() != awayTeamName )
                {
                    if( currentMatch != null )
                    {
                        currentRound.getMatches().add( currentMatch );
                    }
                    currentMatch = new Match();
                    final Team homeTeam = new Team();
                    homeTeam.setName( homeTeamName );
                    homeTeam.setGoals( homeGoals );
                    homeTeam.setGoalscorers( new ArrayList<>() );

                    final Team awayTeam = new Team();
                    awayTeam.setName( awayTeamName );
                    awayTeam.setGoals( awayGoals );
                    awayTeam.setGoalscorers( new ArrayList<>() );
                    currentMatch.setHomeTeam( homeTeam );
                    currentMatch.setAwayTeam( awayTeam );

                    currentMatch.setPlayedDateTime( playedDateTime );
                    currentMatch.setScheduledDateTime( scheduledDateTime );

                    currentMatch.setStatus( matchStatus );
                }

                if( currentRound == null ||
                    currentRound.getRoundNumber() != roundNumber )
                {
                    if( currentRound != null )
                    {
                        rounds.add( currentRound );
                    }
                    currentRound = new Round();
                    currentRound.setRoundNumber( roundNumber );
                    currentRound.setMatches( new LinkedList<>() );
                }

                if( !StringUtils.isBlank( scoringTeamName ) )
                {
                    final List<Goalscorer> goalscorers;
                    if( scoringTeamName.equals( homeTeamName ) )
                    {
                        goalscorers = currentMatch.getHomeTeam().getGoalscorers();
                    }
                    else
                    {
                        goalscorers = currentMatch.getAwayTeam().getGoalscorers();
                    }

                    final Goalscorer goalscorer = new Goalscorer();
                    if( ownGoal )
                    {
                        goalscorer.setName( "Opponent Own Goal" );
                    }
                    else
                    {
                        goalscorer.setName(
                            playerGivenNames.substring( 0, 1 ) + ". " +
                            playerFamilyNames );
                    }
                    goalscorer.setNumberOfGoals( playerGoals );
                    goalscorers.add( goalscorer );
                }

            }

            if( currentMatch != null )
            {
                currentRound.getMatches().add( currentMatch );
            }

            if( currentRound != null )
            {
                rounds.add( currentRound );
            }
            return rounds;
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    private static Boolean getBooleanOrNull( final ResultSet resultSet,
                                             final String columnName )
        throws SQLException
    {
        final boolean b = resultSet.getBoolean( columnName );
        if( resultSet.wasNull() )
        {
            return null;
        }
        else
        {
            return b;
        }
    }

    private static Integer getIntegerOrNull( final ResultSet resultSet,
                                             final String columnName )
        throws SQLException
    {
        final int i = resultSet.getInt( columnName );
        if( resultSet.wasNull() )
        {
            return null;
        }
        else
        {
            return i;
        }
    }
}
