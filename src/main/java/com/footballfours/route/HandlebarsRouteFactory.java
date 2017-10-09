package com.footballfours.route;

import java.sql.Connection;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.cache.HighConcurrencyTemplateCache;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;

import spark.Route;

public class HandlebarsRouteFactory
{
    private final Handlebars myHandlebars;
    private final DataSource myDataSource;
    private final EntityManagerFactory myEntityManagerFactory;

    public HandlebarsRouteFactory( final String classpathTemplateLoaderPrefix,
                                   final DataSource dataSource,
                                   final EntityManagerFactory entityManagerFactory )
    {
        myDataSource = dataSource;
        myEntityManagerFactory = entityManagerFactory;
        myHandlebars =
            new Handlebars( new ClassPathTemplateLoader(
                                classpathTemplateLoaderPrefix ) )
                .with( new HighConcurrencyTemplateCache() );
    }

    public Route from( final String templateName,
                       final Function<Connection, Object> modelGenerator )
    {
        return new HandlebarsDataSourceRoute(
            myHandlebars, myDataSource, templateName, modelGenerator );
    }

    public Route fromJpa( final String templateName,
                          final Function<EntityManager, Object> modelGenerator )
    {
        return new HandlebarsEntityManagerFactoryRoute(
            myHandlebars, myEntityManagerFactory, templateName, modelGenerator );
    }
}
