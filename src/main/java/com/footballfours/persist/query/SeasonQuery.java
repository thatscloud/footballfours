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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

public class SeasonQuery
{
    private static final String SEASON_QUERY =
        loadSeasonQuery();

    private static String loadSeasonQuery()
    {
        try( final InputStream in =
                 SeasonQuery.class.getResourceAsStream( "season.sql" );
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

    public static Statement retrieveSeasonQueryStatement( final Connection connection,
                                                          final UUID seasonId )
    {
        try
        {
            final PreparedStatement preparedStatement =
                connection.prepareStatement( SEASON_QUERY );
            preparedStatement.setObject( 1, seasonId );
            preparedStatement.executeUpdate();
            return preparedStatement;
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }
}
