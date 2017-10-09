package com.footballfours.model.rounds.builder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.footballfours.model.rounds.SeasonRounds;
import com.footballfours.persist.query.SeasonRoundsQuery;

public class RoundsModelBuilder
{
    public static List<SeasonRounds> getSeasonRoundsFromConnection( final Connection connection )
    {
        try( final Statement statement =
                 SeasonRoundsQuery.retrieveSeasonRoundsQueryStatement( connection );
             final ResultSet resultSet = statement.getResultSet() )
        {
            final List<SeasonRounds> seasonRoundsList = new LinkedList<>();
            while( resultSet.next() )
            {
                final SeasonRounds seasonRounds = new SeasonRounds();
                seasonRounds.setSeasonName( resultSet.getString( "nm_season" ) );
                seasonRounds.setSeasonId( (UUID)resultSet.getObject( "id_season" ) );
                seasonRounds.setRounds( resultSet.getInt( "seasonRounds" ) );
                seasonRoundsList.add( seasonRounds );
            }
            return seasonRoundsList;
        }
        catch( final SQLException e )
        {
            throw new RuntimeException( e );
        }
    }
}
