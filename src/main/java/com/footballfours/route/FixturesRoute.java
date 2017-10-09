package com.footballfours.route;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.List;

import javax.sql.DataSource;

import com.footballfours.model.fixture.Round;
import com.footballfours.model.fixture.builder.FixturesModelBuilder;
import com.footballfours.persist.RunAgainstDatabase;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;

import spark.Request;
import spark.Response;
import spark.Route;

public class FixturesRoute implements Route
{
    private final DataSource myDataSource;

    public FixturesRoute( final DataSource dataSource )
    {
        myDataSource = dataSource;
    }

    @Override
    public Object handle( final Request request, final Response response )
        throws Exception
    {
        RunAgainstDatabase.run( myDataSource, connection ->
        {
            try
            {
                final List<Round> rounds =
                    FixturesModelBuilder.getRoundsFromConnection( connection );
                final TemplateLoader templateLoader =
                    new ClassPathTemplateLoader( "/com/footballfours/template" );
                final Handlebars handlebars = new Handlebars( templateLoader );
                final Template template = handlebars.compile( "fixtures" );
                try( final Writer writer = response.raw().getWriter() )
                {
                    template.apply( rounds, writer );
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
