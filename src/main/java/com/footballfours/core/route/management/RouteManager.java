package com.footballfours.core.route.management;

import static spark.Spark.before;

import java.nio.charset.StandardCharsets;
import java.util.Set;

import org.reflections.Reflections;

import com.footballfours.core.route.RegistrableRoute;
import com.footballfours.route.StaticContentRoute;
import com.j256.ormlite.support.ConnectionSource;

public class RouteManager
{

    public static void insertRoutes( ConnectionSource connectionSource )
                                                                        throws InstantiationException,
                                                                        IllegalAccessException
    {
        before( ( req, res ) -> {
            res.raw().setCharacterEncoding( StandardCharsets.UTF_8.toString() );
            if ( req.pathInfo().endsWith( ".html" ) )
            {
                res.raw().setContentType( "text/html; charset=utf-8" );
            }
            else if ( req.pathInfo().endsWith( ".css" ) )
            {
                res.raw().setContentType( "text/css; charset=utf-8" );
            }
        } );

        Reflections reflections = new Reflections( "com.footballfours.route" );
        // Register the other Registrable Routes
        try
        {
            Set<Class<? extends RegistrableRoute>> allClasses = reflections
                .getSubTypesOf( RegistrableRoute.class );

            allClasses.remove( StaticContentRoute.class );
            for ( Class<? extends RegistrableRoute> clazz : allClasses )
            {
                clazz.getConstructor( ConnectionSource.class )
                    .newInstance( connectionSource ).register();
                System.out.println( "Route Registered: " + clazz.getName() );
            }

            System.out
                .println( "Registering Static Content Route (this must be done last)." );
            new StaticContentRoute( connectionSource ).register();
            System.out.println( "Route Registered: "
                    + StaticContentRoute.class.getName() );

            System.out.println( "All Routes Registered." );

        }
        catch ( Exception e )
        {
            throw new RuntimeException( "Couldn't register all routes.", e );
        }

    }
}
