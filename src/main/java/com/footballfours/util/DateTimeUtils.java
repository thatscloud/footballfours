package com.footballfours.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateTimeUtils
{
    public static Timestamp convertLocalDateTimeToTimestamp(
        final LocalDateTime localDateTime )
    {
        return new Timestamp(
            localDateTime
                .atZone( ZoneId.of( "Australia/Sydney" ) )
                .toInstant().toEpochMilli() );
    }

    public static LocalDateTime convertTimestampToLocalDateTime(
        final Timestamp timestamp )
    {
        return Instant.ofEpochMilli( timestamp.getTime() )
                      .atZone( ZoneId.of( "Australia/Sydney" ) )
                      .toLocalDateTime();
    }
}
