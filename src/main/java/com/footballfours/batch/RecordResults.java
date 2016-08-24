package com.footballfours.batch;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.footballfours.batch.pojo.Goal;
import com.footballfours.batch.pojo.Match;
import com.footballfours.batch.pojo.Votes;

public class RecordResults extends BatchTask
{
    public static void main( final String[] args )
    {
        new RecordResults( args ).run();
    }

    public RecordResults( final String[] args )
    {
        super( args );
    }

    @Override
    protected void runTask( final Connection connection, final String[] args )
        throws Exception
    {
        if( args.length < 1 )
        {
            throw new Exception( "Must specify at least on JSON file of results" );
        }

        final ObjectMapper objectMapper = new ObjectMapper();
        for( final String arg : args )
        {
            final Match match;
            try( final InputStream in = new FileInputStream( arg ) )
            {
                 match = objectMapper.readValue( in, Match.class );
            }
            checkProvidedMatchDataValidity( match );
            checkSeasonExistence( match, connection );
            checkRoundExistence( match, connection );
            checkTeamExistence( match, connection );
            checkMatchExistence( match, connection );
            checkPlayerExistence( match, connection );
            createMatchTeamPlayerIfRequired( match, connection );
            recordScore( match, connection );
            recordGoldenBallVotes( match, connection );
            setMatchCompleted( match, connection );
        }
    }

    private static void checkProvidedMatchDataValidity( final Match match )
    {
        Objects.requireNonNull( match.getRound(), "round" );
        Objects.requireNonNull( match.getSeason(), "season" );
        Objects.requireNonNull( match.getHomeTeam(), "homeTeam" );
        Objects.requireNonNull( match.getAwayTeam(), "awayTeam" );
        Objects.requireNonNull( match.getHomeTeam().getName(), "homeTeam.name" );
        Objects.requireNonNull( match.getAwayTeam().getName(), "awayTeam.name" );
        Objects.requireNonNull( match.getHomeTeam().getPlayers(), "homeTeam.players" );
        Objects.requireNonNull( match.getAwayTeam().getPlayers(), "awayTeam.players" );
        Objects.requireNonNull( match.getHomeTeam().getGoals(), "homeTeam.goals" );
        int i = 0;
        for( final Goal goal : match.getHomeTeam().getGoals() )
        {
            if( goal.getScorer() == null &&
                ( goal.getOpponentOwnGoal() == null ||
                  Objects.equals( goal.getOpponentOwnGoal(), Boolean.FALSE ) ) )
            {
                throw new IllegalArgumentException(
                    "homeTeam.goal[" + i + "].scorer may not be null when " +
                    "homeTeam.goal[" + i + "].opponentOwnGoal is null or false" );
            }
            i++;
        }
        Objects.requireNonNull( match.getAwayTeam().getGoals(), "awayTeam.goals" );
        i = 0;
        for( final Goal goal : match.getAwayTeam().getGoals() )
        {
            if( goal.getScorer() == null &&
                ( goal.getOpponentOwnGoal() == null ||
                  Objects.equals( goal.getOpponentOwnGoal(), Boolean.FALSE ) ) )
            {
                throw new IllegalArgumentException(
                    "awayTeam.goal[" + i + "].scorer may not be null when " +
                    "awayTeam.goal[" + i + "].opponentOwnGoal is null or false" );
            }
            i++;
        }
        if( match.getHomeTeam().getVotes() != null )
        {
            if( match.getHomeTeam().getVotes().getOneVotePlayer() != null )
            {
                Objects.requireNonNull( match.getHomeTeam().getVotes().getTwoVotesPlayer(),
                                        "if homeTeam.votes.1 present, " +
                                        "homeTeam.votes.2 must be present" );

                if( !match.getHomeTeam().getPlayers().contains(
                        match.getHomeTeam().getVotes().getOneVotePlayer() ) )
                {
                    throw new IllegalArgumentException(
                        "homeTeam.votes.1 ( \"" +
                        match.getHomeTeam().getVotes().getOneVotePlayer() +
                        "\" ) not contained in homeTeam.players" );
                }
            }

            if( match.getHomeTeam().getVotes().getTwoVotesPlayer() != null )
            {
                Objects.requireNonNull( match.getHomeTeam().getVotes().getThreeVotesPlayer(),
                                        "if homeTeam.votes.2 present, " +
                                        "homeTeam.votes.3 must be present" );

                if( !match.getHomeTeam().getPlayers().contains(
                        match.getHomeTeam().getVotes().getTwoVotesPlayer() ) )
                {
                    throw new IllegalArgumentException(
                        "homeTeam.votes.2 ( \"" +
                        match.getHomeTeam().getVotes().getTwoVotesPlayer() +
                        "\" ) not contained in homeTeam.players" );
                }
            }

            if( match.getHomeTeam().getVotes().getThreeVotesPlayer() != null )
            {
                if( !match.getHomeTeam().getPlayers().contains(
                    match.getHomeTeam().getVotes().getThreeVotesPlayer() ) )
                {
                    throw new IllegalArgumentException(
                        "homeTeam.votes.3 ( \"" +
                        match.getHomeTeam().getVotes().getThreeVotesPlayer() +
                        "\" ) not contained in homeTeam.players" );
                }
            }
        }

        if( match.getAwayTeam().getVotes() != null )
        {
            if( match.getAwayTeam().getVotes().getOneVotePlayer() != null )
            {
                Objects.requireNonNull( match.getAwayTeam().getVotes().getTwoVotesPlayer(),
                                        "if awayTeam.votes.1 present, " +
                                        "awayTeam.votes.2 must be present" );

                if( !match.getAwayTeam().getPlayers().contains(
                        match.getAwayTeam().getVotes().getOneVotePlayer() ) )
                {
                    throw new IllegalArgumentException(
                        "awayTeam.votes.1 ( \"" +
                        match.getAwayTeam().getVotes().getOneVotePlayer() +
                        "\" ) not contained in awayTeam.players" );
                }
            }

            if( match.getAwayTeam().getVotes().getTwoVotesPlayer() != null )
            {
                Objects.requireNonNull( match.getAwayTeam().getVotes().getThreeVotesPlayer(),
                                        "if awayTeam.votes.2 present, " +
                                        "awayTeam.votes.3 must be present" );
                if( !match.getAwayTeam().getPlayers().contains(
                    match.getAwayTeam().getVotes().getTwoVotesPlayer() ) )
                {
                    throw new IllegalArgumentException(
                        "awayTeam.votes.2 ( \"" +
                        match.getAwayTeam().getVotes().getTwoVotesPlayer() +
                        "\" ) not contained in awayTeam.players" );
                }
            }

            if( match.getAwayTeam().getVotes().getThreeVotesPlayer() != null )
            {
                if( !match.getAwayTeam().getPlayers().contains(
                        match.getAwayTeam().getVotes().getThreeVotesPlayer() ) )
                {
                    throw new IllegalArgumentException(
                        "awayTeam.votes.3 ( \"" +
                        match.getAwayTeam().getVotes().getThreeVotesPlayer() +
                        "\" ) not contained in awayTeam.players" );
                }
            }
        }

        i = 0;
        for( final Goal goal : match.getHomeTeam().getGoals() )
        {
            if( !Objects.equals( goal.getOpponentOwnGoal(), Boolean.TRUE ) &&
                !match.getHomeTeam().getPlayers().contains( goal.getScorer() ) )
            {
                throw new IllegalArgumentException(
                    "homeTeam.goal[" + i + "].scorer ( \"" + goal.getScorer() +
                    "\" ) not contained in homeTeam.players" );
            }
            i++;
        }

        i = 0;
        for( final Goal goal : match.getAwayTeam().getGoals() )
        {
            if( !Objects.equals( goal.getOpponentOwnGoal(), Boolean.TRUE ) &&
                !match.getAwayTeam().getPlayers().contains( goal.getScorer() ) )
            {
                throw new IllegalArgumentException(
                    "awayTeam.goal[" + i + "].scorer ( \"" + goal.getScorer() +
                    "\" ) not contained in awayTeam.players" );
            }
            i++;
        }
    }

    private static void checkSeasonExistence( final Match match,
                                              final Connection connection )
    {
        try( final PreparedStatement statement = connection.prepareStatement(
                 "SELECT COUNT(*) FROM season WHERE nm_season = ?" ) )
        {
            statement.setString( 1, match.getSeason() );
            try( final ResultSet resultSet = statement.executeQuery() )
            {
                resultSet.next();
                final int count = resultSet.getInt( 1 );
                if( count != 1 )
                {
                    throw new RuntimeException(
                        "Could not find season \"" + match.getSeason() + "\"" );
                }
            }
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    private static void checkRoundExistence( final Match match,
                                             final Connection connection )
    {
        try( final PreparedStatement statement = connection.prepareStatement(
                 "SELECT COUNT(*) FROM round r " +
                 "INNER JOIN season s ON " +
                 "r.id_season = s.id_season " +
                 "WHERE " +
                     "s.nm_season = ? AND " +
                     "r.no_round = ?" ) )
        {
            statement.setString( 1, match.getSeason() );
            statement.setInt( 2, match.getRound() );
            try( final ResultSet resultSet = statement.executeQuery() )
            {
                resultSet.next();
                final int count = resultSet.getInt( 1 );
                if( count != 1 )
                {
                    throw new RuntimeException(
                        "Season \"" + match.getSeason() + "\" has no round " + match.getRound() );
                }
            }
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    private static void checkTeamExistence( final Match match,
                                            final Connection connection )
    {
        Stream.of( match.getHomeTeam(), match.getAwayTeam() ).forEach( t ->
        {
            try( final PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(*) FROM season s " +
                     "INNER JOIN team t ON " +
                     "t.id_season = s.id_season " +
                     "WHERE " +
                         "s.nm_season = ? AND " +
                         "t.nm_team = ?" ) )
            {
                statement.setString( 1, match.getSeason() );
                statement.setString( 2, t.getName() );
                try( final ResultSet resultSet = statement.executeQuery() )
                {
                    resultSet.next();
                    final int count = resultSet.getInt( 1 );
                    if( count != 1 )
                    {
                        throw new RuntimeException(
                            "Season \"" + match.getSeason() + "\" has no team \"" +
                            t.getName() + "\"" );
                    }
                }
            }
            catch( final SQLException e )
            {
                throw new RuntimeException( e );
            }
        } );
    }

    private static void checkMatchExistence( final Match match,
                                             final Connection connection )
    {
        try( final PreparedStatement statement = connection.prepareStatement(
                 "SELECT COUNT(*) FROM match m " +
                 "INNER JOIN round r ON " +
                 "r.id_round = m.id_round " +
                 "INNER JOIN season s ON " +
                 "s.id_season = r.id_season " +
                 "INNER JOIN match_team hmt ON " +
                 "hmt.id_match = m.id_match AND " +
                 "hmt.cd_team_type = 'HOME' " +
                 "INNER JOIN match_team amt ON " +
                 "amt.id_match = m.id_match AND " +
                 "amt.cd_team_type = 'AWAY' " +
                 "INNER JOIN team ht ON " +
                 "ht.id_team = hmt.id_team " +
                 "INNER JOIN team at ON " +
                 "at.id_team = amt.id_team " +
                 "WHERE " +
                     "s.nm_season = ? AND " +
                     "r.no_round = ? AND " +
                     "ht.nm_team = ? AND " +
                     "at.nm_team = ?" ) )
        {
            statement.setString( 1, match.getSeason() );
            statement.setInt( 2, match.getRound() );
            statement.setString( 3, match.getHomeTeam().getName() );
            statement.setString( 4, match.getAwayTeam().getName() );
            try( final ResultSet resultSet = statement.executeQuery() )
            {
                resultSet.next();
                final int count = resultSet.getInt( 1 );
                if( count < 1 )
                {
                    throw new RuntimeException(
                        "Could not find match between " + match.getHomeTeam().getName() +
                        " and " + match.getAwayTeam().getName() + " in round " +
                        match.getRound() + " of the " + match.getSeason() + " season" );
                }
                else if( count > 1 )
                {
                    throw new IllegalStateException(
                        "Found multiple matches between " + match.getHomeTeam().getName() +
                        " and " + match.getAwayTeam().getName() + " in round " +
                        match.getRound() + " of the " + match.getSeason() + " season" );
                }
            }
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    private static void checkPlayerExistence( final Match match,
                                              final Connection connection )
    {
        Stream.of( match.getHomeTeam(), match.getAwayTeam() ).forEach( t ->
        {
            for( final String playerName : t.getPlayers() )
            {
                try( final PreparedStatement statement = connection.prepareStatement(
                        "SELECT COUNT(*) FROM player p " +
                        "INNER JOIN team t ON " +
                        "t.id_team = p.id_team " +
                        "INNER JOIN season s ON " +
                        "s.id_season = t.id_season " +
                        "WHERE " +
                            "p.nm_given = ? AND " +
                            "p.nm_family = ? AND " +
                            "t.nm_team = ? AND " +
                            "s.nm_season = ?" ) )
                {
                    final String[] playerNames = playerName.split( "\\s+" );
                    final String givenNames =
                        Arrays.stream( playerNames, 0, playerNames.length - 1 )
                            .collect( Collectors.joining( " " ) );
                    final String familyName = playerNames[playerNames.length - 1];
                    statement.setString( 1, givenNames  );
                    statement.setString( 2, familyName );
                    statement.setString( 3, t.getName() );
                    statement.setString( 4, match.getSeason() );
                    try( final ResultSet resultSet = statement.executeQuery() )
                    {
                        resultSet.next();
                        final int count = resultSet.getInt( 1 );
                        if( count != 1 )
                        {
                            throw new RuntimeException(
                                "Could not find player \"" + playerName + "\" in team \"" +
                                t.getName() + "\" in season \"" + match.getSeason() + "\"" );
                        }
                    }
                }
                catch( final SQLException e )
                {
                    throw new RuntimeException( e );
                }
            }
        } );
    }

    private static void createMatchTeamPlayerIfRequired( final Match match,
                                                         final Connection connection )
    {
        final UUID matchId = getMatchId( match, connection );
        Stream.of( match.getHomeTeam(), match.getAwayTeam() ).forEach( t ->
        {
            final UUID teamId = getTeamId( match.getSeason(), t.getName(), connection );
            final UUID matchTeamId = getMatchTeamId( matchId, teamId, connection );
            for( final String player : t.getPlayers() )
            {
                final UUID playerId =
                    getPlayerId( match.getSeason(), t.getName(), player, connection );
                final UUID matchTeamPlayerId =
                    getMatchTeamPlayerId( matchTeamId, playerId, connection );
                if( matchTeamPlayerId == null )
                {
                    createMatchTeamPlayer( playerId, teamId, matchTeamId, connection );
                }
            }
        } );
    }

    private static void recordScore( final Match match, final Connection connection )
    {
        final UUID matchId = getMatchId( match, connection );
        checkNoMatchGoals( matchId, connection );
        Stream.of( match.getHomeTeam(), match.getAwayTeam() ).forEach( t ->
        {
            final UUID teamId = getTeamId( match.getSeason(), t.getName(), connection );
            final UUID matchTeamId = getMatchTeamId( matchId, teamId, connection );
            for( final Goal goal : t.getGoals() )
            {
                if( !Objects.equals( goal.getOpponentOwnGoal(), Boolean.TRUE ) )
                {
                    final UUID playerId =
                        getPlayerId( match.getSeason(),
                                     t.getName(),
                                     goal.getScorer(),
                                     connection );
                    final UUID matchTeamPlayerId =
                        getMatchTeamPlayerId( matchTeamId, playerId, connection );
                    addGoalWithScorer( matchTeamId, matchTeamPlayerId, connection );
                }
                else
                {
                    addOpponentOwnGoal( matchTeamId, connection );
                }
            }
        } );
    }

    private static void recordGoldenBallVotes( final Match match, final Connection connection )
    {
        final UUID matchId = getMatchId( match, connection );
        checkNoMatchVotes( matchId, connection );
        Stream.of( match.getHomeTeam(), match.getAwayTeam() ).forEach( t ->
        {
            final UUID teamId = getTeamId( match.getSeason(), t.getName(), connection );
            final UUID matchTeamId = getMatchTeamId( matchId, teamId, connection );
            final UUID threeVoteMatchTeamPlayerId =
                Optional.of( t.getVotes() )
                    .map( Votes::getThreeVotesPlayer )
                    .map( p ->
                    {
                        return getMatchTeamPlayerId( matchTeamId,
                                                     getPlayerId( match.getSeason(),
                                                                  t.getName(),
                                                                  p,
                                                                  connection ),
                                                     connection );
                    } )
                    .orElse( null );
            final UUID twoVoteMatchTeamPlayerId =
                Optional.of( t.getVotes() )
                .map( Votes::getTwoVotesPlayer )
                .map( p ->
                {
                    return getMatchTeamPlayerId( matchTeamId,
                                                 getPlayerId( match.getSeason(),
                                                              t.getName(),
                                                              p,
                                                              connection ),
                                                 connection );
                } )
                .orElse( null );
            final UUID oneVoteMatchTeamPlayerId =
                Optional.of( t.getVotes() )
                .map( Votes::getOneVotePlayer )
                .map( p ->
                {
                    return getMatchTeamPlayerId( matchTeamId,
                                                 getPlayerId( match.getSeason(),
                                                              t.getName(),
                                                              p,
                                                              connection ),
                                                 connection );
                } )
                .orElse( null );


            addGoldenBallVotes( matchTeamId,
                                threeVoteMatchTeamPlayerId,
                                twoVoteMatchTeamPlayerId,
                                oneVoteMatchTeamPlayerId,
                                connection );
        } );
    }

    private static void setMatchCompleted( final Match match, final Connection connection )
    {
        final UUID matchId = getMatchId( match, connection );
        try( final PreparedStatement statement = connection.prepareStatement(
                 "UPDATE match SET cd_status = 'COMPLETED' WHERE id_match = ?" ) )
        {
            statement.setObject( 1, matchId );
            statement.executeUpdate();
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    private static UUID getMatchId( final Match match, final Connection connection )
    {
        try( final PreparedStatement statement = connection.prepareStatement(
                 "SELECT m.id_match FROM match m " +
                 "INNER JOIN round r ON " +
                 "r.id_round = m.id_round " +
                 "INNER JOIN season s ON " +
                 "s.id_season = r.id_season " +
                 "INNER JOIN match_team hmt ON " +
                 "hmt.id_match = m.id_match AND " +
                 "hmt.cd_team_type = 'HOME' " +
                 "INNER JOIN match_team amt ON " +
                 "amt.id_match = m.id_match AND " +
                 "amt.cd_team_type = 'AWAY' " +
                 "INNER JOIN team ht ON " +
                 "ht.id_team = hmt.id_team " +
                 "INNER JOIN team at ON " +
                 "at.id_team = amt.id_team " +
                 "WHERE " +
                     "s.nm_season = ? AND " +
                     "r.no_round = ? AND " +
                     "ht.nm_team = ? AND " +
                     "at.nm_team = ?" ) )
        {
            statement.setString( 1, match.getSeason() );
            statement.setInt( 2, match.getRound() );
            statement.setString( 3, match.getHomeTeam().getName() );
            statement.setString( 4, match.getAwayTeam().getName() );
            try( final ResultSet resultSet = statement.executeQuery() )
            {
                resultSet.next();
                return (UUID)resultSet.getObject( 1 );
            }
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    private static UUID getPlayerId( final String season,
                                     final String team,
                                     final String player,
                                     final Connection connection )
    {
        try( final PreparedStatement statement = connection.prepareStatement(
                "SELECT p.id_player FROM player p " +
                "INNER JOIN team t ON " +
                "t.id_team = p.id_team " +
                "INNER JOIN season s ON " +
                "s.id_season = t.id_season " +
                "WHERE " +
                    "p.nm_given = ? AND " +
                    "p.nm_family = ? AND " +
                    "t.nm_team = ? AND " +
                    "s.nm_season = ?" ) )
        {
            final String[] playerNames = player.split( "\\s+" );
            final String givenNames =
                Arrays.stream( playerNames, 0, playerNames.length - 1 )
                    .collect( Collectors.joining( " " ) );
            final String familyName = playerNames[playerNames.length - 1];
            statement.setString( 1, givenNames  );
            statement.setString( 2, familyName );
            statement.setString( 3, team );
            statement.setString( 4, season );
            try( final ResultSet resultSet = statement.executeQuery() )
            {
                resultSet.next();
                return (UUID)resultSet.getObject( 1 );
            }
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    private static UUID getTeamId( final String season,
                                   final String team,
                                   final Connection connection )
    {
        try( final PreparedStatement statement = connection.prepareStatement(
                 "SELECT t.id_team FROM season s " +
                 "INNER JOIN team t ON " +
                 "t.id_season = s.id_season " +
                 "WHERE " +
                     "s.nm_season = ? AND " +
                     "t.nm_team = ?" ) )
        {
            statement.setString( 1, season );
            statement.setString( 2, team );
            try( final ResultSet resultSet = statement.executeQuery() )
            {
                resultSet.next();
                return (UUID)resultSet.getObject( 1 );
            }
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    private static UUID getMatchTeamId( final UUID matchId,
                                        final UUID teamId,
                                        final Connection connection )
    {
        try( final PreparedStatement statement = connection.prepareStatement(
                 "SELECT id_match_team FROM match_team WHERE " +
                 "id_match = ? AND id_team = ?" ) )
        {
            statement.setObject( 1, matchId );
            statement.setObject( 2, teamId );
            try( final ResultSet resultSet = statement.executeQuery() )
            {
                resultSet.next();
                return (UUID)resultSet.getObject( 1 );
            }
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    private static UUID getMatchTeamPlayerId( final UUID matchTeamId,
                                              final UUID playerId,
                                              final Connection connection )
    {
        try( final PreparedStatement statement = connection.prepareStatement(
                 "SELECT id_match_team_player FROM match_team_player WHERE " +
                 "id_match_team = ? AND id_player = ?" ) )
        {
            statement.setObject( 1, matchTeamId );
            statement.setObject( 2, playerId );
            try( final ResultSet resultSet = statement.executeQuery() )
            {
                if( resultSet.next() )
                {
                    return (UUID)resultSet.getObject( 1 );
                }
                else
                {
                    return null;
                }
            }
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    private static void createMatchTeamPlayer( final UUID playerId,
                                               final UUID teamId,
                                               final UUID matchTeamId,
                                               final Connection connection )
    {
        final int nextMatchTeamPlayerNumber;
        try( final PreparedStatement statement = connection.prepareStatement(
                 "SELECT no_match_team_player FROM match_team_player WHERE " +
                 "id_match_team = ? " +
                 "ORDER BY no_match_team_player DESC" ) )
        {
            statement.setObject( 1, matchTeamId );
            try( final ResultSet resultSet = statement.executeQuery() )
            {
                if( resultSet.next() )
                {
                    nextMatchTeamPlayerNumber = resultSet.getInt( 1 ) + 1;
                }
                else
                {
                    nextMatchTeamPlayerNumber = 1;
                }
            }
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }

        try( final PreparedStatement statement = connection.prepareStatement(
                 "INSERT INTO match_team_player " +
                 "( " +
                     "id_match_team_player, " +
                     "id_player, " +
                     "id_team, " +
                     "id_match_team, " +
                     "no_match_team_player " +
                 ") " +
                 "VALUES " +
                 "( " +
                     "RANDOM_UUID(), " +
                     "?, " +
                     "?, " +
                     "?, " +
                     "? " +
                 ")" ) )
        {
            statement.setObject( 1, playerId );
            statement.setObject( 2, teamId );
            statement.setObject( 3, matchTeamId );
            statement.setInt( 4, nextMatchTeamPlayerNumber );
            statement.executeUpdate();

        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    private static void checkNoMatchGoals( final UUID matchId,
                                           final Connection connection )
    {
        try( final PreparedStatement statement = connection.prepareStatement(
                 "SELECT COUNT(*) FROM goal g " +
                 "INNER JOIN match_team mt ON " +
                 "mt.id_match_team = g.id_match_team " +
                 "WHERE mt.id_match = ?" ) )
        {
            statement.setObject( 1, matchId );
            try( final ResultSet resultSet = statement.executeQuery() )
            {
                resultSet.next();
                final int count = resultSet.getInt( 1 );
                if( count > 0 )
                {
                    throw new RuntimeException(
                        "Goals already recorded against match with ID " + matchId );
                }
            }
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    private static void addOpponentOwnGoal( final UUID matchTeamId,
                                            final Connection connection )
    {
        try( final PreparedStatement statement = connection.prepareStatement(
                 "INSERT INTO goal " +
                 "( " +
                     "id_goal, " +
                     "id_match_team, " +
                     "fl_opponent_own_goal " +
                 ") " +
                 "VALUES " +
                 "( " +
                     "RANDOM_UUID(), " +
                     "?, " +
                     "TRUE " +
                 ")" ) )
        {
            statement.setObject( 1, matchTeamId );
            statement.executeUpdate();
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    private static void addGoalWithScorer( final UUID matchTeamId,
                                           final UUID matchTeamPlayerId,
                                           final Connection connection )
    {
        try( final PreparedStatement statement = connection.prepareStatement(
                 "INSERT INTO goal " +
                 "( " +
                     "id_goal, " +
                     "id_match_team, " +
                     "id_match_team_player " +
                 ") " +
                 "VALUES " +
                 "( " +
                     "RANDOM_UUID(), " +
                     "?, " +
                     "? " +
                 ")" ) )
        {
            statement.setObject( 1, matchTeamId );
            statement.setObject( 2, matchTeamPlayerId );
            statement.executeUpdate();
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    private static void checkNoMatchVotes( final UUID matchId,
                                           final Connection connection )
    {
        try( final PreparedStatement statement = connection.prepareStatement(
                 "SELECT COUNT(*) FROM golden_ball_vote gbv " +
                 "INNER JOIN match_team mt ON " +
                 "mt.id_match_team = gbv.id_match_team " +
                 "WHERE mt.id_match = ?" ) )
        {
            statement.setObject( 1, matchId );
            try( final ResultSet resultSet = statement.executeQuery() )
            {
                resultSet.next();
                final int count = resultSet.getInt( 1 );
                if( count > 0 )
                {
                    throw new RuntimeException(
                        "Golden ball votes already recorded against match with ID " + matchId );
                }
            }
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }

    private static void addGoldenBallVotes( final UUID matchTeamId,
                                            final UUID threeVoteMatchTeamPlayerId,
                                            final UUID twoVoteMatchTeamPlayerId,
                                            final UUID oneVoteMatchTeamPlayerId,
                                            final Connection connection )
    {
        Stream.of( Pair.of( 3, threeVoteMatchTeamPlayerId ),
                   Pair.of( 2, twoVoteMatchTeamPlayerId ),
                   Pair.of( 1, oneVoteMatchTeamPlayerId ) )
            .filter( p -> p.getRight() != null )
            .forEach( p ->
        {
            try( final PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO golden_ball_vote " +
                     "( " +
                         "id_golden_ball_vote, " +
                         "id_match_team_player, " +
                         "id_match_team, " +
                         "no_votes " +
                     ") " +
                     "VALUES " +
                     "( " +
                         "RANDOM_UUID(), " +
                         "?, " +
                         "?, " +
                         "? " +
                     ")" ) )
            {
                statement.setObject( 1, p.getRight() );
                statement.setObject( 2, matchTeamId );
                statement.setInt( 3, p.getLeft() );
                statement.executeUpdate();
            }
            catch( final SQLException e )
            {
                throw new RuntimeException( e );
            }
        } );
    }
}
