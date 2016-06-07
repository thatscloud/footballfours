package com.footballfours.core.route;

import java.util.HashMap;
import java.util.Map;

import spark.Request;
import spark.Response;
import spark.Route;

import com.footballfours.core.JsonTransformer;
import com.footballfours.core.user.User;
import com.j256.ormlite.support.ConnectionSource;

public abstract class RegistrableRoute implements Route
{
    private final ConnectionSource connectionSource;

    private final JsonTransformer jsonTransformer = new JsonTransformer();

    public RegistrableRoute( ConnectionSource connectionSource )
    {
        this.connectionSource = connectionSource;
    }

    public ConnectionSource getConnectionSource()
    {
        return connectionSource;
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
