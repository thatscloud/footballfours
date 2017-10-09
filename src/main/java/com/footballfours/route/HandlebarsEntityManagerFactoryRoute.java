package com.footballfours.route;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.footballfours.persist.RunAgainstDatabase;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

import spark.Request;
import spark.Response;
import spark.Route;

public class HandlebarsEntityManagerFactoryRoute implements Route
{
    private final Handlebars myHandlebars;
    private final EntityManagerFactory myEntityManagerFactory;
    private final String myTemplateName;
    private final Function<EntityManager, Object> myModelGenerator;

    public HandlebarsEntityManagerFactoryRoute(
        final Handlebars handlebars,
        final EntityManagerFactory entityManagerFactory,
        final String templateName,
        final Function<EntityManager, Object> modelGenerator )
    {
        myHandlebars = handlebars;
        myEntityManagerFactory = entityManagerFactory;
        myTemplateName = templateName;
        myModelGenerator = modelGenerator;
    }

    @Override
    public Object handle( final Request request, final Response response )
        throws Exception
    {
        RunAgainstDatabase.run( myEntityManagerFactory, entityManager ->
        {
            try
            {
                final Object model = myModelGenerator.apply( entityManager );
                final Template template =
                    myHandlebars.compile( myTemplateName );
                try( final Writer writer = response.raw().getWriter() )
                {
                    template.apply( model, writer );
                }
            }
            catch( final Exception e )
            {
                if( e instanceof RuntimeException )
                {
                    throw (RuntimeException)e;
                }
                if( e instanceof IOException )
                {
                    throw new UncheckedIOException( (IOException)e );
                }
                else
                {
                    throw new RuntimeException( e );
                }
            }
        } );
        return response.raw();
    }
}
