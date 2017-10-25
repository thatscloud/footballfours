package com.footballfours.entity;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.SortedSet;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.SortComparator;

import com.footballfours.entity.constant.MatchTeamTeamTypeCode;

@Entity
@Table( name = "match_team" )
public class MatchTeam
{
    private UUID myMatchTeamId;
    private MatchTeamTeamTypeCode myTeamTypeCode;
    private Team myTeam;
    private Match myMatch;
    private Round myRound;
    private Season mySeason;
    private SortedSet<MatchTeamPlayer> myMatchTeamPlayers;
    private Collection<Goal> myGoals;
    private SortedSet<GoldenBallVote> myGoldenBallVotes;

    @Id
    @GeneratedValue
    @Column( name = "id_match_team" )
    public UUID getMatchTeamId()
    {
        return myMatchTeamId;
    }

    public void setMatchTeamId( final UUID matchTeamId )
    {
        myMatchTeamId = matchTeamId;
    }

    @Column( name = "cd_team_type" )
    @Enumerated( EnumType.STRING )
    public MatchTeamTeamTypeCode getTeamTypeCode()
    {
        return myTeamTypeCode;
    }

    public void setTeamTypeCode( final MatchTeamTeamTypeCode teamTypeCode )
    {
        myTeamTypeCode = teamTypeCode;
    }

    @ManyToOne
    @JoinColumn( name = "id_team" )
    public Team getTeam()
    {
        return myTeam;
    }

    public void setTeam( final Team team )
    {
        myTeam = team;
    }

    @ManyToOne
    @JoinColumn( name = "id_match" )
    public Match getMatch()
    {
        return myMatch;
    }

    public void setMatch( final Match match )
    {
        myMatch = match;
    }

    @ManyToOne
    @JoinColumn( name = "id_round" )
    public Round getRound()
    {
        return myRound;
    }

    public void setRound( final Round round )
    {
        myRound = round;
    }

    @ManyToOne
    @JoinColumn( name = "id_season" )
    public Season getSeason()
    {
        return mySeason;
    }

    public void setSeason( final Season season )
    {
        mySeason = season;
    }

    public static class MatchTeamPlayerSortComparator implements Comparator<MatchTeamPlayer>
    {
        @Override
        public int compare( final MatchTeamPlayer o1, final MatchTeamPlayer o2 )
        {
            if( o1 == null )
            {
                return o2 == null ? 0 : 1;
            }

            if( o2 == null )
            {
                return -1;
            }

            return Integer.compare( o1.getMatchTeamPlayerNumber(), o2.getMatchTeamPlayerNumber() );
        }
    }

    @OneToMany( mappedBy = "matchTeam" )
    @SortComparator( MatchTeamPlayerSortComparator.class )
    public SortedSet<MatchTeamPlayer> getMatchTeamPlayers()
    {
        return myMatchTeamPlayers;
    }

    public void setMatchTeamPlayers( final SortedSet<MatchTeamPlayer> matchTeamPlayers )
    {
        myMatchTeamPlayers = matchTeamPlayers;
    }

    @OneToMany( mappedBy = "matchTeam" )
    @SortComparator( MatchTeamPlayerSortComparator.class )
    public Collection<Goal> getGoals()
    {
        return myGoals;
    }

    public void setGoals( final Collection<Goal> goals )
    {
        myGoals = goals;
    }

    public static class GoldenBallVoteSortComparator implements Comparator<GoldenBallVote>
    {
        @Override
        public int compare( final GoldenBallVote o1, final GoldenBallVote o2 )
        {
            if( o1 == null )
            {
                return o2 == null ? 0 : 1;
            }

            if( o2 == null )
            {
                return -1;
            }

            // Descending order (NB. o2 and o1 reversed)
            return Integer.compare( o2.getVotesNumber(), o1.getVotesNumber() );
        }
    }

    @OneToMany( mappedBy = "matchTeam" )
    @SortComparator( GoldenBallVoteSortComparator.class )
    public SortedSet<GoldenBallVote> getGoldenBallVotes()
    {
        return myGoldenBallVotes;
    }

    public void setGoldenBallVotes( final SortedSet<GoldenBallVote> goldenBallVotes )
    {
        myGoldenBallVotes = goldenBallVotes;
    }

    @Override
    public int hashCode()
    {
        return myMatchTeamId == null ? 0 : myMatchTeamId.hashCode();
    }

    @Override
    public boolean equals( final Object obj )
    {
        return obj instanceof MatchTeam &&
               Objects.equals( myMatchTeamId,
                              ( (MatchTeam)obj ).myMatchTeamId );
    }
}
