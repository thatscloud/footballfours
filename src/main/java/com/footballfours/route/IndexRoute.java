package com.footballfours.route;

import static spark.Spark.get;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;

import org.apache.commons.io.IOUtils;

import spark.Request;
import spark.Response;

import com.footballfours.core.route.RegistrableRoute;
import com.j256.ormlite.support.ConnectionSource;

public class IndexRoute extends RegistrableRoute
{

    public IndexRoute( final Connection connection )
    {
        super( connection );
    }

    @Override
    public Object handle( final Request request,
                          final Response response ) throws Exception
    {
        try (final InputStream in = getClass().getClassLoader()
            .getResourceAsStream( "com/footballfours/ng/pages/index.html" );
                final OutputStream out = response.raw().getOutputStream())
        {
            IOUtils.copy( in, out );
        }
        catch ( Exception e )
        {
            response.redirect( getFullUrl( request, "/404" ) );
        }
        return response.raw();
    }

    @Override
    public void register()
    {
        get( "/", this );
        get( "/index", this );
    }

}
