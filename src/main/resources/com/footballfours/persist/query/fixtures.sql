SELECT
    r.no_round AS roundNumber,
    ht.nm_team AS homeTeamName,
    at.nm_team AS awayTeamName,
    m.dt_scheduled AS scheduledDateTime,
    m.dt_played AS playedDateTime,
    m.cd_status AS matchStatus,
    homeGoals,
    awayGoals,
    t.nm_team AS scoringTeamName,
    g.fl_opponent_own_goal AS ownGoal,
    p.nm_given AS playerGivenNames,
    p.nm_family AS playerFamilyNames,
    COUNT(g.id_goal) AS playerGoals
FROM round r
INNER JOIN match m ON
m.id_round = r.id_round
INNER JOIN season s ON
r.id_season = s.id_season
INNER JOIN parameter param ON
param.cd_parameter = 'CURRENT_SEASON' AND
param.tx_parameter = s.id_season
INNER JOIN match_team hmt ON
hmt.cd_team_type = 'HOME' AND
hmt.id_match = m.id_match
INNER JOIN team ht ON
hmt.id_team = ht.id_team
INNER JOIN match_team amt ON
amt.cd_team_type = 'AWAY' AND
amt.id_match = m.id_match
INNER JOIN team at ON
amt.id_team = at.id_team
INNER JOIN
(
    SELECT
        m.id_match AS homeGoalCountMatchId,
        COUNT(g_tot_home.id_goal) AS homeGoals
    FROM match m
    INNER JOIN match_team hmt ON
    hmt.cd_team_type = 'HOME' AND
    hmt.id_match = m.id_match
    LEFT OUTER JOIN goal g_tot_home ON
    g_tot_home.id_match_team = hmt.id_match_team
    GROUP BY 
        m.id_match
)
ON m.id_match = homeGoalCountMatchId
INNER JOIN
(
    SELECT
        m.id_match AS awayGoalCountMatchId,
        COUNT(g_tot_away.id_goal) AS awayGoals
    FROM match m
    INNER JOIN match_team amt ON
    amt.cd_team_type = 'AWAY' AND
    amt.id_match = m.id_match
    LEFT OUTER JOIN goal g_tot_away ON
    g_tot_away.id_match_team = amt.id_match_team
    GROUP BY 
        m.id_match
)
ON m.id_match = awayGoalCountMatchId
LEFT OUTER JOIN
(
    goal g
    INNER JOIN match_team mt ON
    mt.id_match_team = g.id_match_team
    INNER JOIN team t ON
    t.id_team = mt.id_team
    LEFT OUTER JOIN 
    (
        match_team_player mtp
        INNER JOIN player p ON
        p.id_player = mtp.id_player
    )
    ON mtp.id_match_team_player = g.id_match_team_player
)
ON 
g.id_match_team = amt.id_match_team OR
g.id_match_team = hmt.id_match_team
GROUP BY 
    r.no_round,
    ht.nm_team,
    at.nm_team,
    m.dt_scheduled,
    m.dt_played,
    m.cd_status,
    homeGoals,
    awayGoals,
    t.nm_team,
    g.fl_opponent_own_goal,
    p.nm_given,
    p.nm_family,
    mtp.id_match_team_player
ORDER BY r.no_round ASC, m.dt_played ASC, g.fl_opponent_own_goal ASC, p.nm_family ASC