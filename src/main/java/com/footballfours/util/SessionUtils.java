package com.footballfours.util;

import java.sql.SQLException;

import spark.Request;

import com.j256.ormlite.support.ConnectionSource;

public class SessionUtils
{

    public static boolean isAdminSession( Request request,
                                          ConnectionSource connectionSource )
                                                                             throws SQLException
    {
        return true;
    }
}
