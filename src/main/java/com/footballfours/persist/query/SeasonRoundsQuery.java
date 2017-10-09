package com.footballfours.persist.query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.IOUtils;

public class SeasonRoundsQuery
{
    private static final String SEASON_ROUNDS_QUERY =
        loadSeasonRoundsQuery();

    private static String loadSeasonRoundsQuery()
    {
        try( final InputStream in =
                 SeasonRoundsQuery.class.getResourceAsStream( "seasonrounds.sql" );
             final Reader reader = new InputStreamReader( in, StandardCharsets.UTF_8 );
             final Reader buff = new BufferedReader( reader ) )
        {
            final StringWriter stringWriter = new StringWriter();
            IOUtils.copy( buff, stringWriter );
            return stringWriter.toString();
        }
        catch( final IOException e )
        {
            throw new UncheckedIOException( e );
        }
    }

    public static Statement retrieveSeasonRoundsQueryStatement( final Connection connection )
    {
        try
        {
            final Statement statement = connection.createStatement();
            statement.execute( SEASON_ROUNDS_QUERY );
            return statement;
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }
}
