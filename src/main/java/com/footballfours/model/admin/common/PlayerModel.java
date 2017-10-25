package com.footballfours.model.admin.common;

import java.util.UUID;

public class PlayerModel
{
    private UUID myPlayerId;
    private String myPlayerGivenName;
    private String myPlayerFamilyName;

    public UUID getPlayerId()
    {
        return myPlayerId;
    }

    public void setPlayerId( final UUID playerId )
    {
        myPlayerId = playerId;
    }

    public String getPlayerGivenName()
    {
        return myPlayerGivenName;
    }

    public void setPlayerGivenName( final String playerGivenName )
    {
        myPlayerGivenName = playerGivenName;
    }

    public String getPlayerFamilyName()
    {
        return myPlayerFamilyName;
    }

    public void setPlayerFamilyName( final String playerFamilyName )
    {
        myPlayerFamilyName = playerFamilyName;
    }
}
