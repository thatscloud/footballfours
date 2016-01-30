package com.footballfours.persist.init;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class DatabaseInitialiser
{
    public static boolean needsInitialising( final Connection connection )
    {
        try( final Statement statement = connection.createStatement() )
        {
            final ResultSet resultSet = statement.executeQuery(
                "SELECT tx_parameter FROM db_info " +
                "WHERE cd_parameter = 'VERSION'" );
            resultSet.next();
            if( Objects.equals( resultSet.getString( "tx_parameter" ), "0.0.1" ) )
            {
                return false;
            }
            else
            {
                throw new IllegalStateException( "Invalid DB version" );
            }

        }
        catch( final SQLException e )
        {
            return true;
        }
    }

    public static void initialise( final Connection connection )
    {
        final StringWriter stringWriter = new StringWriter();
        try( final InputStream in =
                 DatabaseInitialiser.class.getResourceAsStream( "init.sql" );
             final Reader reader = new InputStreamReader( in, StandardCharsets.UTF_8 );
             final Reader buff = new BufferedReader( reader ) )
        {
            IOUtils.copy( buff, stringWriter );
        }
        catch( final IOException e )
        {
            throw new UncheckedIOException( e );
        }

        Stream.of( stringWriter.toString().split( ";" ) )
            .filter( s -> !StringUtils.isBlank( s ) )
            .forEach( s ->
        {
            try( final Statement statement = connection.createStatement() )
            {

                statement.executeUpdate( s );
            }
            catch( final SQLException e )
            {
                throw new RuntimeException( e );
            }
        } );
    }
}
