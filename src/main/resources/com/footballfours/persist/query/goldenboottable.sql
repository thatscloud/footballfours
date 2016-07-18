SELECT 
    p.nm_given AS givenName,
    p.nm_family AS familyName,
    t.nm_team AS teamName,
    COUNT( g.id_goal ) AS goals
FROM
player p
INNER JOIN team t ON
t.id_team = p.id_team
INNER JOIN season s ON
t.id_season = s.id_season
INNER JOIN parameter param ON
param.cd_parameter = 'CURRENT_SEASON' AND
param.tx_parameter = s.id_season 
INNER JOIN match_team_player mtp ON
p.id_player = mtp.id_player
INNER JOIN goal g ON
g.id_match_team_player = mtp.id_match_team_player
INNER JOIN match_team mt ON
mt.id_match_team = mtp.id_match_team
INNER JOIN match m ON
m.id_match = mt.id_match
WHERE
    m.cd_status = 'COMPLETED'
GROUP BY p.id_player, t.id_team
ORDER BY goals DESC, familyName ASC, givenName ASC