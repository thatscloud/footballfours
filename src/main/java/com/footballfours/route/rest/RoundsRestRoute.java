package com.footballfours.route.rest;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
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

public class RoundsRestRoute extends RegistrableRoute
{
    public RoundsRestRoute( final ConnectionSource connectionSource )
    {
        super( connectionSource );
    }

    @Override
    public void register()
    {
        get( "/rounds", "application/json", ( request, response ) -> {
            Map<String, Object> page = getNewPageModel( request );
            return page;
        }, getJsonTransformer() );

        get( "/rounds/:roundId", "application/json",
            ( request, response ) -> {
                Map<String, Object> page = getNewPageModel( request );
                return page;
            }, getJsonTransformer() );

        delete( "/rounds/:roundId", "application/json",
            ( request, response ) -> {
                Map<String, Object> page = getNewPageModel( request );
                return page;
            }, getJsonTransformer() );

        post( "/rounds", "application/json", ( request, response ) -> {
            Map<String, Object> page = getNewPageModel( request );
            return page;
        }, getJsonTransformer() );

    }
}
