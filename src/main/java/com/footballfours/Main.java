package com.footballfours;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.footballfours.core.route.management.RouteManager;

public class Main
{
    private static final Logger theLogger = LoggerFactory.getLogger( Main.class );

    public static void main( final String[] args ) throws InstantiationException,
                                                   IllegalAccessException
    {
        try
        {
            final URI uri = Main.class.getClassLoader()
                .getResource( "com/footballfours/Main.class" ).toURI();
            if ( uri.getScheme().equals( "jar" ) )
            {
                final String uriString = uri.toString();
                final String jarUriString = uriString.split( "!" )[0];
                final URI jarUri = new URI( jarUriString );
                final Map<String, String> env = new HashMap<>();
                env.put( "create", "true" );
                FileSystems.newFileSystem( jarUri, env );
                theLogger
                    .info( "Loading static resources from jar( " + jarUri + " )" );
            }
            else
            {
                theLogger.info( "Loading static resources from filesystem" );
            }
        }
        catch ( final URISyntaxException | IOException e )
        {
            throw new RuntimeException( e );
        }

        final String sqlUsername = "football";
        final String sqlPassword = "fours";
        final String encryptionPassword = "footballfours";
        final String connectionPassword = encryptionPassword + " " + sqlPassword;
        final JdbcConnectionPool connectionPool = JdbcConnectionPool.create(
            "jdbc:h2:./footballfours;CIPHER=AES", sqlUsername, connectionPassword );

        final Flyway flyway = new Flyway();
        flyway.setDataSource( connectionPool );
        flyway.setLocations( "classpath:com/footballfours/persist/migration" );
        flyway.migrate();

        try
        {
            final Server server = Server.createWebServer( "-webPort", "4568" );
            theLogger.info( "Starting DB web server..." );
            server.start();
            theLogger.info( "DB web server started on port " + server.getPort() );
        }
        catch ( final SQLException e )
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
        catch ( final SQLException e )
        {
            throw new RuntimeException( e );
        }

        Connection restConnection = null;
        try
        {
            restConnection = connectionPool.getConnection();
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "Could not get connection.", e );
        }
        
        // New Rest Routes
        RouteManager.insertRoutes( restConnection );
        

    }
}