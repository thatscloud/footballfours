package com.footballfours.entity;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table( name = "golden_ball_vote" )
public class GoldenBallVote
{
    private UUID myGoldenBallVoteId;
    private int myVotesNumber;
    private MatchTeamPlayer myMatchTeamPlayer;
    private MatchTeam myMatchTeam;

    @Id
    @GeneratedValue
    @Column( name = "id_golden_ball_vote" )
    public UUID getGoldenBallVoteId()
    {
        return myGoldenBallVoteId;
    }

    public void setGoldenBallVoteId( final UUID goldenBallVoteId )
    {
        myGoldenBallVoteId = goldenBallVoteId;
    }

    @Column( name = "no_votes" )
    public int getVotesNumber()
    {
        return myVotesNumber;
    }

    public void setVotesNumber( final int votesNumber )
    {
        myVotesNumber = votesNumber;
    }

    @OneToOne
    @JoinColumn( name = "id_match_team_player" )
    public MatchTeamPlayer getMatchTeamPlayer()
    {
        return myMatchTeamPlayer;
    }

    public void setMatchTeamPlayer( final MatchTeamPlayer matchTeamPlayer )
    {
        myMatchTeamPlayer = matchTeamPlayer;
    }

    @ManyToOne
    @JoinColumn( name = "id_match_team" )
    public MatchTeam getMatchTeam()
    {
        return myMatchTeam;
    }

    public void setMatchTeam( final MatchTeam matchTeam )
    {
        myMatchTeam = matchTeam;
    }

    @Override
    public int hashCode()
    {
        return myGoldenBallVoteId == null ? 0 : myGoldenBallVoteId.hashCode();
    }

    @Override
    public boolean equals( final Object obj )
    {
        return obj instanceof GoldenBallVote &&
               Objects.equals( myGoldenBallVoteId,
                              ( (GoldenBallVote)obj ).myGoldenBallVoteId );
    }
}
