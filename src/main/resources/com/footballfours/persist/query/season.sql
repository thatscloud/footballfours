SELECT
    id_season, nm_season
FROM season
WHERE
    id_season = ?
ORDER BY UPPER(nm_season) ASC;