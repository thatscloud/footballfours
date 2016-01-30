package com.footballfours.batch;

import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class AddTeams extends BatchTask
{
    public static void main( final String[] args )
    {
        new AddTeams( args ).run();
    }

    public AddTeams( final String[] args )
    {
        super( args );
    }

    @Override
    protected void runTask( final Connection connection, final String[] args )
        throws Exception
    {
        if( args.length != 1 )
        {
            throw new Exception( "Must specify a CSV file of team names, one per row" );
        }

        try( final Reader reader = new FileReader( args[0] );
             final CSVParser csvParser = new CSVParser( reader, CSVFormat.RFC4180 ) )
        {
            try( final PreparedStatement selectStatement =
                     connection.prepareStatement(
                         "SELECT id_team FROM team WHERE nm_team = ?" );
                 final PreparedStatement insertStatement =
                     connection.prepareStatement(
                         "INSERT INTO team ( id_team, nm_team ) VALUES ( ?, ? )" ) )
            {
                for( final CSVRecord record : csvParser )
                {
                    final String teamName = record.get( 0 );
                    selectStatement.setString( 1, teamName );
                    try( final ResultSet resultSet =
                             selectStatement.executeQuery() )
                    {
                        if( resultSet.next() )
                        {
                            System.out.println(
                                "Found team with name \"" + teamName + "\". " +
                                "id: " + resultSet.getString( "id_team" ) );
                        }
                        else
                        {
                            final UUID teamId = UUID.randomUUID();
                            insertStatement.setString( 1, teamId.toString() );
                            insertStatement.setString( 2, teamName );
                            insertStatement.executeUpdate();
                            insertStatement.clearParameters();
                            System.out.println(
                                "Inserted team with name \"" + teamName + "\". " +
                                "id: " + teamId );
                        }
                    }
                    selectStatement.clearParameters();
                }
            }
        }
    }
}
