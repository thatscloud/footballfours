package com.footballfours.model.table.builder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.footballfours.model.table.GoldenBallTableRow;
import com.footballfours.model.table.GoldenBootTableRow;
import com.footballfours.model.table.LeagueTableRow;
import com.footballfours.model.table.Tables;
import com.footballfours.persist.query.TableQueries;

public class TablesModelBuilder
{
    public static Tables getTablesFromConnection( final Connection connection )
    {
        final Tables tables = new Tables();
        try( final Statement statement =
                 TableQueries.retrieveLeagueTableQueryStatement( connection );
             final ResultSet resultSet = statement.getResultSet() )
        {
            final List<LeagueTableRow> leagueTable = new LinkedList<>();
            LeagueTableRow previousLeagueTableRow = null;
            int ordinalNumber = 0;
            while( resultSet.next() )
            {
                ordinalNumber++;
                final LeagueTableRow leagueTableRow = new LeagueTableRow();
                leagueTableRow.setTeamName( resultSet.getString( "teamName" ) );
                leagueTableRow.setMatchesPlayed( resultSet.getInt( "matchesPlayed" ) );
                leagueTableRow.setNumberOfWins( resultSet.getInt( "win" ) );
                leagueTableRow.setNumberOfDraws( resultSet.getInt( "draw" ) );
                leagueTableRow.setNumberOfLosses( resultSet.getInt( "loss" ) );
                leagueTableRow.setGoalsFor( resultSet.getInt( "goalsFor") );
                leagueTableRow.setGoalsAgainst( resultSet.getInt( "goalsAgainst" ) );
                leagueTableRow.setGoalDifference(
                    resultSet.getInt( "goalDifference" ) );
                leagueTableRow.setPoints( resultSet.getInt( "points" ) );
                if( previousLeagueTableRow == null )
                {
                    leagueTableRow.setPosition( ordinalNumber );
                }
                else
                {
                    if( previousLeagueTableRow.getPoints() ==
                            leagueTableRow.getPoints() &&
                        previousLeagueTableRow.getGoalDifference() ==
                            leagueTableRow.getGoalDifference() &&
                        previousLeagueTableRow.getGoalsFor() ==
                            leagueTableRow.getGoalsFor() )
                    {
                        leagueTableRow.setPosition(
                            previousLeagueTableRow.getPosition() );
                    }
                    else
                    {
                        leagueTableRow.setPosition( ordinalNumber );
                    }
                }
                leagueTable.add( leagueTableRow );
                previousLeagueTableRow = leagueTableRow;
            }
            tables.setLeagueTable( leagueTable );
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }



        try( final Statement statement =
                 TableQueries.retrieveGoldenBallTableQueryStatement( connection );
             final ResultSet resultSet = statement.getResultSet() )
        {
            final List<GoldenBallTableRow> goldenBallTable = new LinkedList<>();
            GoldenBallTableRow previousGoldenBallTableRow = null;
            int ordinalNumber = 0;
            while( resultSet.next() )
            {
                ordinalNumber++;
                final GoldenBallTableRow goldenBallTableRow = new GoldenBallTableRow();
                goldenBallTableRow.setPlayerName(
                    resultSet.getString( "givenName" ).substring( 0, 1 ) + ". " +
                    resultSet.getString( "familyName" ) );
                goldenBallTableRow.setTeamName( resultSet.getString( "teamName" ) );
                goldenBallTableRow.setVotes( resultSet.getInt( "votes" ) );
                if( previousGoldenBallTableRow == null )
                {
                    goldenBallTableRow.setPosition( ordinalNumber );
                }
                else
                {
                    if( previousGoldenBallTableRow.getVotes() ==
                            goldenBallTableRow.getVotes() )
                    {
                        goldenBallTableRow.setPosition(
                            previousGoldenBallTableRow.getPosition() );
                    }
                    else
                    {
                        goldenBallTableRow.setPosition( ordinalNumber );
                    }
                }
                goldenBallTable.add( goldenBallTableRow );
                previousGoldenBallTableRow = goldenBallTableRow;

            }
            tables.setGoldenBallTable( goldenBallTable );
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }

        try( final Statement statement =
                 TableQueries.retrieveGoldenBootTableQueryStatement( connection );
             final ResultSet resultSet = statement.getResultSet() )
        {
            final List<GoldenBootTableRow> goldenBootTable = new LinkedList<>();
            GoldenBootTableRow previousGoldenBootTableRow = null;
            int ordinalNumber = 0;
            while( resultSet.next() )
            {
                ordinalNumber++;
                final GoldenBootTableRow goldenBootTableRow = new GoldenBootTableRow();
                goldenBootTableRow.setPlayerName(
                    resultSet.getString( "givenName" ).substring( 0, 1 ) + ". " +
                    resultSet.getString( "familyName" ) );
                goldenBootTableRow.setTeamName( resultSet.getString( "teamName" ) );
                goldenBootTableRow.setGoals( resultSet.getInt( "goals" ) );
                if( previousGoldenBootTableRow == null )
                {
                    goldenBootTableRow.setPosition( ordinalNumber );
                }
                else
                {
                    if( previousGoldenBootTableRow.getGoals() ==
                            goldenBootTableRow.getGoals() )
                    {
                        goldenBootTableRow.setPosition(
                            previousGoldenBootTableRow.getPosition() );
                    }
                    else
                    {
                        goldenBootTableRow.setPosition( ordinalNumber );
                    }
                }
                goldenBootTable.add( goldenBootTableRow );
                previousGoldenBootTableRow = goldenBootTableRow;

            }
            tables.setGoldenBootTable( goldenBootTable );
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }

        return tables;
    }
}
