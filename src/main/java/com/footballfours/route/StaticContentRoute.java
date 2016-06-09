package com.footballfours.route;

import static spark.Spark.get;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import spark.Request;
import spark.Response;
import spark.utils.IOUtils;

import com.footballfours.core.route.RegistrableRoute;
import com.j256.ormlite.support.ConnectionSource;

public class StaticContentRoute extends RegistrableRoute
{

    public StaticContentRoute( final Connection connection )
    {
        super( connection );
    }

    @Override
    public Object handle( final Request request,
                          final Response response ) throws Exception
    {
        String uriString;
        if ( request.splat() == null || request.splat().length == 0 )
        {
            uriString = "";
        }
        else
        {
            uriString = request.splat()[0];
        }

        boolean notFound = true;
        String fullResourceAddress = "";
        List<String> resourcePaths = Arrays.asList( new String[] {
                "com/footballfours/staticcontent/", "com/footballfours/ng/" } );
        for ( String resourcePath : resourcePaths )
        {
            fullResourceAddress = resourcePath + uriString;
            final URL url = getClass().getClassLoader()
                .getResource( resourcePath + uriString );
            if ( url == null )
            {
                notFound = true;
            }
            else
            {
                final Path path = Paths.get( url.toURI() );
                if ( Files.isDirectory( path ) )
                {
                    notFound = true;
                }
                else
                {
                    // resource found
                    notFound = false;
                    break;
                }
            }
        }

        if ( notFound )
        {
            fullResourceAddress = "com/footballfours/staticcontent/404.html";
        }

        try (final InputStream in = getClass().getClassLoader()
            .getResourceAsStream( fullResourceAddress );
                final OutputStream out = response.raw().getOutputStream())
        {
            IOUtils.copy( in, out );
        }
        return response.raw();
    }

    @Override
    public void register()
    {
        // Redirect all other pages to static content (or 404)
        get( "/*", this );
    }

}
