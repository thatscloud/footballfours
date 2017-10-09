package com.footballfours.model.admin.matches;

import java.util.UUID;

public class TeamModel
{
    private UUID myTeamId;
    private String myTeamName;

    public UUID getTeamId()
    {
        return myTeamId;
    }

    public void setTeamId( final UUID teamId )
    {
        myTeamId = teamId;
    }

    public String getTeamName()
    {
        return myTeamName;
    }

    public void setTeamName( final String teamName )
    {
        myTeamName = teamName;
    }
}
