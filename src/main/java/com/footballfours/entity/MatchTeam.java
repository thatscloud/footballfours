package com.footballfours.entity;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
