SELECT
    nm_team
FROM team
WHERE
    id_season = ?
ORDER BY UPPER(nm_team) ASC;