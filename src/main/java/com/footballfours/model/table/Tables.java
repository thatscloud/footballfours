package com.footballfours.model.table;

import java.util.List;

public class Tables
{
    private List<LeagueTableRow> myLeagueTable;
    private List<GoldenBallTableRow> myGoldenBallTable;
    private List<GoldenBootTableRow> myGoldenBootTable;

    public List<LeagueTableRow> getLeagueTable()
    {
        return myLeagueTable;
    }

    public void setLeagueTable( final List<LeagueTableRow> leagueTable )
    {
        myLeagueTable = leagueTable;
    }

    public List<GoldenBallTableRow> getGoldenBallTable()
    {
        return myGoldenBallTable;
    }

    public void setGoldenBallTable( final List<GoldenBallTableRow> goldenBallTable )
    {
        myGoldenBallTable = goldenBallTable;
    }

    public List<GoldenBootTableRow> getGoldenBootTable()
    {
        return myGoldenBootTable;
    }

    public void setGoldenBootTable( final List<GoldenBootTableRow> goldenBootTable )
    {
        myGoldenBootTable = goldenBootTable;
    }

}
