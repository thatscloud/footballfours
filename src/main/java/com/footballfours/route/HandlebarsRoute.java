package com.footballfours.route;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.sql.Connection;
import java.util.function.Function;

import javax.sql.DataSource;

import com.footballfours.persist.RunAgainstDataSource;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

import spark.Request;
import spark.Response;
import spark.Route;

public class HandlebarsRoute implements Route
{

    private final Handlebars myHandlebars;
    private final DataSource myDataSource;
    private final String myTemplateName;
    private final Function<Connection, Object> myModelGenerator;

    public HandlebarsRoute( final Handlebars handlebars,
                            final DataSource dataSource,
                            final String templateName,
                            final Function<Connection, Object> modelGenerator )
    {
        myHandlebars = handlebars;
        myDataSource = dataSource;
        myTemplateName = templateName;
        myModelGenerator = modelGenerator;
    }

    @Override
    public Object handle( final Request request, final Response response )
        throws Exception
    {
        RunAgainstDataSource.run( myDataSource, connection ->
        {
            try
            {
                final Object model = myModelGenerator.apply( connection );
                final Template template = myHandlebars.compile( myTemplateName );
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
