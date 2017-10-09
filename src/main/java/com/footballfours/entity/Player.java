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
@Table( name = "player" )
public class Player
{
    private UUID myPlayerId;
    private int myTeamPlayerNumber;
    private String myGivenName;
    private String myFamilyName;
    private Team myTeam;

    @Id
    @GeneratedValue
    @Column( name = "id_player" )
    public UUID getPlayerId()
    {
        return myPlayerId;
    }

    public void setPlayerId( final UUID playerId )
    {
        myPlayerId = playerId;
    }

    @Column( name = "no_team_player" )
    public int getTeamPlayerNumber()
    {
        return myTeamPlayerNumber;
    }

    public void setTeamPlayerNumber( final int teamPlayerNumber )
    {
        myTeamPlayerNumber = teamPlayerNumber;
    }

    @Column( name = "nm_given" )
    public String getGivenName()
    {
        return myGivenName;
    }

    public void setGivenName( final String givenName )
    {
        myGivenName = givenName;
    }

    @Column( name = "nm_family" )
    public String getFamilyName()
    {
        return myFamilyName;
    }

    public void setFamilyName( final String familyName )
    {
        myFamilyName = familyName;
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

    @Override
    public int hashCode()
    {
        return myPlayerId == null ? 0 : myPlayerId.hashCode();
    }

    @Override
    public boolean equals( final Object obj )
    {
        return obj instanceof Player &&
               Objects.equals( myPlayerId,
                              ( (Player)obj ).myPlayerId );
    }
}
