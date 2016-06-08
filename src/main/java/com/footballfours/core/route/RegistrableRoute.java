package com.footballfours.core.route;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.footballfours.core.JsonTransformer;
import com.j256.ormlite.support.ConnectionSource;

import spark.Request;
import spark.Response;
import spark.Route;

public abstract class RegistrableRoute implements Route
{
    private final ConnectionSource connectionSource;
    private final Connection connection;

    private final JsonTransformer jsonTransformer = new JsonTransformer();

    public RegistrableRoute( ConnectionSource connectionSource ,
                             Connection connection)
    {
        this.connectionSource = connectionSource;
        this.connection = connection;
    }

    public ConnectionSource getConnectionSource()
    {
        return connectionSource;
    }

    public Connection getConnection()
    {
        return connection;
    }
    
    public JsonTransformer getJsonTransformer()
    {
        return jsonTransformer;
    }

    @Override
    public Object handle( Request request, Response response ) throws Exception
    {
        response.redirect( getFullUrl( request, "/404" ) );
        return response.raw();
    }

    protected String getFullUrl( Request request, String path )
    {
        StringBuilder builder = new StringBuilder( request.host() );
        builder.insert( 0, "http://" );
        builder.append( path );
        return builder.toString();
    }

    protected Map<String, Object> getNewPageModel( Request request )
    {
        Map<String, Object> page = new HashMap<>();
        page.put( "user", request.session( ).attribute( "user" ) );
        return page;
    }

    public abstract void register();

}
