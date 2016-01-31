package com.footballfours.route;

import java.sql.Connection;
import java.util.function.Function;

import javax.sql.DataSource;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.cache.HighConcurrencyTemplateCache;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;

import spark.Route;

public class HandlebarsRouteFactory
{
    private final Handlebars myHandlebars;
    private final DataSource myDataSource;

    public HandlebarsRouteFactory( final String classpathTemplateLoaderPrefix,
                                   final DataSource dataSource )
    {
        myDataSource = dataSource;
        myHandlebars =
            new Handlebars( new ClassPathTemplateLoader(
                                classpathTemplateLoaderPrefix ) )
                .with( new HighConcurrencyTemplateCache() );
    }

    public Route from( final String templateName,
                       final Function<Connection, Object> modelGenerator )
    {
        return new HandlebarsRoute(
            myHandlebars, myDataSource, templateName, modelGenerator );
    }
}
