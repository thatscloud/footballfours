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

public class AddPlayers extends BatchTask
{
    public static void main( final String[] args )
    {
        new AddPlayers( args ).run();
    }

    public AddPlayers( final String[] args )
    {
        super( args );
    }

    @Override
    protected void runTask( final Connection connection, final String[] args )
        throws Exception
    {
        if( args.length != 1 )
        {
            throw new Exception( "Must specify a CSV file of players" );
        }

        try( final Reader reader = new FileReader( args[0] );
             final CSVParser csvParser = new CSVParser( reader, CSVFormat.RFC4180 ) )
        {
            try( final PreparedStatement selectPlayerStatement =
                     connection.prepareStatement(
                         "SELECT id_player, no_team_player FROM player " +
                         "WHERE " +
                         "id_team = ? AND " +
                         "nm_given = ? AND " +
                         "nm_family = ?" );
                 final PreparedStatement selectTeamStatement =
                     connection.prepareStatement(
                         "SELECT id_team FROM team WHERE nm_team = ?" );
                 final PreparedStatement insertPlayerStatement =
                     connection.prepareStatement(
                         "INSERT INTO player " +
                         "( id_player, id_team, no_team_player, nm_given, nm_family ) " +
                         "VALUES " +
                         "( ?, ?, ?, ?, ? )" ) )
            {
                for( final CSVRecord record : csvParser )
                {
                    final String teamName = record.get( 0 );
                    final String givenNames = record.get( 1 );
                    final String familyNames = record.get( 2 );
                    selectTeamStatement.setString( 1, teamName );
                    final UUID teamId;
                    try( final ResultSet resultSet =
                             selectTeamStatement.executeQuery() )
                    {
                        if( resultSet.next() )
                        {
                            teamId =
                                UUID.fromString( resultSet.getString( "id_team" ) );
                        }
                        else
                        {
                            throw new Exception(
                                "Could not find team with team name of \"" +
                                teamName + "\"" );
                        }
                    }
                    selectTeamStatement.clearParameters();
                    selectPlayerStatement.setString( 1, teamId.toString() );
                    selectPlayerStatement.setString( 2, givenNames );
                    selectPlayerStatement.setString( 3, familyNames );
                    try( final ResultSet resultSet =
                             selectPlayerStatement.executeQuery() )
                    {
                        if( resultSet.next() )
                        {
                            System.out.println(
                                "Found player with name \"" + givenNames + " " +
                                familyNames + "\" in team \"" + teamName + "\" with " +
                                "team player number of " +
                                resultSet.getInt( "no_team_player" ) + ". " +
                                "id: " + resultSet.getString( "id_player" ) );
                        }
                        else
                        {
                            final UUID playerId = UUID.randomUUID();
                            final int teamPlayerNumber =
                                determineNextAvailablePlayerNumberForTeam(
                                    connection, teamName );
                            insertPlayerStatement.setString( 1, playerId.toString() );
                            insertPlayerStatement.setString( 2, teamId.toString() );
                            insertPlayerStatement.setInt( 3, teamPlayerNumber );
                            insertPlayerStatement.setString( 4, givenNames );
                            insertPlayerStatement.setString( 5, familyNames );
                            insertPlayerStatement.executeUpdate();
                            insertPlayerStatement.clearParameters();
                            System.out.println(
                                "Inserted player with name \"" + givenNames + " " +
                                familyNames + "\" in team \"" + teamName + "\" with " +
                                "team player number of " +
                                teamPlayerNumber + ". " +
                                "id: " + playerId );
                        }
                    }
                    selectPlayerStatement.clearParameters();
                }
            }
        }
    }

    private static int determineNextAvailablePlayerNumberForTeam(
        final Connection connection,
        final String teamName )
        throws SQLException
    {
        try( final PreparedStatement statement =
                 connection.prepareStatement(
                     "SELECT p.id_player FROM player p " +
                     "INNER JOIN team t ON " +
                     "p.id_team = t.id_team " +
                     "WHERE " +
                     "t.nm_team = ? AND " +
                     "p.no_team_player = ?" ) )
        {
            for( int i = 1; ; i++ )
            {
                statement.setString( 1, teamName );
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
