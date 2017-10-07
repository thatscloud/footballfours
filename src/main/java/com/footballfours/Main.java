package com.footballfours;

import static org.apache.commons.io.FileUtils.readFileToString;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.post;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.footballfours.model.fixture.builder.FixturesModelBuilder;
import com.footballfours.model.table.builder.TablesModelBuilder;
import com.footballfours.route.HandlebarsRouteFactory;
import com.footballfours.route.StaticContentRoute;

import spark.Session;

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

        final Flyway flyway = new Flyway();
        flyway.setDataSource( connectionPool );
        flyway.setLocations( "classpath:com/footballfours/persist/migration" );
        flyway.migrate();

        final String adminAuth;
        try
        {
            adminAuth = readFileToString( new File( "auth.txt" ) ).trim();
        }
        catch( final IOException e )
        {
            throw new UncheckedIOException( e );
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

        final HandlebarsRouteFactory hbRouteFactory =
            new HandlebarsRouteFactory( "/com/footballfours/template",
                                        connectionPool );

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
        before( "/admin/*", ( req, res ) ->
        {
            if( !Objects.equals( req.splat()[0], "login.html" ) &&
                !Objects.equals( req.splat()[0], "authenticate" ) &&
                !Objects.equals( req.splat()[0], "logout.html" ) )
            {
                final Session session = req.session( false );
                if( session == null ||
                    !Objects.equals(
                        session.attribute( "authenticated" ),
                        Boolean.TRUE ) )
                {
                    res.redirect( "/admin/login.html", 302 );
                }
            }
        } );
        before( "/admin/login.html", ( req, res ) ->
        {
            final Session session = req.session( false );
            if( session != null &&
                Objects.equals(
                    session.attribute( "authenticated" ),
                    Boolean.TRUE ) )
            {
                res.redirect( "/admin/index.html", 302 );
            }
        } );
        before( "/admin/logout.html", ( req, res ) ->
        {
            final Session session = req.session( false );
            if( session != null )
            {
                session.invalidate();
            }
        } );
        get( "/", ( req, res ) ->
        {
            res.redirect( "/fixtures.html", 302 );
            return null;
        } );
        get( "/fixtures.html",
             hbRouteFactory.from(
                 "fixtures",
                 FixturesModelBuilder::getRoundsFromConnection ) );
        get( "/tables.html",
             hbRouteFactory.from(
                 "tables",
                 TablesModelBuilder::getTablesFromConnection ) );
        get( "/admin", ( req, res ) ->
        {
            res.redirect( "/admin/index.html", 302 );
            return null;
        } );
        get( "/admin/index.html",
             hbRouteFactory.from( "admin/index", c -> new Object() ) );
        get( "/admin/login.html", ( req, res ) ->
             hbRouteFactory
                 .from( "admin/login", c ->
                     Objects.equals( req.queryParams( "failed" ), "true" ) )
                 .handle( req, res ) );
        get( "/admin/logout.html", ( req, res ) ->
        hbRouteFactory
            .from( "admin/logout", c ->
                Objects.equals( req.queryParams( "failed" ), "true" ) )
            .handle( req, res ) );
        post( "/admin/authenticate", ( req, res ) ->
        {
            final String password = req.queryParams( "password" );
            final MessageDigest md = MessageDigest.getInstance( "SHA-512" );
            String hexHash = "";
            for( final byte b : md.digest( password.getBytes( StandardCharsets.UTF_8 ) ) )
            {
                hexHash += String.format( "%02x", b < 0 ? ( b + 256 ) : b );
            }
            if( Objects.equals( adminAuth, hexHash ) )
            {
                if( req.session( false ) != null )
                {
                    req.session( false ).invalidate();
                }
                req.session().attribute( "authenticated", Boolean.TRUE );
                res.redirect( "/admin/index.html" );
            }
            else
            {
                res.redirect( "/admin/login.html?failed=true" );
            }
            return res;
        } );
        get( "/*", new StaticContentRoute() );
    }
}