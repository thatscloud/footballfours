package com.footballfours;

import static spark.Spark.before;
import static spark.Spark.get;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.footballfours.persist.init.DatabaseInitialiser;
import com.footballfours.route.FixturesRoute;
import com.footballfours.route.StaticContentRoute;
import com.footballfours.route.TablesRoute;

public class Main
{
    private static final Logger theLogger = LoggerFactory.getLogger( Main.class );

    public static void main( final String[] args )
    {
        try
        {
            final URI uri = Main.class.getClassLoader().getResource(
                                 "com/footballfours/Main.class" ).toURI();
            if( uri.getScheme().equals( "jar" ) )
            {
                final String uriString = uri.toString();
                final String jarUriString = uriString.split( "!" )[0];
                final URI jarUri = new URI( jarUriString );
                final Map<String, String> env = new HashMap<>();
                env.put( "create", "true" );
                FileSystems.newFileSystem( jarUri, env );
                theLogger.info(
                    "Loading static resources from jar( " + jarUri + " )" );
            }
            else
            {
                theLogger.info( "Loading static resources from filesystem" );
            }
        }
        catch( final URISyntaxException | IOException e )
        {
            throw new RuntimeException( e );
        }

        final String sqlUsername = "football";
        final String sqlPassword = "fours";
        final String encryptionPassword = "footballfours";
        final JdbcConnectionPool connectionPool = JdbcConnectionPool.create(
            "jdbc:h2:./footballfours;CIPHER=AES",
            sqlUsername,
            encryptionPassword + " " + sqlPassword );
        try( final Connection connection = connectionPool.getConnection() )
        {
            if( DatabaseInitialiser.needsInitialising( connection ) )
            {
                theLogger.info( "DB needs initialising" );
                DatabaseInitialiser.initialise( connection );
                theLogger.info( "DB initialised" );
            }
            else
            {
                theLogger.info( "Pre-existing DB found" );
            }
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }

        try
        {
            final Server server = Server.createWebServer( "-webPort", "4568" );
            theLogger.info( "Starting DB web server..." );
            server.start();
            theLogger.info( "DB web server started on port " + server.getPort() );
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }

        try
        {
            final Server server = Server.createTcpServer( "-tcpPort", "4569" );
            theLogger.info( "Starting DB tcp server..." );
            server.start();
            theLogger.info( "DB tcp server started on port " + server.getPort() );
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
        before( ( req, res ) ->
        {
            res.raw().setCharacterEncoding( StandardCharsets.UTF_8.toString() );
            if( req.pathInfo().endsWith( ".html" ) )
            {
                res.raw().setContentType( "text/html; charset=utf-8" );
            }
            else if( req.pathInfo().endsWith( ".css" ) )
            {
                res.raw().setContentType( "text/css; charset=utf-8" );
            }
        } );
        get( "/", ( req, res ) ->
        {
            res.redirect( "/fixtures.html", 302 );
            return res.raw();
        } );
        get( "/fixtures.html", new FixturesRoute( connectionPool ) );
        get( "/tables.html", new TablesRoute( connectionPool ) );
        get( "/*", new StaticContentRoute() );
    }
}