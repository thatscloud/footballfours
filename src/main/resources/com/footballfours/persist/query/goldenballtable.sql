SELECT 
    p.nm_given AS givenName,
    p.nm_family AS familyName,
    t.nm_team AS teamName,
    SUM( gbv.no_votes ) AS votes
FROM
player p
INNER JOIN team t ON
t.id_team = p.id_team 
INNER JOIN match_team_player mtp ON
p.id_player = mtp.id_player
INNER JOIN golden_ball_vote gbv ON
gbv.id_match_team_player = mtp.id_match_team_player
INNER JOIN match_team mt ON
mt.id_match_team = mtp.id_match_team
INNER JOIN match m ON
m.id_match = mt.id_match
WHERE
    m.cd_status = 'COMPLETED'
GROUP BY p.id_player, t.id_team
ORDER BY votes DESC, familyName ASC, givenName ASC