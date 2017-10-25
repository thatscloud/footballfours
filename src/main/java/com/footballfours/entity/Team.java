package com.footballfours.entity;

import java.util.Comparator;
import java.util.Objects;
import java.util.SortedSet;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.SortComparator;

@Entity
@Table( name = "team" )
public class Team
{
    private UUID myTeamId;
    private String myTeamName;
    private Season mySeason;
    private SortedSet<Player> myPlayers;

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

    public static class PlayerSortComparator implements Comparator<Player>
    {
        @Override
        public int compare( final Player o1, final Player o2 )
        {
            if( o1 == null )
            {
                return o2 == null ? 0 : 1;
            }

            if( o2 == null )
            {
                return -1;
            }

            return Integer.compare( o1.getTeamPlayerNumber(), o2.getTeamPlayerNumber() );
        }
    }

    @OneToMany( mappedBy = "team" )
    @SortComparator( PlayerSortComparator.class )
    public SortedSet<Player> getPlayers()
    {
        return myPlayers;
    }

    public void setPlayers( final SortedSet<Player> players )
    {
        myPlayers = players;
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
