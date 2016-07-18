SELECT
    teamName,
    matchesPlayed,
    win,
    draw,
    loss,
    goalsFor,
    goalsAgainst,
    goalsFor - goalsAgainst AS goalDifference,
    win * 3 + draw AS points 
FROM
(
    SELECT
    t.nm_team AS teamName,
    matchesPlayed,
    SUM( CASE WHEN homeTeamWin = 1 AND t.id_team = homeTeamId OR
                   homeTeamLoss = 1 AND t.id_team <> homeTeamId
              THEN 1
              ELSE 0 END ) AS win,
    SUM( homeTeamDraw ) AS draw,
    SUM( CASE WHEN homeTeamWin = 1 AND t.id_team <> homeTeamId OR
                   homeTeamLoss = 1 AND t.id_team = homeTeamId
              THEN 1
              ELSE 0 END ) AS loss,
    goalsFor,
    goalsAgainst
    FROM
    team t
    INNER JOIN season s ON
    t.id_season = s.id_season
    INNER JOIN parameter param ON
    param.cd_parameter = 'CURRENT_SEASON' AND
    param.tx_parameter = s.id_season
    LEFT OUTER JOIN match_team mt ON
    mt.id_team = t.id_team
    LEFT OUTER JOIN match m ON
    mt.id_match = m.id_match
    LEFT OUTER JOIN
    (
        SELECT 
            CASE WHEN homeGoals > awayGoals THEN 1 ELSE 0 END AS homeTeamWin,
            CASE WHEN homeGoals = awayGoals THEN 1 ELSE 0 END AS homeTeamDraw,
            CASE WHEN awayGoals > homeGoals THEN 1 ELSE 0 END AS homeTeamLoss,
            homeTeamId,
            m.id_match AS matchId
        FROM
        match m
        INNER JOIN
        (
            SELECT
                COUNT(g_tot_home.id_goal) AS homeGoals,
                hmt.id_team AS homeTeamId,
                hmt.id_match AS homeMatchId
            FROM
            match_team hmt
            LEFT OUTER JOIN goal g_tot_home ON
            g_tot_home.id_match_team = hmt.id_match_team
            WHERE hmt.cd_team_type = 'HOME'
            GROUP BY 
                hmt.id_match
        )
        ON m.id_match = homeMatchId
        INNER JOIN
        (
            SELECT
                COUNT(g_tot_away.id_goal) AS awayGoals,
                amt.id_team AS awayTeamId,
                amt.id_match AS awayMatchId
            FROM
            match_team amt
            LEFT OUTER JOIN goal g_tot_away ON
            g_tot_away.id_match_team = amt.id_match_team
            WHERE amt.cd_team_type = 'AWAY' 
            GROUP BY 
                amt.id_match
        )
        ON m.id_match = awayMatchId
    )
    ON m.id_match = matchId
    LEFT OUTER JOIN
    (
        SELECT
            t.id_team AS goalsForTeamId,
            COUNT( g.id_goal ) AS goalsFor
        FROM
        team t   
        LEFT OUTER JOIN
        (    
            match_team mt
            INNER JOIN match m ON
            m.id_match = mt.id_match AND
            m.cd_status = 'COMPLETED'
            INNER JOIN goal g ON
            g.id_match_team = mt.id_match_team
        )
        ON mt.id_team = t.id_team
        GROUP BY t.id_team
    )
    ON t.id_team = goalsForTeamId
    LEFT OUTER JOIN
    (
        SELECT
            t.id_team AS goalsAgainstTeamId,
            COUNT( g.id_goal ) AS goalsAgainst
        FROM
        team t   
        LEFT OUTER JOIN
        (    
            match_team mt
            INNER JOIN match m ON
            m.id_match = mt.id_match AND
            m.cd_status = 'COMPLETED'
            INNER JOIN match_team other_mt ON
            other_mt.id_match = m.id_match AND
            other_mt.id_team <> mt.id_team
            INNER JOIN goal g ON
            g.id_match_team = other_mt.id_match_team
        )
        ON mt.id_team = t.id_team
        GROUP BY t.id_team
    )
    ON t.id_team = goalsAgainstTeamId
    LEFT OUTER JOIN
    (
        SELECT
            t.id_team AS matchesPlayedTeamId,
            COUNT( m.id_match ) AS matchesPlayed
        FROM
        team t
        LEFT OUTER JOIN
        (
            match_team mt
            INNER JOIN match m ON
            m.id_match = mt.id_match AND
            m.cd_status = 'COMPLETED'
        )
        ON mt.id_team = t.id_team
        GROUP BY t.id_team
    )
    ON t.id_team = matchesPlayedTeamId
    WHERE m.cd_status = 'COMPLETED' OR m.cd_status IS NULL
    GROUP BY t.nm_team, goalsFor, goalsAgainst
)
ORDER BY points DESC, goalDifference DESC, goalsFor DESC, teamName ASC