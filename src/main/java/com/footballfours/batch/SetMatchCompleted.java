package com.footballfours.batch;

import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class SetMatchCompleted extends BatchTask
{
    public static void main( final String[] args )
    {
        new SetMatchCompleted( args ).run();
    }

    public SetMatchCompleted( final String[] args )
    {
        super( args );
    }

    @Override
    protected void runTask( final Connection connection, final String[] args )
        throws Exception
    {
        if( args.length != 1 )
        {
            throw new Exception( "Must specify a CSV file of match summaries" );
        }

        try( final Reader reader = new FileReader( args[0] );
             final CSVParser csvParser = new CSVParser( reader, CSVFormat.RFC4180 ) )
        {
            try( final PreparedStatement updateStatement =
                     connection.prepareStatement(
                         "UPDATE match SET cd_status = 'COMPLETED' " +
                         "WHERE id_match = ( " +
                         "    SELECT " +
                         "        m.id_match " +
                         "    FROM round r " +
                         "    INNER JOIN match m ON " +
                         "    m.id_round = r.id_round " +
                         "    INNER JOIN match_team hmt ON " +
                         "    hmt.cd_team_type = 'HOME' AND " +
                         "    hmt.id_match = m.id_match " +
                         "    INNER JOIN team ht ON " +
                         "    hmt.id_team = ht.id_team " +
                         "    INNER JOIN match_team amt ON " +
                         "    amt.cd_team_type = 'AWAY' AND " +
                         "    amt.id_match = m.id_match " +
                         "    INNER JOIN team at ON " +
                         "    amt.id_team = at.id_team " +
                         "    INNER JOIN " +
                         "    ( " +
                         "        SELECT " +
                         "            m.id_match AS homeGoalCountMatchId, " +
                         "            COUNT(g_tot_home.id_goal) AS homeGoals " +
                         "        FROM match m " +
                         "        INNER JOIN match_team hmt ON " +
                         "        hmt.cd_team_type = 'HOME' AND " +
                         "        hmt.id_match = m.id_match " +
                         "        LEFT OUTER JOIN goal g_tot_home ON " +
                         "        g_tot_home.id_match_team = hmt.id_match_team " +
                         "        GROUP BY  " +
                         "            m.id_match " +
                         "    ) " +
                         "    ON m.id_match = homeGoalCountMatchId " +
                         "    INNER JOIN " +
                         "    ( " +
                         "        SELECT " +
                         "            m.id_match AS awayGoalCountMatchId, " +
                         "            COUNT(g_tot_away.id_goal) AS awayGoals " +
                         "        FROM match m " +
                         "        INNER JOIN match_team amt ON " +
                         "        amt.cd_team_type = 'AWAY' AND " +
                         "        amt.id_match = m.id_match " +
                         "        LEFT OUTER JOIN goal g_tot_away ON " +
                         "        g_tot_away.id_match_team = amt.id_match_team " +
                         "        GROUP BY  " +
                         "            m.id_match " +
                         "    ) " +
                         "    ON m.id_match = awayGoalCountMatchId " +
                         "    INNER JOIN golden_ball_vote gbv ON " +
                         "    gbv.id_match_team = amt.id_match_team OR " +
                         "    gbv.id_match_team = hmt.id_match_team " +
                         "    WHERE " +
                         "        r.no_round = ? AND " +
                         "        ht.nm_team = ? AND " +
                         "        at.nm_team = ? AND " +
                         "        homeGoals = ? AND " +
                         "        awayGoals = ? " +
                         "    GROUP BY  " +
                         "        m.id_match " +
                         "    HAVING " +
                         "        COUNT( gbv.id_golden_ball_vote ) = 6 " +
                         ")" ) )
            {
                int lineNumber = 0;
                for( final CSVRecord record : csvParser )
                {
                    lineNumber++;
                    final int roundNumber = Integer.parseInt( record.get( 0 ) );
                    final String homeTeamName = record.get( 1 );
                    final String awayTeamName = record.get( 2 );
                    final int homeTeamScore = Integer.parseInt( record.get( 3 ) );
                    final int awayTeamScore = Integer.parseInt( record.get( 4 ) );

                    updateStatement.setInt( 1, roundNumber );
                    updateStatement.setString( 2, homeTeamName );
                    updateStatement.setString( 3, awayTeamName );
                    updateStatement.setInt( 4, homeTeamScore );
                    updateStatement.setInt( 5, awayTeamScore );
                    if( updateStatement.executeUpdate() != 1 )
                    {
                        throw new Exception( "Check line " + lineNumber );
                    }
                    updateStatement.clearParameters();
                }
                System.out.println( lineNumber + " match summaries set to complete." );
            }
        }
    }

}
