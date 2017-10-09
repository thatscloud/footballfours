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
@Table( name = "round" )
public class Round
{
    private UUID myRoundId;
    private int myRoundNumber;
    private Season mySeason;

    @Id
    @GeneratedValue
    @Column( name = "id_round" )
    public UUID getRoundId()
    {
        return myRoundId;
    }

    public void setRoundId( final UUID roundId )
    {
        myRoundId = roundId;
    }

    @Column( name = "no_round" )
    public int getRoundNumber()
    {
        return myRoundNumber;
    }

    public void setRoundNumber( final int roundNumber )
    {
        myRoundNumber = roundNumber;
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
        return myRoundId == null ? 0 : myRoundId.hashCode();
    }

    @Override
    public boolean equals( final Object obj )
    {
        return obj instanceof Round &&
               Objects.equals( myRoundId,
                              ( (Round)obj ).myRoundId );
    }

}
