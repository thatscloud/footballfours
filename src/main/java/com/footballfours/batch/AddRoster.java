package com.footballfours.batch;

import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class AddRoster extends BatchTask
{
    public static void main( final String[] args )
    {
        new AddRoster( args ).run();
    }

    public AddRoster( final String[] args )
    {
        super( args );
    }

    @Override
    protected void runTask( final Connection connection, final String[] args )
        throws Exception
    {
        if( args.length != 1 )
        {
            throw new Exception( "Must specify a CSV file of roster" );
        }

        try( final Reader reader = new FileReader( args[0] );
             final CSVParser csvParser = new CSVParser( reader, CSVFormat.RFC4180 ) )
        {
            try( final PreparedStatement selectMatchTeamPlayerStatement =
                     connection.prepareStatement(
                         "SELECT " +
                         "id_match_team_player, " +
                         "no_match_team_player " +
                         "FROM match_team_player " +
                         "WHERE " +
                         "id_match_team = ? AND " +
                         "id_player = ?" );
                 final PreparedStatement selectMatchTeamStatement =
                     connection.prepareStatement(
                         "SELECT mt.id_match_team FROM " +
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
                         "t.nm_team = ? " );
                 final PreparedStatement selectPlayerStatement =
                     connection.prepareStatement(
                         "SELECT p.id_player FROM " +
                         "player p " +
                         "INNER JOIN team t ON " +
                         "p.id_team = t.id_team " +
                         "INNER JOIN match_team mt ON " +
                         "t.id_team = mt.id_team " +
                         "WHERE " +
                         "mt.id_match_team = ? AND " +
                         "p.nm_given = ? AND " +
                         "p.nm_family = ?" );
                 final PreparedStatement insertMatchTeamPlayerStatement =
                     connection.prepareStatement(
                         "INSERT INTO match_team_player " +
                         "( " +
                         "id_match_team_player, " +
                         "id_player, " +
                         "id_team, " +
                         "id_match_team, " +
                         "no_match_team_player " +
                         ") " +
                         "SELECT " +
                         "?, " +
                         "p.id_player, " +
                         "mt.id_team, " +
                         "mt.id_match_team, " +
                         "? " +
                         "FROM " +
                         "player p " +
                         "INNER JOIN match_team mt ON " +
                         "mt.id_team = p.id_team " +
                         "WHERE " +
                         "p.id_player = ? AND " +
                         "mt.id_match_team = ?" ) )
            {
                for( final CSVRecord record : csvParser )
                {
                    final int roundNumber = Integer.parseInt( record.get( 0 ) );
                    final String homeTeamName = record.get( 1 );
                    final String awayTeamName = record.get( 2 );
                    final String playerTeamName = record.get( 3 );
                    final String givenNames = record.get( 4 );
                    final String familyNames = record.get( 5 );

                    selectMatchTeamStatement.setInt( 1, roundNumber );
                    selectMatchTeamStatement.setString( 2, homeTeamName );
                    selectMatchTeamStatement.setString( 3, awayTeamName );
                    selectMatchTeamStatement.setString( 4, playerTeamName );

                    final UUID matchTeamId;
                    try( final ResultSet resultSet =
                             selectMatchTeamStatement.executeQuery() )
                    {
                        if( resultSet.next() )
                        {
                            matchTeamId =
                                UUID.fromString(
                                    resultSet.getString( "id_match_team" ) );
                        }
                        else
                        {
                            throw new Exception(
                                "Could not find match team for team \"" +
                                playerTeamName + "\" in match between \"" +
                                homeTeamName + "\" and \"" + awayTeamName +
                                "\" in round " + roundNumber );
                        }
                    }
                    selectMatchTeamStatement.clearParameters();

                    selectPlayerStatement.setString( 1, matchTeamId.toString() );
                    selectPlayerStatement.setString( 2, givenNames );
                    selectPlayerStatement.setString( 3, familyNames );

                    final UUID playerId;
                    try( final ResultSet resultSet =
                             selectPlayerStatement.executeQuery() )
                    {
                        if( resultSet.next() )
                        {
                            playerId =
                                UUID.fromString( resultSet.getString( "id_player" ) );
                        }
                        else
                        {
                            throw new Exception(
                                "Could not find player with name \"" +
                                givenNames + " " + familyNames +
                                "\" in team \"" + playerTeamName +"\"" );
                        }
                    }
                    selectPlayerStatement.clearParameters();

                    selectMatchTeamPlayerStatement.setString(
                        1, matchTeamId.toString() );
                    selectMatchTeamPlayerStatement.setString( 2, playerId.toString() );
                    try( final ResultSet resultSet =
                             selectMatchTeamPlayerStatement.executeQuery() )
                    {
                        if( resultSet.next() )
                        {
                            System.out.println(
                                "Found player with name \"" + givenNames + " " +
                                familyNames + "\" in team \"" + playerTeamName +
                                "\" with match team player number of " +
                                resultSet.getInt( "no_match_team_player" ) +
                                " for round " + roundNumber + " match between \"" +
                                homeTeamName + "\" and \"" + awayTeamName + "\". " +
                                "id: " + resultSet.getString( "id_match_team_player" ) );
                        }
                        else
                        {
                            final UUID matchTeamPlayerId = UUID.randomUUID();
                            final int matchTeamPlayerNumber =
                                determineNextAvailablePlayerNumberForMatchTeam(
                                    connection,
                                    matchTeamId );
                            insertMatchTeamPlayerStatement.setString(
                                1, matchTeamPlayerId.toString() );
                            insertMatchTeamPlayerStatement.setInt(
                                2, matchTeamPlayerNumber );
                            insertMatchTeamPlayerStatement.setString(
                                3, playerId.toString() );
                            insertMatchTeamPlayerStatement.setString(
                                4, matchTeamId.toString() );
                            insertMatchTeamPlayerStatement.executeUpdate();
                            insertMatchTeamPlayerStatement.clearParameters();
                            System.out.println(
                                "Inserted player with name \"" + givenNames + " " +
                                familyNames + "\" in team \"" + playerTeamName +
                                "\" with match team player number of " +
                                matchTeamPlayerNumber + " for round " +
                                roundNumber + " match between \"" + homeTeamName +
                                "\" and \"" + awayTeamName + "\". " +
                                "id: " + matchTeamPlayerId );
                        }
                    }
                    selectMatchTeamPlayerStatement.clearParameters();
                }
            }
        }
    }

    private static int determineNextAvailablePlayerNumberForMatchTeam(
        final Connection connection,
        final UUID matchTeamId )
        throws SQLException
    {
        try( final PreparedStatement statement =
                 connection.prepareStatement(
                     "SELECT mtp.id_match_team_player " +
                     "FROM " +
                     "match_team_player mtp " +
                     "INNER JOIN match_team mt ON " +
                     "mtp.id_match_team = mt.id_match_team " +
                     "WHERE " +
                     "mt.id_match_team = ? AND " +
                     "mtp.no_match_team_player = ?" ) )
        {
            for( int i = 1; ; i++ )
            {
                statement.setString( 1, matchTeamId.toString() );
                statement.setInt( 2, i );
                try( final ResultSet resultSet = statement.executeQuery() )
                {
                    if( !resultSet.next() )
                    {
                        return i;
                    }
                }
                statement.clearParameters();
            }
        }
    }
}
