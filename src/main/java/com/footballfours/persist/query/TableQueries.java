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

public class TableQueries
{
    private static final String LEAGUE_TABLE_QUERY =
        loadQuery( "leaguetable.sql" );
    private static final String GOLDEN_BALL_TABLE_QUERY =
        loadQuery( "goldenballtable.sql" );
    private static final String GOLDEN_BOOT_TABLE_QUERY =
        loadQuery( "goldenboottable.sql" );

    private static String loadQuery( final String relativeFileName )
    {
        try( final InputStream in =
                 TableQueries.class.getResourceAsStream( relativeFileName );
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

    public static Statement retrieveLeagueTableQueryStatement(
        final Connection connection )
    {
        try
        {
            final Statement statement = connection.createStatement();
            statement.execute( LEAGUE_TABLE_QUERY );
            return statement;
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    public static Statement retrieveGoldenBootTableQueryStatement(
        final Connection connection )
    {
        try
        {
            final Statement statement = connection.createStatement();
            statement.execute( GOLDEN_BOOT_TABLE_QUERY );
            return statement;
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    public static Statement retrieveGoldenBallTableQueryStatement(
        final Connection connection )
    {
        try
        {
            final Statement statement = connection.createStatement();
            statement.execute( GOLDEN_BALL_TABLE_QUERY );
            return statement;
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }
}
