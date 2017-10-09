package com.footballfours.entity;

import java.time.Instant;
import java.util.Map;
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
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.footballfours.entity.constant.MatchStatusCode;
import com.footballfours.entity.constant.MatchTeamTeamTypeCode;

@Entity
@Table( name = "match" )
public class Match
{
    private UUID myMatchId;
    private Instant myScheduledDateTime;
    private Instant myPlayedDateTime;
    private MatchStatusCode myStatusCode;
    private Round myRound;
    private Season mySeason;
    private Map<MatchTeamTeamTypeCode, MatchTeam> myMatchTeams;

    @Id
    @GeneratedValue
    @Column( name = "id_match" )
    public UUID getMatchId()
    {
        return myMatchId;
    }

    public void setMatchId( final UUID matchId )
    {
        myMatchId = matchId;
    }

    @Column( name = "dt_scheduled" )
    public Instant getScheduledDateTime()
    {
        return myScheduledDateTime;
    }

    public void setScheduledDateTime( final Instant scheduledDateTime )
    {
        myScheduledDateTime = scheduledDateTime;
    }

    @Column( name = "dt_played" )
    public Instant getPlayedDateTime()
    {
        return myPlayedDateTime;
    }

    public void setPlayedDateTime( final Instant playedDateTime )
    {
        myPlayedDateTime = playedDateTime;
    }

    @Column( name = "cd_status" )
    @Enumerated( EnumType.STRING )
    public MatchStatusCode getStatusCode()
    {
        return myStatusCode;
    }

    public void setStatusCode( final MatchStatusCode statusCode )
    {
        myStatusCode = statusCode;
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

    @OneToMany( mappedBy = "match" )
    @MapKey( name = "teamTypeCode" )
    public Map<MatchTeamTeamTypeCode, MatchTeam> getMatchTeams()
    {
        return myMatchTeams;
    }

    public void setMatchTeams( final Map<MatchTeamTeamTypeCode, MatchTeam> matchTeams )
    {
        myMatchTeams = matchTeams;
    }

    @Transient
    public MatchTeam getHomeMatchTeam()
    {
        return myMatchTeams.get( MatchTeamTeamTypeCode.HOME );
    }

    @Transient
    public MatchTeam getAwayMatchTeam()
    {
        return myMatchTeams.get( MatchTeamTeamTypeCode.AWAY );
    }

    @Override
    public int hashCode()
    {
        return myMatchId == null ? 0 : myMatchId.hashCode();
    }

    @Override
    public boolean equals( final Object obj )
    {
        return obj instanceof Match &&
               Objects.equals( myMatchId,
                              ( (Match)obj ).myMatchId );
    }

}
