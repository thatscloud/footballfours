package com.footballfours.batch;

import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.BooleanUtils;

public class AddGoals extends BatchTask
{
    public static void main( final String[] args )
    {
        new AddGoals( args ).run();
    }

    public AddGoals( final String[] args )
    {
        super( args );
    }

    @Override
    protected void runTask( final Connection connection, final String[] args )
        throws Exception
    {
        if( args.length != 1 )
        {
            throw new Exception( "Must specify a CSV file of goals" );
        }

        try( final Reader reader = new FileReader( args[0] );
             final CSVParser csvParser = new CSVParser( reader, CSVFormat.RFC4180 ) )
        {
            try( final PreparedStatement insertStatementForPlayerGoal =
                     connection.prepareStatement(
                         "INSERT INTO goal " +
                         "( " +
                         "id_goal, " +
                         "id_match_team, " +
                         "fl_opponent_own_goal, " +
                         "id_match_team_player " +
                         ") " +
                         "SELECT " +
                         "?, " +
                         "mt.id_match_team, " +
                         "FALSE, " +
                         "mtp.id_match_team_player " +
                         "FROM " +
                         "match_team mt " +
                         "INNER JOIN team t ON " +
                         "t.id_team = mt.id_team " +
                         "INNER JOIN match m ON " +
                         "m.id_match = mt.id_match " +
                         "INNER JOIN match_team hmt ON " +
                         "hmt.id_match = m.id_match AND " +
                         "hmt.cd_team_type = 'HOME' " +
                         "INNER JOIN team ht ON " +
                         "ht.id_team = hmt.id_team " +
                         "INNER JOIN match_team amt ON " +
                         "amt.id_match = m.id_match AND " +
                         "amt.cd_team_type = 'AWAY' " +
                         "INNER JOIN team at ON " +
                         "at.id_team = amt.id_team " +
                         "INNER JOIN round r ON " +
                         "m.id_round = r.id_round " +
                         "INNER JOIN match_team_player mtp ON " +
                         "mtp.id_match_team = mt.id_match_team " +
                         "INNER JOIN player p ON " +
                         "mtp.id_player = p.id_player " +
                         "WHERE " +
                         "r.no_round = ? AND " +
                         "ht.nm_team = ? AND " +
                         "at.nm_team = ? AND " +
                         "t.nm_team = ? AND " +
                         "p.nm_given = ? AND " +
                         "p.nm_family = ?" );
                 final PreparedStatement insertStatementForOwnGoal =
                     connection.prepareStatement(
                         "INSERT INTO goal " +
                         "( " +
                         "id_goal, " +
                         "id_match_team, " +
                         "fl_opponent_own_goal, " +
                         "id_match_team_player " +
                         ") " +
                         "SELECT " +
                         "?, " +
                         "mt.id_match_team, " +
                         "TRUE, " +
                         "NULL " +
                         "FROM " +
                         "match_team mt " +
                         "INNER JOIN team t ON " +
                         "t.id_team = mt.id_team " +
                         "INNER JOIN match m ON " +
                         "m.id_match = mt.id_match " +
                         "INNER JOIN match_team hmt ON " +
                         "hmt.id_match = m.id_match AND " +
                         "hmt.cd_team_type = 'HOME' " +
                         "INNER JOIN team ht ON " +
                         "ht.id_team = hmt.id_team " +
                         "INNER JOIN match_team amt ON " +
                         "amt.id_match = m.id_match AND " +
                         "amt.cd_team_type = 'AWAY' " +
                         "INNER JOIN team at ON " +
                         "at.id_team = amt.id_team " +
                         "INNER JOIN round r ON " +
                         "m.id_round = r.id_round " +
                         "WHERE " +
                         "r.no_round = ? AND " +
                         "ht.nm_team = ? AND " +
                         "at.nm_team = ? AND " +
                         "t.nm_team = ?" ) )
            {
                int lineNumber = 0;
                for( final CSVRecord record : csvParser )
                {
                    lineNumber++;
                    final int roundNumber = Integer.parseInt( record.get( 0 ) );
                    final String homeTeamName = record.get( 1 );
                    final String awayTeamName = record.get( 2 );
                    final String scoringTeamName = record.get( 3 );
                    final boolean ownGoal = BooleanUtils.toBoolean( record.get( 4 ) );
                    final UUID goalId = UUID.randomUUID();
                    if( ownGoal )
                    {
                        insertStatementForOwnGoal.setString( 1, goalId.toString() );
                        insertStatementForOwnGoal.setInt( 2, roundNumber );
                        insertStatementForOwnGoal.setString( 3, homeTeamName );
                        insertStatementForOwnGoal.setString( 4, awayTeamName );
                        insertStatementForOwnGoal.setString( 5, scoringTeamName );
                        if( insertStatementForOwnGoal.executeUpdate() != 1 )
                        {
                            throw new Exception( "Check line " + lineNumber );
                        }
                        insertStatementForOwnGoal.clearParameters();
                    }
                    else
                    {
                        final String givenNames = record.get( 5 );
                        final String familyNames = record.get( 6 );
                        insertStatementForPlayerGoal.setString( 1, goalId.toString() );
                        insertStatementForPlayerGoal.setInt( 2, roundNumber );
                        insertStatementForPlayerGoal.setString( 3, homeTeamName );
                        insertStatementForPlayerGoal.setString( 4, awayTeamName );
                        insertStatementForPlayerGoal.setString( 5, scoringTeamName );
                        insertStatementForPlayerGoal.setString( 6, givenNames );
                        insertStatementForPlayerGoal.setString( 7, familyNames );
                        if( insertStatementForPlayerGoal.executeUpdate() != 1 )
                        {
                            throw new Exception( "Check line " + lineNumber );
                        }
                        insertStatementForPlayerGoal.clearParameters();
                    }
                }
                System.out.println( lineNumber + " goals inserted." );
            }
        }
    }
}
