package com.footballfours.route.rest;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import spark.Request;
import spark.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.footballfours.core.route.RegistrableRoute;
import com.footballfours.model.fixture.Round;
import com.footballfours.model.fixture.builder.FixturesModelBuilder;
import com.footballfours.persist.RunAgainstDataSource;
import com.footballfours.util.SessionUtils;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.j256.ormlite.support.ConnectionSource;

public class TablesRestRoute extends RegistrableRoute
{
    public TablesRestRoute( final Connection connection )
    {
        super( connection );
    }

    @Override
    public void register()
    {
        get( "/tables", "application/json", ( request, response ) -> {
            Map<String, Object> page = getNewPageModel( request );
            return page;
        }, getJsonTransformer() );

        get( "/tables/:tableId", "application/json", ( request, response ) -> {
            Map<String, Object> page = getNewPageModel( request );
            return page;
        }, getJsonTransformer() );

        delete( "/tables/:tableId", "application/json", ( request, response ) -> {
            Map<String, Object> page = getNewPageModel( request );
            return page;
        }, getJsonTransformer() );

        post( "/tables", "application/json", ( request, response ) -> {
            Map<String, Object> page = getNewPageModel( request );
            return page;
        }, getJsonTransformer() );

    }
}
