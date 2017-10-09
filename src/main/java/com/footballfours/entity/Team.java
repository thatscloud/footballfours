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
@Table( name = "team" )
public class Team
{
    private UUID myTeamId;
    private String myTeamName;
    private Season mySeason;

    @Id
    @GeneratedValue
    @Column( name = "id_team" )
    public UUID getTeamId()
    {
        return myTeamId;
    }

    public void setTeamId( final UUID teamId )
    {
        myTeamId = teamId;
    }

    @Column( name = "nm_team" )
    public String getTeamName()
    {
        return myTeamName;
    }

    public void setTeamName( final String teamName )
    {
        myTeamName = teamName;
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

    @Override
    public int hashCode()
    {
        return myTeamId == null ? 0 : myTeamId.hashCode();
    }

    @Override
    public boolean equals( final Object obj )
    {
        return obj instanceof Team &&
               Objects.equals( myTeamId,
                              ( (Team)obj ).myTeamId );
    }
}
