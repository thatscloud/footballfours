package com.footballfours.entity;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table( name = "goal" )
public class Goal
{
    private UUID myGoalId;
    private boolean myOpponentOwnGoal;
    private MatchTeam myMatchTeam;
    private MatchTeamPlayer myMatchTeamPlayer;

    @Id
    @GeneratedValue
    @Column( name = "id_goal" )
    public UUID getGoalId()
    {
        return myGoalId;
    }

    public void setGoalId( final UUID goalId )
    {
        myGoalId = goalId;
    }

    @Column( name = "fl_opponent_own_goal" )
    public boolean isOpponentOwnGoal()
    {
        return myOpponentOwnGoal;
    }

    public void setOpponentOwnGoal( final boolean opponentOwnGoal )
    {
        myOpponentOwnGoal = opponentOwnGoal;
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

    @ManyToOne
    @JoinColumn( name = "id_match_team_player" )
    public MatchTeamPlayer getMatchTeamPlayer()
    {
        return myMatchTeamPlayer;
    }

    public void setMatchTeamPlayer( final MatchTeamPlayer matchTeamPlayer )
    {
        myMatchTeamPlayer = matchTeamPlayer;
    }

    @Override
    public int hashCode()
    {
        return myGoalId == null ? 0 : myGoalId.hashCode();
    }

    @Override
    public boolean equals( final Object obj )
    {
        return obj instanceof Goal &&
               Objects.equals( myGoalId,
                              ( (Goal)obj ).myGoalId );
    }
}
