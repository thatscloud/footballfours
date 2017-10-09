package com.footballfours.model.admin.seasons.builder;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.footballfours.entity.Parameter;
import com.footballfours.entity.Parameter_;
import com.footballfours.entity.Season;
import com.footballfours.entity.Season_;
import com.footballfours.entity.constant.ParameterParameterCode;
import com.footballfours.model.admin.common.SeasonModel;

public class SeasonsModelBuilder
{
    public static List<SeasonModel> getSeasonsFromEntityManager( final EntityManager entityManager )
    {

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        final CriteriaQuery<String> parameterTextCriteriaQuery =
            criteriaBuilder.createQuery( String.class );
        final Root<Parameter> parameterRoot = parameterTextCriteriaQuery.from( Parameter.class );
        parameterTextCriteriaQuery.where(
            criteriaBuilder.equal( parameterRoot.get( Parameter_.parameterCode ),
                                   ParameterParameterCode.CURRENT_SEASON ) );
        parameterTextCriteriaQuery.select( parameterRoot.get( Parameter_.parameterText ) );
        final UUID currentSeasonId =
            UUID.fromString(
                entityManager.createQuery( parameterTextCriteriaQuery ).getSingleResult() );

        final CriteriaQuery<Season> seasonCriteriaQuery =
            criteriaBuilder.createQuery( Season.class );
        final Root<Season> seasonRoot = seasonCriteriaQuery.from( Season.class );
        seasonCriteriaQuery.orderBy(
            criteriaBuilder.asc( criteriaBuilder.upper( seasonRoot.get( Season_.seasonName ) ) ) );
        return entityManager.createQuery( seasonCriteriaQuery ).getResultList().stream()
            .map( seasonItem ->
            {
                final SeasonModel seasonModel = new SeasonModel();
                seasonModel.setSeasonId( seasonItem.getSeasonId() );
                seasonModel.setSeasonName( seasonItem.getSeasonName() );
                seasonModel.setSelected( Objects.equals( seasonItem.getSeasonId(),
                                                         currentSeasonId ) );
                return seasonModel;
            } )
            .collect( toList() );
    }
}
