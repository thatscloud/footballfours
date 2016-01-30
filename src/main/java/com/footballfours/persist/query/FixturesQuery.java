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

public class FixturesQuery
{
    private static final String FIXTURES_QUERY =
        loadFixturesQuery();

    private static String loadFixturesQuery()
    {
        try( final InputStream in =
                 FixturesQuery.class.getResourceAsStream( "fixtures.sql" );
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

    public static Statement retrieveFixturesQueryStatement( final Connection connection )
    {
        try
        {
            final Statement statement = connection.createStatement();
            statement.execute( FIXTURES_QUERY );
            return statement;
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }
}
