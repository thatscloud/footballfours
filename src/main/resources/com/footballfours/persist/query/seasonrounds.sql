SELECT
    s.id_season, s.nm_season, NVL( MAX( r.no_round ), 0 ) AS "seasonRounds"
FROM season s
LEFT OUTER JOIN round r ON
r.id_season = s.id_season
GROUP BY r.id_season
ORDER BY UPPER(nm_season) ASC;