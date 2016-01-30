package com.footballfours.batch;

import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.UUID;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class AddGoldenBallVotes extends BatchTask
{

    public static void main( final String[] args )
    {
        new AddGoldenBallVotes( args ).run();
    }

    public AddGoldenBallVotes( final String[] args )
    {
        super( args );
    }

    @Override
    protected void runTask( final Connection connection, final String[] args )
        throws Exception
    {
        if( args.length != 1 )
        {
            throw new Exception( "Must specify a CSV file of golden ball votes" );
        }

        try( final Reader reader = new FileReader( args[0] );
             final CSVParser csvParser = new CSVParser( reader, CSVFormat.RFC4180 ) )
        {
            try( final PreparedStatement insertStatement =
                     connection.prepareStatement(
                         "INSERT INTO golden_ball_vote " +
                         "( " +
                         "id_golden_ball_vote, " +
                         "id_match_team_player, " +
                         "id_match_team, " +
                         "no_votes " +
                         ") " +
                         "SELECT " +
                         "?, " +
                         "mtp.id_match_team_player, " +
                         "mt.id_match_team, " +
                         "? " +
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
                         "p.nm_family = ?" ) )
            {
                int lineNumber = 0;
                try
                {
                    for( final CSVRecord record : csvParser )
                    {
                        lineNumber++;
                        final int roundNumber = Integer.parseInt( record.get( 0 ) );
                        final String homeTeamName = record.get( 1 );
                        final String awayTeamName = record.get( 2 );
                        final String voteTeamName = record.get( 3 );
                        final String givenNames = record.get( 4 );
                        final String familyNames = record.get( 5 );
                        final int numberOfVotes = Integer.parseInt( record.get( 6 ) );

                        final UUID goldenBallVoteId = UUID.randomUUID();
                        insertStatement.setString( 1, goldenBallVoteId.toString() );
                        insertStatement.setInt( 2, numberOfVotes );
                        insertStatement.setInt( 3, roundNumber );
                        insertStatement.setString( 4, homeTeamName );
                        insertStatement.setString( 5, awayTeamName );
                        insertStatement.setString( 6, voteTeamName );
                        insertStatement.setString( 7, givenNames );
                        insertStatement.setString( 8, familyNames );
                        if( insertStatement.executeUpdate() != 1 )
                        {
                            throw new Exception( "Check line " + lineNumber );
                        }
                        insertStatement.clearParameters();
                    }
                }
                catch( final Exception e )
                {
                    throw new Exception( "Check line " + lineNumber, e );
                }
                System.out.println( lineNumber + " voted for players inserted." );
            }
        }
    }

}
