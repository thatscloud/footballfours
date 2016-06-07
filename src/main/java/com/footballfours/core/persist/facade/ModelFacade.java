package com.footballfours.core.persist.facade;

import com.j256.ormlite.support.ConnectionSource;

public class ModelFacade
{
    protected ConnectionSource connectionSource;

    public ModelFacade( ConnectionSource connectionSource )
    {
        this.connectionSource = connectionSource;
    }

    protected ConnectionSource getConnectionSource()
    {
        return connectionSource;
    }
}
