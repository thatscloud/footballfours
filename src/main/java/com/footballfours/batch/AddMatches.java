package com.footballfours.batch;

import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.footballfours.util.DateTimeUtils;

public class AddMatches extends BatchTask
{
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
        DateTimeFormatter.ofPattern( "dd-MM-yyyy HH:mm" );

    public static void main( final String[] args )
    {
        new AddMatches( args ).run();
    }

    public AddMatches( final String[] args )
    {
        super( args );
    }

    @Override
    protected void runTask( final Connection connection, final String[] args )
        throws Exception
    {
        if( args.length != 1 )
        {
            throw new Exception( "Must specify a CSV file of matches" );
        }

        try( final Reader reader = new FileReader( args[0] );
             final CSVParser csvParser = new CSVParser( reader, CSVFormat.RFC4180 ) )
        {
            try( final PreparedStatement selectMatchStatement =
                     connection.prepareStatement(
                         "SELECT m.id_match FROM match m " +
                         "INNER JOIN match_team hmt ON " +
                         "hmt.id_match = m.id_match AND " +
                         "hmt.cd_team_type = 'HOME' " +
                         "INNER JOIN team ht ON " +
                         "ht.id_team = hmt.id_team " +
                         "INNER JOIN match_team amt ON " +
                         "amt.id_match = m.id_match AND " +
                         "amt.cd_team_Type = 'AWAY' " +
                         "INNER JOIN team at ON " +
                         "at.id_team = amt.id_team " +
                         "WHERE " +
                         "m.id_round = ? AND " +
                         "ht.nm_team = ? AND " +
                         "at.nm_team = ?" );
                 final PreparedStatement selectTeamStatement =
                     connection.prepareStatement(
                         "SELECT id_team FROM team WHERE nm_team = ?" );
                 final PreparedStatement selectRoundStatement =
                     connection.prepareStatement(
                         "SELECT id_round FROM round WHERE no_round = ?" );
                 final PreparedStatement insertMatchStatement =
                     connection.prepareStatement(
                         "INSERT INTO match " +
                         "( id_match, id_round, dt_scheduled, dt_played ) " +
                         "VALUES " +
                         "( ?, ?, ?, ? )" );
                 final PreparedStatement insertMatchTeamStatement =
                     connection.prepareStatement(
                         "INSERT INTO match_team " +
                         "( " +
                         "id_match_team, " +
                         "id_team, " +
                         "id_match, " +
                         "id_round, " +
                         "cd_team_type " +
                         ") " +
                         "SELECT " +
                         "?, " +
                         "?, " +
                         "id_match, " +
                         "id_round, " +
                         "? " +
                         "FROM match WHERE id_match = ?" ) )
            {
                for( final CSVRecord record : csvParser )
                {
                    final int roundNumber = Integer.parseInt( record.get( 0 ) );
                    final String homeTeamName = record.get( 1 );
                    final String awayTeamName = record.get( 2 );
                    final LocalDateTime scheduledDateTime =
                        LocalDateTime.parse( record.get( 3 ), DATE_TIME_FORMATTER );
                    final LocalDateTime playedDateTime =
                        LocalDateTime.parse( record.get( 4 ), DATE_TIME_FORMATTER );

                    selectTeamStatement.setString( 1, homeTeamName );
                    final UUID homeTeamId;
                    try( final ResultSet resultSet =
                             selectTeamStatement.executeQuery() )
                    {
                        if( resultSet.next() )
                        {
                            homeTeamId =
                                UUID.fromString( resultSet.getString( "id_team" ) );
                        }
                        else
                        {
                            throw new Exception(
                                "Could not find team with team name of \"" +
                                homeTeamName + "\"" );
                        }
                    }
                    selectTeamStatement.clearParameters();

                    selectTeamStatement.setString( 1, awayTeamName );
                    final UUID awayTeamId;
                    try( final ResultSet resultSet =
                             selectTeamStatement.executeQuery() )
                    {
                        if( resultSet.next() )
                        {
                            awayTeamId =
                                UUID.fromString( resultSet.getString( "id_team" ) );
                        }
                        else
                        {
                            throw new Exception(
                                "Could not find team with team name of \"" +
                                awayTeamName + "\"" );
                        }
                    }
                    selectTeamStatement.clearParameters();

                    selectRoundStatement.setInt( 1, roundNumber );
                    final UUID roundId;
                    try( final ResultSet resultSet =
                             selectRoundStatement.executeQuery() )
                    {
                        if( resultSet.next() )
                        {
                            roundId =
                                UUID.fromString( resultSet.getString( "id_round" ) );
                        }
                        else
                        {
                            throw new Exception(
                                "Could not find round number " + roundNumber );
                        }
                    }
                    selectRoundStatement.clearParameters();

                    selectMatchStatement.setString( 1, roundId.toString() );
                    selectMatchStatement.setString( 2, homeTeamName );
                    selectMatchStatement.setString( 3, awayTeamName );
                    try( final ResultSet resultSet =
                             selectMatchStatement.executeQuery() )
                    {
                        if( resultSet.next() )
                        {
                            System.out.println(
                                "Found round " + roundNumber + " match between " +
                                "\"" + homeTeamName + "\" and " +
                                "\"" + awayTeamName + "\". " +
                                "id: " + resultSet.getString( "id_match" ) );
                        }
                        else
                        {
                            final UUID matchId = UUID.randomUUID();
                            insertMatchStatement.setString( 1, matchId.toString() );
                            insertMatchStatement.setString( 2, roundId.toString() );
                            insertMatchStatement.setTimestamp(
                                3,
                                DateTimeUtils
                                    .convertLocalDateTimeToTimestamp(
                                        scheduledDateTime ) );
                            insertMatchStatement.setTimestamp(
                                4,
                                DateTimeUtils
                                    .convertLocalDateTimeToTimestamp(
                                        playedDateTime ) );
                            insertMatchStatement.executeUpdate();
                            insertMatchStatement.clearParameters();

                            final UUID homeMatchTeamId = UUID.randomUUID();
                            insertMatchTeamStatement.setString(
                                1, homeMatchTeamId.toString() );
                            insertMatchTeamStatement.setString(
                                2, homeTeamId.toString() );
                            insertMatchTeamStatement.setString(
                                3, "HOME" );
                            insertMatchTeamStatement.setString(
                                4, matchId.toString() );
                            insertMatchTeamStatement.execute();
                            insertMatchTeamStatement.clearParameters();

                            final UUID awayMatchTeamId = UUID.randomUUID();
                            insertMatchTeamStatement.setString(
                                1, awayMatchTeamId.toString() );
                            insertMatchTeamStatement.setString(
                                2, awayTeamId.toString() );
                            insertMatchTeamStatement.setString(
                                3, "AWAY" );
                            insertMatchTeamStatement.setString(
                                4, matchId.toString() );
                            insertMatchTeamStatement.execute();
                            insertMatchTeamStatement.clearParameters();

                            System.out.println(
                                "Inserted round " + roundNumber + " match between " +
                                "\"" + homeTeamName + "\" and " +
                                "\"" + awayTeamName + "\". " +
                                "id: " + matchId );
                        }
                    }
                    selectMatchStatement.clearParameters();
                }
            }
        }
    }
}
