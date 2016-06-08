package com.footballfours.route.rest;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.footballfours.core.route.RegistrableRoute;
import com.footballfours.model.fixture.Round;
import com.footballfours.model.fixture.builder.FixturesModelBuilder;
import com.j256.ormlite.support.ConnectionSource;

public class RoundsRestRoute extends RegistrableRoute
{
    public RoundsRestRoute( final ConnectionSource connectionSource,
                            final Connection connection )
    {
        super( connectionSource, connection );
    }

    @Override
    public void register()
    {
        get( "/rounds", "application/json", ( request, response ) -> {
            Map<String, Object> page = getNewPageModel( request );

            final List<Round> rounds = FixturesModelBuilder
                .getRoundsFromConnection( getConnection() );

            page.put( "rounds", rounds );

            return page;
        }, getJsonTransformer() );

        get( "/rounds/:roundId", "application/json", ( request, response ) -> {
            Map<String, Object> page = getNewPageModel( request );
            return page;
        }, getJsonTransformer() );

        delete( "/rounds/:roundId", "application/json", ( request, response ) -> {
            Map<String, Object> page = getNewPageModel( request );
            return page;
        }, getJsonTransformer() );

        post( "/rounds", "application/json", ( request, response ) -> {
            Map<String, Object> page = getNewPageModel( request );
            return page;
        }, getJsonTransformer() );

    }
}
