package com.footballfours;

import static java.lang.Integer.parseInt;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.lang3.BooleanUtils.toBoolean;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.commons.lang3.tuple.Pair;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.Server;
import org.hibernate.jpa.boot.spi.Bootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.footballfours.entity.Goal;
import com.footballfours.entity.GoldenBallVote;
import com.footballfours.entity.Match;
import com.footballfours.entity.MatchTeam;
import com.footballfours.entity.MatchTeamPlayer;
import com.footballfours.entity.Parameter;
import com.footballfours.entity.Parameter_;
import com.footballfours.entity.Player;
import com.footballfours.entity.Player_;
import com.footballfours.entity.Round;
import com.footballfours.entity.Round_;
import com.footballfours.entity.Season;
import com.footballfours.entity.Team;
import com.footballfours.entity.constant.MatchStatusCode;
import com.footballfours.entity.constant.MatchTeamTeamTypeCode;
import com.footballfours.entity.constant.ParameterParameterCode;
import com.footballfours.model.admin.matches.builder.MatchesModelBuilder;
import com.footballfours.model.admin.players.builder.PlayersModelBuilder;
import com.footballfours.model.admin.results.builder.ResultsModelBuilder;
import com.footballfours.model.admin.seasons.builder.SeasonsModelBuilder;
import com.footballfours.model.admin.teams.builder.TeamsModelBuilder;
import com.footballfours.model.fixture.builder.FixturesModelBuilder;
import com.footballfours.model.rounds.builder.RoundsModelBuilder;
import com.footballfours.model.table.builder.TablesModelBuilder;
import com.footballfours.persist.RunAgainstDatabase;
import com.footballfours.route.HandlebarsRouteFactory;
import com.footballfours.route.StaticContentRoute;
import com.footballfours.util.PersistenceUnitInfoUtils;
import com.footballfours.util.UrlCodecUtils;

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

        final EntityManagerFactory entityManagerFactory =
            Bootstrap.getEntityManagerFactoryBuilder(
                    PersistenceUnitInfoUtils.createPersistenceUnitInfo( connectionPool ),
                    Collections.emptyMap() )
                .withDataSource( connectionPool )
                .build();

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
                                        connectionPool,
                                        entityManagerFactory );

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
            if( req.splat().length == 0 )
            {
                res.redirect( "/admin/index.html", 302 );
                halt();
            }
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
                    halt();
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
                halt();
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
        get( "/rulessummary.html",
            hbRouteFactory.from( "rulessummary", c -> new Object() ) );
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
        get( "/admin/logout.html",
             hbRouteFactory.from( "admin/logout", c -> new Object() ) );
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
        get( "/admin/seasons.html",
            ( req, res ) ->
            hbRouteFactory
                .fromJpa( "/admin/seasons",
                          em ->
                          {
                              final HashMap<String, Object> context = new HashMap<>();
                              context.put( "seasons",
                                           SeasonsModelBuilder.getSeasonsFromEntityManager( em ) );
                              if( isNotEmpty( req.queryParams( "error" ) ) )
                              {
                                  context.put( "error", req.queryParams( "error" ) );
                              }
                              return context;
                          } )
                .handle( req, res ) );
        post( "/admin/seasons", ( req, res ) ->
        {
            if( StringUtils.isBlank( req.queryParams( "seasonName" ) ) )
            {
                    res.redirect( "/admin/seasons.html?error=" +
                                  UrlCodecUtils.encode( "Season name cannot be blank." ) );
                    halt();
            }
            RunAgainstDatabase.run( connectionPool, connection ->
            {
                try( final PreparedStatement statement =
                         connection.prepareStatement(
                             "INSERT INTO season " +
                             "( " +
                             "    id_season," +
                             "    nm_season " +
                             ") " +
                             "VALUES " +
                             "( " +
                             "    RANDOM_UUID(), " +
                             "    ? " +
                             ")" ) )
                {
                    statement.setString( 1, StringUtils.strip( req.queryParams( "seasonName" ) ) );
                    statement.executeUpdate();
                }
                catch( final SQLException e )
                {
                    res.redirect( "/admin/seasons.html?error=" +
                                  UrlCodecUtils.encode( e.getMessage() ) );
                    halt();
                }
            } );
            res.redirect( "/admin/seasons.html" );
            return null;
        } );
        get( "/admin/rounds.html",
            hbRouteFactory.from( "admin/rounds",
                                 RoundsModelBuilder::getSeasonRoundsFromConnection ) );
        post( "/admin/addRound", ( req, res ) ->
        {
            RunAgainstDatabase.run( entityManagerFactory, entityManager ->
            {
                final Season season =
                    entityManager.find( Season.class,
                                        UUID.fromString( req.queryParams( "seasonId" ) ) );

                final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
                final CriteriaQuery<Integer> criteriaQuery =
                    criteriaBuilder.createQuery( Integer.class );
                final Root<Round> round = criteriaQuery.from( Round.class );
                criteriaQuery.where( criteriaBuilder.equal( round.get( Round_.season ), season ) );
                criteriaQuery.select( criteriaBuilder.max( round.get( Round_.roundNumber ) ) );
                final Optional<Integer> maxRoundNumber =
                    Optional.ofNullable(
                        entityManager.createQuery( criteriaQuery ).getSingleResult() );

                final Round newRound = new Round();
                newRound.setRoundNumber( maxRoundNumber.orElse( 0 ) + 1 );
                newRound.setSeason( season );
                entityManager.persist( newRound );
            } );
            res.redirect( "/admin/rounds.html" );
            return null;
        } );
        get( "/admin/teams.html",
            ( req, res ) ->
                hbRouteFactory.fromJpa(
                        "admin/teams",
                        entityManager ->
                            TeamsModelBuilder.getTeamsModelFromEntityManager(
                                entityManager,
                                isBlank( req.queryParams( "seasonId" ) ) ?
                                    null :
                                    UUID.fromString ( req.queryParams( "seasonId" ) ) ) )
                    .handle( req, res ) );
        post( "/admin/addTeam", ( req, res ) ->
        {
            RunAgainstDatabase.run( entityManagerFactory, entityManager ->
            {
                final Season season =
                    entityManager.find( Season.class,
                                        UUID.fromString( req.queryParams( "seasonId" ) ) );
                final Team team = new Team();
                team.setSeason( season );
                team.setTeamName( StringUtils.strip( req.queryParams( "teamName" ) ) );
                entityManager.persist( team );
            } );
            res.redirect( "/admin/teams.html?seasonId=" +
                              UrlCodecUtils.encode( req.queryParams( "seasonId" ) ) );
            return null;
        } );
        get( "/admin/matches.html",
            ( req, res ) ->
                hbRouteFactory.fromJpa(
                        "admin/matches",
                        entityManager ->
                            MatchesModelBuilder.getMatchesModelFromEntityManager(
                                entityManager,
                                isBlank( req.queryParams( "seasonId" ) ) ?
                                    null :
                                    UUID.fromString ( req.queryParams( "seasonId" ) ) ) )
                    .handle( req, res ) );
        post( "/admin/addMatch", ( req, res ) ->
        {
            RunAgainstDatabase.run( entityManagerFactory, entityManager ->
            {
                final Season season =
                    entityManager.find( Season.class,
                                        UUID.fromString( req.queryParams( "seasonId" ) ) );
                final Round round =
                    entityManager.find( Round.class,
                                        UUID.fromString( req.queryParams( "roundId" ) ) );
                final Team homeTeam =
                    entityManager.find( Team.class,
                                        UUID.fromString( req.queryParams( "homeTeamId" ) ) );
                final Team awayTeam =
                    entityManager.find( Team.class,
                                        UUID.fromString( req.queryParams( "awayTeamId" ) ) );

                final Match match = new Match();
                match.setRound( round );
                match.setSeason( season );
                match.setScheduledDateTime(
                    LocalDateTime.parse( req.queryParams( "scheduledDateTime" ) )
                        .atZone( ZoneId.of( "Australia/NSW" ) )
                        .toInstant() );
                match.setPlayedDateTime(
                    LocalDateTime.parse( req.queryParams( "playedDateTime" ) )
                        .atZone( ZoneId.of( "Australia/NSW" ) )
                        .toInstant() );

                entityManager.persist( match );

                final MatchTeam homeMatchTeam = new MatchTeam();
                homeMatchTeam.setRound( round );
                homeMatchTeam.setSeason( season );
                homeMatchTeam.setTeam( homeTeam );
                homeMatchTeam.setMatch( match );
                homeMatchTeam.setTeamTypeCode( MatchTeamTeamTypeCode.HOME );

                final MatchTeam awayMatchTeam = new MatchTeam();
                awayMatchTeam.setRound( round );
                awayMatchTeam.setSeason( season );
                awayMatchTeam.setTeam( awayTeam );
                awayMatchTeam.setMatch( match );
                awayMatchTeam.setTeamTypeCode( MatchTeamTeamTypeCode.AWAY );

                entityManager.persist( homeMatchTeam );
                entityManager.persist( awayMatchTeam );
            } );
            res.redirect( "/admin/matches.html?seasonId=" +
                              UrlCodecUtils.encode( req.queryParams( "seasonId" ) ) );
            return null;
        } );
        post( "/admin/setCurrentSeason", ( req, res ) ->
        {
            RunAgainstDatabase.run( entityManagerFactory, entityManager ->
            {
                final Season season =
                    entityManager.find( Season.class,
                                        UUID.fromString( req.queryParams( "seasonId" ) ) );

                final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
                final CriteriaQuery<Parameter> parameterCriteriaQuery =
                    criteriaBuilder.createQuery( Parameter.class );
                final Root<Parameter> parameterRoot = parameterCriteriaQuery.from( Parameter.class );
                parameterCriteriaQuery.where(
                    criteriaBuilder.equal( parameterRoot.get( Parameter_.parameterCode ),
                                           ParameterParameterCode.CURRENT_SEASON ) );
                final Parameter parameter =
                    entityManager.createQuery( parameterCriteriaQuery ).getSingleResult();
                parameter.setParameterText( season.getSeasonId().toString() );
            } );
            res.redirect( "/admin/seasons.html" );
            return null;
        } );
        get( "/admin/players.html",
            ( req, res ) ->
                hbRouteFactory.fromJpa(
                        "admin/players",
                        entityManager ->
                            PlayersModelBuilder.getPlayersModelFromEntityManager(
                                entityManager,
                                isBlank( req.queryParams( "seasonId" ) ) ?
                                    null :
                                    UUID.fromString ( req.queryParams( "seasonId" ) ),
                                isBlank( req.queryParams( "teamId" ) ) ?
                                    null :
                                    UUID.fromString ( req.queryParams( "teamId" ) ) ) )
                    .handle( req, res ) );
        post( "/admin/addPlayer", ( req, res ) ->
        {
            RunAgainstDatabase.run( entityManagerFactory, entityManager ->
            {
                requireNonNull( entityManager.find(
                    Season.class,
                    UUID.fromString( req.queryParams( "seasonId" ) ) ) );
                final Team team =
                    requireNonNull( entityManager.find(
                        Team.class,
                        UUID.fromString( req.queryParams( "teamId" ) ) ) );

                final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
                final CriteriaQuery<Integer> criteriaQuery =
                    criteriaBuilder.createQuery( Integer.class );
                final Root<Player> player = criteriaQuery.from( Player.class );
                criteriaQuery.where( criteriaBuilder.equal( player.get( Player_.team ), team ) );
                criteriaQuery.select(
                    criteriaBuilder.max( player.get( Player_.teamPlayerNumber ) ) );
                final Optional<Integer> maxTeamPlayerNumber =
                    Optional.ofNullable(
                        entityManager.createQuery( criteriaQuery ).getSingleResult() );

                final Player newPlayer = new Player();
                newPlayer.setGivenName( req.queryParams( "givenName" ) );
                newPlayer.setFamilyName( req.queryParams( "familyName" ) );
                newPlayer.setTeamPlayerNumber( maxTeamPlayerNumber.orElse( 0 ) + 1 );
                newPlayer.setTeam( team );
                entityManager.persist( newPlayer );
            } );
            res.redirect( "/admin/players.html?seasonId=" +
                              UrlCodecUtils.encode( req.queryParams( "seasonId" ) ) + "&" +
                              "teamId=" +
                              UrlCodecUtils.encode( req.queryParams( "teamId" ) ) );
            return null;
        } );
        get( "/admin/results.html",
            ( req, res ) ->
                hbRouteFactory.fromJpa(
                        "admin/results",
                        entityManager ->
                            ResultsModelBuilder.getResultsModelFromEntityManager(
                                entityManager,
                                isBlank( req.queryParams( "matchId" ) ) ?
                                    null :
                                    UUID.fromString ( req.queryParams( "matchId" ) ) ) )
                    .handle( req, res ) );
        post( "/admin/addResults", ( req, res ) ->
        {
            final Mutable<UUID> seasonId = new MutableObject<>();
            RunAgainstDatabase.run( entityManagerFactory, entityManager ->
            {
                final Match match =
                    requireNonNull( entityManager.find(
                        Match.class,
                        UUID.fromString( req.queryParams( "matchId" ) ) ) );
                seasonId.setValue( match.getSeason().getSeasonId() );

                final MatchTeam homeMatchTeam = match.getHomeMatchTeam();
                final MatchTeam awayMatchTeam = match.getAwayMatchTeam();

                homeMatchTeam.getGoals().forEach( entityManager::remove );
                homeMatchTeam.getGoldenBallVotes().forEach( entityManager::remove );
                homeMatchTeam.getMatchTeamPlayers().forEach( entityManager::remove );
                awayMatchTeam.getGoals().forEach( entityManager::remove );
                awayMatchTeam.getGoldenBallVotes().forEach( entityManager::remove );
                awayMatchTeam.getMatchTeamPlayers().forEach( entityManager::remove );

                entityManager.flush();

                final Supplier<Integer> homeMatchTeamPlayerNumberSupplier =
                    IntStream.iterate( 1, i -> i + 1 ).boxed().iterator()::next;
                final Supplier<Integer> awayMatchTeamPlayerNumberSupplier =
                    IntStream.iterate( 1, i -> i + 1 ).boxed().iterator()::next;

                for( int i = 0; true; i++ )
                {
                    recordPlayerData( req.queryParams( "homePlayer" + i + "Id" ),
                                      req.queryParams( "homePlayer" + i + "Present" ),
                                      req.queryParams( "homePlayer" + i + "Votes" ),
                                      req.queryParams( "homePlayer" + i + "Goals" ),
                                      homeMatchTeamPlayerNumberSupplier,
                                      homeMatchTeam,
                                      entityManager );

                    recordPlayerData( req.queryParams( "awayPlayer" + i + "Id" ),
                                      req.queryParams( "awayPlayer" + i + "Present" ),
                                      req.queryParams( "awayPlayer" + i + "Votes" ),
                                      req.queryParams( "awayPlayer" + i + "Goals" ),
                                      awayMatchTeamPlayerNumberSupplier,
                                      awayMatchTeam,
                                      entityManager );

                    if( isBlank( req.queryParams( "homePlayer" + i + "Id" ) ) &&
                        isBlank( req.queryParams( "awayPlayer" + i + "Id" ) ) )
                    {
                        break;
                    }
                }

                Stream.of( Pair.of( "home", homeMatchTeam ),
                           Pair.of( "away", awayMatchTeam ) )
                    .forEach( pair ->
                    {
                        if( isNotBlank( req.queryParams( pair.getLeft() + "OpponentOwnGoals" ) ) )
                        {
                            final int goals =
                                parseInt( req.queryParams( pair.getLeft() + "OpponentOwnGoals" ) );
                            if( goals < 0 )
                            {
                                throw new IllegalArgumentException(
                                    "Goals must be non-negative, was " + goals );
                            }

                            for( int i = 0; i < goals; i++ )
                            {
                                final Goal goal = new Goal();
                                goal.setMatchTeam( pair.getRight() );
                                goal.setOpponentOwnGoal( true );
                                entityManager.persist( goal );
                            }
                        }
                    } );
                match.setStatusCode( MatchStatusCode.COMPLETED );
            } );
            res.redirect( "/admin/matches.html?seasonId=" +
                              UrlCodecUtils.encode( seasonId.getValue().toString() ) );
            return null;
        } );
        get( "/*", new StaticContentRoute() );
    }

    private static void recordPlayerData( final String playerIdString,
                                          final String playerPresentString,
                                          final String playerVotesString,
                                          final String playerGoalsString,
                                          final Supplier<Integer> matchTeamPlayerNumberSupplier,
                                          final MatchTeam matchTeam,
                                          final EntityManager entityManager )
    {
        final UUID homePlayerId =
            isBlank( playerIdString ) ? null : UUID.fromString( playerIdString );
        if( homePlayerId != null && toBoolean( playerPresentString ) )
        {
            final Player player = entityManager.find( Player.class, homePlayerId );

            final MatchTeamPlayer matchTeamPlayer = new MatchTeamPlayer();
            matchTeamPlayer.setPlayer( player );
            matchTeamPlayer.setMatchTeam( matchTeam );
            matchTeamPlayer.setTeam( matchTeam.getTeam() );
            matchTeamPlayer.setMatchTeamPlayerNumber(
                matchTeamPlayerNumberSupplier.get() );
            entityManager.persist( matchTeamPlayer );

            if( isNotBlank( playerVotesString ) )
            {
                final int votes = parseInt( playerVotesString );
                if( votes < 0 || votes > 3 )
                {
                    throw new IllegalArgumentException(
                        "Votes must be 1 to 3 inclusive, was " + votes );
                }

                if( votes != 0 )
                {
                    final GoldenBallVote goldenBallVote = new GoldenBallVote();
                    goldenBallVote.setMatchTeam( matchTeam );
                    goldenBallVote.setMatchTeamPlayer( matchTeamPlayer );
                    goldenBallVote.setVotesNumber( votes );
                    entityManager.persist( goldenBallVote );
                }
            }

            if( isNotBlank( playerGoalsString ) )
            {
                final int goals = parseInt( playerGoalsString );
                if( goals < 0 )
                {
                    throw new IllegalArgumentException(
                        "Goals must be non-negative, was " + goals );
                }

                for( int i = 0; i < goals; i++ )
                {
                    final Goal goal = new Goal();
                    goal.setMatchTeam( matchTeam );
                    goal.setMatchTeamPlayer( matchTeamPlayer );
                    goal.setOpponentOwnGoal( false );
                    entityManager.persist( goal );
                }
            }
        }
    }
}