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
@Table( name = "match_team_player" )
public class MatchTeamPlayer
{
    private UUID myMatchTeamPlayerId;
    private int myMatchTeamPlayerNumber;
    private Player myPlayer;
    private Team myTeam;
    private MatchTeam myMatchTeam;

    @Id
    @GeneratedValue
    @Column( name = "id_match_team_player" )
    public UUID getMatchTeamPlayerId()
    {
        return myMatchTeamPlayerId;
    }

    public void setMatchTeamPlayerId( final UUID matchTeamPlayerId )
    {
        myMatchTeamPlayerId = matchTeamPlayerId;
    }

    @Column( name = "no_match_team_player" )
    public int getMatchTeamPlayerNumber()
    {
        return myMatchTeamPlayerNumber;
    }

    public void setMatchTeamPlayerNumber( final int matchTeamPlayerNumber )
    {
        myMatchTeamPlayerNumber = matchTeamPlayerNumber;
    }

    @ManyToOne
    @JoinColumn( name = "id_player" )
    public Player getPlayer()
    {
        return myPlayer;
    }

    public void setPlayer( final Player player )
    {
        myPlayer = player;
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
    @JoinColumn( name = "id_match_team")
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
        return myMatchTeamPlayerId == null ? 0 : myMatchTeamPlayerId.hashCode();
    }

    @Override
    public boolean equals( final Object obj )
    {
        return obj instanceof MatchTeamPlayer &&
               Objects.equals( myMatchTeamPlayerId,
                              ( (MatchTeamPlayer)obj ).myMatchTeamPlayerId );
    }
}
