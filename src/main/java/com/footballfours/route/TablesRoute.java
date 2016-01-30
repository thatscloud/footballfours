package com.footballfours.route;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;

import javax.sql.DataSource;

import com.footballfours.model.table.Tables;
import com.footballfours.model.table.builder.TablesModelBuilder;
import com.footballfours.persist.RunAgainstDataSource;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;

import spark.Request;
import spark.Response;
import spark.Route;

public class TablesRoute implements Route
{
    private final DataSource myDataSource;

    public TablesRoute( final DataSource dataSource )
    {
        myDataSource = dataSource;
    }

    @Override
    public Object handle( final Request request, final Response response )
        throws Exception
    {
        RunAgainstDataSource.run( myDataSource, connection ->
        {
            try
            {
                final Tables tables  =
                    TablesModelBuilder.getTablesFromConnection( connection );
                final TemplateLoader templateLoader =
                    new ClassPathTemplateLoader( "/com/footballfours/template" );
                final Handlebars handlebars = new Handlebars( templateLoader );
                final Template template = handlebars.compile( "tables" );
                try( final Writer writer = response.raw().getWriter() )
                {
                    template.apply( tables, writer );
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
