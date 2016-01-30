package com.footballfours.batch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class AddRounds extends BatchTask
{
    public static void main( final String[] args )
    {
        new AddRounds( args ).run();
    }

    public AddRounds( final String[] args )
    {
        super( args );
    }

    @Override
    protected void runTask( final Connection connection, final String[] args )
        throws Exception
    {
        if( args.length != 1 )
        {
            throw new Exception( "Must specify number of rounds" );
        }

        final int numberOfRounds = Integer.parseInt( args[0] );

        try( final PreparedStatement selectStatement =
                 connection.prepareStatement(
                     "SELECT id_round FROM round WHERE no_round = ?" );
             final PreparedStatement insertStatement =
                 connection.prepareStatement(
                     "INSERT INTO round ( id_round, no_round ) VALUES ( ?, ? )" ) )
        {
            for( int i = 1; i <= numberOfRounds; i++ )
            {
                selectStatement.setInt( 1, i );
                try( final ResultSet resultSet =
                         selectStatement.executeQuery() )
                {
                    if( resultSet.next() )
                    {
                        System.out.println(
                            "Found round number " + i + ". " +
                            "id: " + resultSet.getString( "id_round" ) );
                    }
                    else
                    {
                        final UUID roundId = UUID.randomUUID();
                        insertStatement.setString( 1, roundId.toString() );
                        insertStatement.setInt( 2, i );
                        insertStatement.executeUpdate();
                        insertStatement.clearParameters();
                        System.out.println(
                            "Inserted round " + i + ". " +
                            "id: " + roundId );
                    }
                }
                selectStatement.clearParameters();
            }
        }

    }
}
