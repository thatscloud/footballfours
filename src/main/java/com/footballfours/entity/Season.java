package com.footballfours.entity;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "season" )
public class Season
{
    public UUID mySeasonId;
    public String mySeasonName;

    @Id
    @Column( name = "id_season" )
    public UUID getSeasonId()
    {
        return mySeasonId;
    }

    public void setSeasonId( final UUID seasonId )
    {
        mySeasonId = seasonId;
    }

    @Column( name = "nm_season" )
    public String getSeasonName()
    {
        return mySeasonName;
    }

    public void setSeasonName( final String seasonName )
    {
        mySeasonName = seasonName;
    }

    @Override
    public int hashCode()
    {
        return mySeasonId == null ? 0 : mySeasonId.hashCode();
    }

    @Override
    public boolean equals( final Object obj )
    {
        return obj instanceof Season &&
               Objects.equals( mySeasonId,
                              ( (Season)obj ).mySeasonId );
    }
}
