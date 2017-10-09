package com.footballfours.model.admin.common;

import java.util.UUID;

public class SeasonModel
{
    private UUID mySeasonId;
    private String mySeasonName;
    private boolean mySelected;

    public UUID getSeasonId()
    {
        return mySeasonId;
    }

    public void setSeasonId( final UUID seasonId )
    {
        mySeasonId = seasonId;
    }

    public String getSeasonName()
    {
        return mySeasonName;
    }


    public void setSeasonName( final String seasonName )
    {
        mySeasonName = seasonName;
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
