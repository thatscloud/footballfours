package com.footballfours.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class UrlCodecUtils
{
    private UrlCodecUtils(){}

    public static String encode( final String str )
    {
        try
        {
            return URLEncoder.encode( str, StandardCharsets.UTF_8.toString() );
        }
        catch( final UnsupportedEncodingException e )
        {
            throw new RuntimeException( e );
        }
    }

    public static String decode( final String str )
    {
        try
        {
            return URLDecoder.decode( str, StandardCharsets.UTF_8.toString() );
        }
        catch( final UnsupportedEncodingException e )
        {
            throw new RuntimeException( e );
        }
    }
}
