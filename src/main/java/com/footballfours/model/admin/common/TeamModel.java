package com.footballfours.model.admin.common;

import java.util.UUID;

public class TeamModel
{
    private UUID myTeamId;
    private String myTeamName;
    private boolean mySelected;

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

    public boolean isSelected()
    {
        return mySelected;
    }

    public void setSelected( final boolean selected )
    {
        mySelected = selected;
    }
}
