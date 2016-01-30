package com.footballfours.model.fixture;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Match
{
    private static final DateTimeFormatter DAY_OF_WEEK_NAME_FORMATTER =
        DateTimeFormatter.ofPattern( "EEEE" );
    private static final DateTimeFormatter DAY_OF_MONTH_FORMATTER =
        DateTimeFormatter.ofPattern( "d" );
    private static final DateTimeFormatter MONTH_AND_YEAR_FORMATTER =
        DateTimeFormatter.ofPattern( "MMMM yyyy" );
    private static final DateTimeFormatter TIME_FORMATTER =
        DateTimeFormatter.ofPattern( "hh:mma" );

    private static String localDateTimeToPrettyDateString( final LocalDateTime ldt )
    {
        return ldt.format( DAY_OF_WEEK_NAME_FORMATTER ) + ", " +
               ldt.format( DAY_OF_MONTH_FORMATTER ) +
               getOrdinal( ldt.getDayOfMonth() ) + " " +
               ldt.format( MONTH_AND_YEAR_FORMATTER );
    }

    private static String localDateTimeToPrettyTimeString( final LocalDateTime ldt )
    {
        return ldt.format( TIME_FORMATTER );
    }

    private static String getOrdinal( final int dayOfMonth )
    {
        if( dayOfMonth >= 11 && dayOfMonth <= 13 )
        {
            return "th";
        }
        switch ( dayOfMonth % 10 )
        {
            case 1: return "st";
            case 2: return "nd";
            case 3: return "rd";
            default: return "th";
        }
    }

    private Team myHomeTeam;
    private Team myAwayTeam;
    private LocalDateTime myScheduledDateTime;
    private LocalDateTime myPlayedDateTime;
    private String myStatus;

    public Team getHomeTeam()
    {
        return myHomeTeam;
    }

    public void setHomeTeam( final Team homeTeam )
    {
        myHomeTeam = homeTeam;
    }

    public Team getAwayTeam()
    {
        return myAwayTeam;
    }

    public void setAwayTeam( final Team awayTeam )
    {
        myAwayTeam = awayTeam;
    }

    public LocalDateTime getScheduledDateTime()
    {
        return myScheduledDateTime;
    }

    public void setScheduledDateTime( final LocalDateTime scheduledDateTime )
    {
        myScheduledDateTime = scheduledDateTime;
    }

    public LocalDateTime getPlayedDateTime()
    {
        return myPlayedDateTime;
    }

    public void setPlayedDateTime( final LocalDateTime playedDateTime )
    {
        myPlayedDateTime = playedDateTime;
    }

    public String getScheduledDatePretty()
    {
        return localDateTimeToPrettyDateString( myScheduledDateTime );
    }

    public String getPlayedDatePretty()
    {
        return localDateTimeToPrettyDateString( myPlayedDateTime );
    }

    public String getScheduledTimePretty()
    {
        return localDateTimeToPrettyTimeString( myScheduledDateTime );
    }

    public String getPlayedTimePretty()
    {
        return localDateTimeToPrettyTimeString( myPlayedDateTime );
    }

    public String getStatus()
    {
        return myStatus;
    }

    public void setStatus( final String status )
    {
        myStatus = status;
    }

    public boolean isCompleted()
    {
        return Objects.equals( myStatus, "COMPLETED" );
    }

    public boolean isRescheduled()
    {
        return !Objects.equals( myScheduledDateTime, myPlayedDateTime );
    }
}
