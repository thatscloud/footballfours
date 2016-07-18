INSERT INTO season ( id_season, nm_season ) VALUES ( RANDOM_UUID(), 'Summer 2015/2016' );

INSERT INTO team ( id_team, nm_team, id_season ) VALUES ( RANDOM_UUID(), 'The Motherf***ing Meme Team', SELECT id_season FROM season WHERE nm_season = 'Summer 2015/2016' );
INSERT INTO team ( id_team, nm_team, id_season ) VALUES ( RANDOM_UUID(), 'Testers FC', SELECT id_season FROM season WHERE nm_season = 'Summer 2015/2016' );
INSERT INTO team ( id_team, nm_team, id_season ) VALUES ( RANDOM_UUID(), 'X', SELECT id_season FROM season WHERE nm_season = 'Summer 2015/2016' );
INSERT INTO team ( id_team, nm_team, id_season ) VALUES ( RANDOM_UUID(), '2KOOL4SKOOL', SELECT id_season FROM season WHERE nm_season = 'Summer 2015/2016' );

INSERT INTO round ( id_round, no_round, id_season ) VALUES ( RANDOM_UUID(), 1, SELECT id_season FROM season WHERE nm_season = 'Summer 2015/2016' );
INSERT INTO round ( id_round, no_round, id_season ) VALUES ( RANDOM_UUID(), 2, SELECT id_season FROM season WHERE nm_season = 'Summer 2015/2016' );
INSERT INTO round ( id_round, no_round, id_season ) VALUES ( RANDOM_UUID(), 3, SELECT id_season FROM season WHERE nm_season = 'Summer 2015/2016' );

INSERT INTO match ( id_match, id_round, id_season, dt_scheduled, dt_played, cd_status ) 
SELECT
    RANDOM_UUID(),
    id_round,
    id_season,
    TIMESTAMP '2012-01-01 12:00:00',
    TIMESTAMP '2012-01-01 12:00:00',
    'COMPLETED'
FROM round 
WHERE no_round = 1;
INSERT INTO match ( id_match, id_round, id_season, dt_scheduled, dt_played, cd_status )
SELECT
    RANDOM_UUID(),
    id_round,
    id_season,
    TIMESTAMP '2012-01-01 12:00:00',
    TIMESTAMP '2012-01-02 12:00:00',
    'COMPLETED'
FROM round 
WHERE no_round = 1;
INSERT INTO match ( id_match, id_round, id_season, dt_scheduled, dt_played, cd_status )
SELECT
    RANDOM_UUID(),
    id_round,
    id_season,
    TIMESTAMP '2012-01-03 12:00:00',
    TIMESTAMP '2012-01-03 12:00:00',
    'COMPLETED'
FROM round 
WHERE no_round = 2;
INSERT INTO match ( id_match, id_round, id_season, dt_scheduled, dt_played, cd_status )
SELECT
    RANDOM_UUID(),
    id_round,
    id_season,
    TIMESTAMP '2012-01-03 13:00:00',
    TIMESTAMP '2012-01-03 13:00:00',
    'COMPLETED'
FROM round 
WHERE no_round = 2;
INSERT INTO match ( id_match, id_round, id_season, dt_scheduled, dt_played, cd_status )
SELECT
    RANDOM_UUID(),
    id_round,
    id_season,
    TIMESTAMP '2012-01-05 11:30:00',
    TIMESTAMP '2012-01-05 11:00:00',
    NULL
FROM round 
WHERE no_round = 3;
INSERT INTO match ( id_match, id_round, id_season, dt_scheduled, dt_played, cd_status ) 
SELECT
    RANDOM_UUID(),
    id_round,
    id_season,
    TIMESTAMP '2012-01-05 12:30:00',
    TIMESTAMP '2012-01-05 12:00:00',
    NULL
FROM round 
WHERE no_round = 3;

INSERT INTO match_team ( id_match_team, id_team, id_match, id_round, id_season, cd_team_type )
SELECT
    RANDOM_UUID(),
    t.id_team,
    m.id_match,
    r.id_round,
    r.id_season,
    'HOME'
FROM team t, match m
INNER JOIN round r ON
r.id_round = m.id_round
WHERE
    t.nm_team = 'The Motherf***ing Meme Team' AND
    r.no_round = 1 AND
    m.dt_played = TIMESTAMP '2012-01-01 12:00:00';
INSERT INTO match_team ( id_match_team, id_team, id_match, id_round, id_season, cd_team_type )
SELECT
    RANDOM_UUID(),
    t.id_team,
    m.id_match,
    r.id_round,
    r.id_season,
    'AWAY'
FROM team t, match m
INNER JOIN round r ON
r.id_round = m.id_round
WHERE
    t.nm_team = 'Testers FC' AND
    r.no_round = 1 AND
    m.dt_played = TIMESTAMP '2012-01-01 12:00:00';
INSERT INTO match_team ( id_match_team, id_team, id_match, id_round, id_season, cd_team_type )
SELECT
    RANDOM_UUID(),
    t.id_team,
    m.id_match,
    r.id_round,
    r.id_season,
    'HOME'
FROM team t, match m
INNER JOIN round r ON
r.id_round = m.id_round
WHERE
    t.nm_team = 'X' AND
    r.no_round = 1 AND
    m.dt_played = TIMESTAMP '2012-01-02 12:00:00';
INSERT INTO match_team ( id_match_team, id_team, id_match, id_round, id_season, cd_team_type )
SELECT
    RANDOM_UUID(),
    t.id_team,
    m.id_match,
    r.id_round,
    r.id_season,
    'AWAY'
FROM team t, match m
INNER JOIN round r ON
r.id_round = m.id_round
WHERE
    t.nm_team = '2KOOL4SKOOL' AND
    r.no_round = 1 AND
    m.dt_played = TIMESTAMP '2012-01-02 12:00:00';
INSERT INTO match_team ( id_match_team, id_team, id_match, id_round, id_season, cd_team_type )
SELECT
    RANDOM_UUID(),
    t.id_team,
    m.id_match,
    r.id_round,
    r.id_season,
    'HOME'
FROM team t, match m
INNER JOIN round r ON
r.id_round = m.id_round
WHERE
    t.nm_team = 'The Motherf***ing Meme Team' AND
    r.no_round = 2 AND
    m.dt_played = TIMESTAMP '2012-01-03 12:00:00';
INSERT INTO match_team ( id_match_team, id_team, id_match, id_round, id_season, cd_team_type )
SELECT
    RANDOM_UUID(),
    t.id_team,
    m.id_match,
    r.id_round,
    r.id_season,
    'AWAY'
FROM team t, match m
INNER JOIN round r ON
r.id_round = m.id_round
WHERE
    t.nm_team = 'X' AND
    r.no_round = 2 AND
    m.dt_played = TIMESTAMP '2012-01-03 12:00:00';
INSERT INTO match_team ( id_match_team, id_team, id_match, id_round, id_season, cd_team_type )
SELECT
    RANDOM_UUID(),
    t.id_team,
    m.id_match,
    r.id_round,
    r.id_season,
    'HOME'
FROM team t, match m
INNER JOIN round r ON
r.id_round = m.id_round
WHERE
    t.nm_team = 'Testers FC' AND
    r.no_round = 2 AND
    m.dt_played = TIMESTAMP '2012-01-03 13:00:00';
INSERT INTO match_team ( id_match_team, id_team, id_match, id_round, id_season, cd_team_type )
SELECT
    RANDOM_UUID(),
    t.id_team,
    m.id_match,
    r.id_round,
    r.id_season,
    'AWAY'
FROM team t, match m
INNER JOIN round r ON
r.id_round = m.id_round
WHERE
    t.nm_team = '2KOOL4SKOOL' AND
    r.no_round = 2 AND
    m.dt_played = TIMESTAMP '2012-01-03 13:00:00';
INSERT INTO match_team ( id_match_team, id_team, id_match, id_round, id_season, cd_team_type )
SELECT
    RANDOM_UUID(),
    t.id_team,
    m.id_match,
    r.id_round,
    r.id_season,
    'HOME'
FROM team t, match m
INNER JOIN round r ON
r.id_round = m.id_round
WHERE
    t.nm_team = 'The Motherf***ing Meme Team' AND
    r.no_round = 3 AND
    m.dt_played = TIMESTAMP '2012-01-05 11:00:00';
INSERT INTO match_team ( id_match_team, id_team, id_match, id_round, id_season, cd_team_type )
SELECT
    RANDOM_UUID(),
    t.id_team,
    m.id_match,
    r.id_round,
    r.id_season,
    'AWAY'
FROM team t, match m
INNER JOIN round r ON
r.id_round = m.id_round
WHERE
    t.nm_team = '2KOOL4SKOOL' AND
    r.no_round = 3 AND
    m.dt_played = TIMESTAMP '2012-01-05 11:00:00';
INSERT INTO match_team ( id_match_team, id_team, id_match, id_round, id_season, cd_team_type )
SELECT
    RANDOM_UUID(),
    t.id_team,
    m.id_match,
    r.id_round,
    r.id_season,
    'HOME'
FROM team t, match m
INNER JOIN round r ON
r.id_round = m.id_round
WHERE
    t.nm_team = 'X' AND
    r.no_round = 3 AND
    m.dt_played = TIMESTAMP '2012-01-05 12:00:00';
INSERT INTO match_team ( id_match_team, id_team, id_match, id_round, id_season, cd_team_type )
SELECT
    RANDOM_UUID(),
    t.id_team,
    m.id_match,
    r.id_round,
    r.id_season,
    'AWAY'
FROM team t, match m
INNER JOIN round r ON
r.id_round = m.id_round
WHERE
    t.nm_team = 'Testers FC' AND
    r.no_round = 3 AND
    m.dt_played = TIMESTAMP '2012-01-05 12:00:00';
    
INSERT INTO player ( id_player, id_team, no_team_player, nm_given, nm_family )
SELECT
    RANDOM_UUID(),
    id_team,
    1,
    'Joshua',
    'Smith'
FROM team WHERE nm_team = 'The Motherf***ing Meme Team';
INSERT INTO player ( id_player, id_team, no_team_player, nm_given, nm_family )
SELECT
    RANDOM_UUID(),
    id_team,
    2,
    'Jessica',
    'Jones'
FROM team WHERE nm_team = 'The Motherf***ing Meme Team';
INSERT INTO player ( id_player, id_team, no_team_player, nm_given, nm_family )
SELECT
    RANDOM_UUID(),
    id_team,
    3,
    'Jack',
    'Williams'
FROM team WHERE nm_team = 'The Motherf***ing Meme Team';
INSERT INTO player ( id_player, id_team, no_team_player, nm_given, nm_family )
SELECT
    RANDOM_UUID(),
    id_team,
    4,
    'Emily',
    'Brown'
FROM team WHERE nm_team = 'The Motherf***ing Meme Team';
INSERT INTO player ( id_player, id_team, no_team_player, nm_given, nm_family )
SELECT
    RANDOM_UUID(),
    id_team,
    5,
    'Thomas',
    'Wilson'
FROM team WHERE nm_team = 'The Motherf***ing Meme Team';
INSERT INTO player ( id_player, id_team, no_team_player, nm_given, nm_family )
SELECT
    RANDOM_UUID(),
    id_team,
    6,
    'Sarah',
    'Taylor'
FROM team WHERE nm_team = 'The Motherf***ing Meme Team';
INSERT INTO player ( id_player, id_team, no_team_player, nm_given, nm_family )
SELECT
    RANDOM_UUID(),
    id_team,
    1,
    'Lachlan',
    'Johnson'
FROM team WHERE nm_team = 'Testers FC';
INSERT INTO player ( id_player, id_team, no_team_player, nm_given, nm_family )
SELECT
    RANDOM_UUID(),
    id_team,
    2,
    'Georgia',
    'White'
FROM team WHERE nm_team = 'Testers FC';
INSERT INTO player ( id_player, id_team, no_team_player, nm_given, nm_family )
SELECT
    RANDOM_UUID(),
    id_team,
    3,
    'Matthew',
    'Martin'
FROM team WHERE nm_team = 'Testers FC';
INSERT INTO player ( id_player, id_team, no_team_player, nm_given, nm_family )
SELECT
    RANDOM_UUID(),
    id_team,
    4,
    'Olivia',
    'Anderson'
FROM team WHERE nm_team = 'Testers FC';
INSERT INTO player ( id_player, id_team, no_team_player, nm_given, nm_family )
SELECT
    RANDOM_UUID(),
    id_team,
    5,
    'James',
    'Thompson'
FROM team WHERE nm_team = 'Testers FC';
INSERT INTO player ( id_player, id_team, no_team_player, nm_given, nm_family )
SELECT
    RANDOM_UUID(),
    id_team,
    6,
    'Emma',
    'Nguyen'
FROM team WHERE nm_team = 'Testers FC';
INSERT INTO player ( id_player, id_team, no_team_player, nm_given, nm_family )
SELECT
    RANDOM_UUID(),
    id_team,
    1,
    'Daniel',
    'Thomas'
FROM team WHERE nm_team = 'X';
INSERT INTO player ( id_player, id_team, no_team_player, nm_given, nm_family )
SELECT
    RANDOM_UUID(),
    id_team,
    2,
    'Chloe',
    'Walker'
FROM team WHERE nm_team = 'X';
INSERT INTO player ( id_player, id_team, no_team_player, nm_given, nm_family )
SELECT
    RANDOM_UUID(),
    id_team,
    3,
    'Nicholas',
    'Harris'
FROM team WHERE nm_team = 'X';
INSERT INTO player ( id_player, id_team, no_team_player, nm_given, nm_family )
SELECT
    RANDOM_UUID(),
    id_team,
    4,
    'Sophie',
    'Lee'
FROM team WHERE nm_team = 'X';
INSERT INTO player ( id_player, id_team, no_team_player, nm_given, nm_family )
SELECT
    RANDOM_UUID(),
    id_team,
    5,
    'Benjamin',
    'Ryan'
FROM team WHERE nm_team = 'X';
INSERT INTO player ( id_player, id_team, no_team_player, nm_given, nm_family )
SELECT
    RANDOM_UUID(),
    id_team,
    6,
    'Hannah',
    'Robinson'
FROM team WHERE nm_team = 'X';
INSERT INTO player ( id_player, id_team, no_team_player, nm_given, nm_family )
SELECT
    RANDOM_UUID(),
    id_team,
    1,
    'William',
    'Kelly'
FROM team WHERE nm_team = '2KOOL4SKOOL';
INSERT INTO player ( id_player, id_team, no_team_player, nm_given, nm_family )
SELECT
    RANDOM_UUID(),
    id_team,
    2,
    'Isabella',
    'King'
FROM team WHERE nm_team = '2KOOL4SKOOL';
INSERT INTO player ( id_player, id_team, no_team_player, nm_given, nm_family )
SELECT
    RANDOM_UUID(),
    id_team,
    3,
    'Ryan',
    'Campbell'
FROM team WHERE nm_team = '2KOOL4SKOOL';
INSERT INTO player ( id_player, id_team, no_team_player, nm_given, nm_family )
SELECT
    RANDOM_UUID(),
    id_team,
    4,
    'Caitlin',
    'Davis'
FROM team WHERE nm_team = '2KOOL4SKOOL';
INSERT INTO player ( id_player, id_team, no_team_player, nm_given, nm_family )
SELECT
    RANDOM_UUID(),
    id_team,
    5,
    'Jacob',
    'Roberts'
FROM team WHERE nm_team = '2KOOL4SKOOL';
INSERT INTO player ( id_player, id_team, no_team_player, nm_given, nm_family )
SELECT
    RANDOM_UUID(),
    id_team,
    6,
    'Grace',
    'Hall'
FROM team WHERE nm_team = '2KOOL4SKOOL';

INSERT INTO match_team_player 
( 
    id_match_team_player, 
    id_player, 
    id_team, 
    id_match_team, 
    no_match_team_player 
)
SELECT
    RANDOM_UUID(),
    p.id_player,
    t.id_team,
    mt.id_match_team,
    p.no_team_player
FROM
    player p 
    INNER JOIN team t ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team;
    
INSERT INTO goal ( id_goal, id_match_team, fl_opponent_own_goal, id_match_team_player )
SELECT
    RANDOM_UUID(),
    mt.id_match_team,
    FALSE,
    mtp.id_match_team_player
FROM
    player p 
    INNER JOIN team t ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
    INNER JOIN match m ON
    mt.id_match = m.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
WHERE
    p.nm_given = 'Joshua' AND
    p.nm_family = 'Smith' AND
    r.no_round = 1 AND
    t.nm_team = 'The Motherf***ing Meme Team';
INSERT INTO goal ( id_goal, id_match_team, fl_opponent_own_goal, id_match_team_player )
SELECT
    RANDOM_UUID(),
    mt.id_match_team,
    FALSE,
    mtp.id_match_team_player
FROM
    player p 
    INNER JOIN team t ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
    INNER JOIN match m ON
    mt.id_match = m.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
WHERE
    p.nm_given = 'Joshua' AND
    p.nm_family = 'Smith' AND
    r.no_round = 1 AND
    t.nm_team = 'The Motherf***ing Meme Team';
INSERT INTO goal ( id_goal, id_match_team, fl_opponent_own_goal, id_match_team_player )
SELECT
    RANDOM_UUID(),
    mt.id_match_team,
    FALSE,
    mtp.id_match_team_player
FROM
    player p 
    INNER JOIN team t ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
    INNER JOIN match m ON
    mt.id_match = m.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
WHERE
    p.nm_given = 'Joshua' AND
    p.nm_family = 'Smith' AND
    r.no_round = 1 AND
    t.nm_team = 'The Motherf***ing Meme Team';
INSERT INTO goal ( id_goal, id_match_team, fl_opponent_own_goal, id_match_team_player )
SELECT
    RANDOM_UUID(),
    mt.id_match_team,
    TRUE,
    NULL
FROM
    team t
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    mt.id_match = m.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
WHERE
    r.no_round = 1 AND
    t.nm_team = 'Testers FC';       
INSERT INTO goal ( id_goal, id_match_team, fl_opponent_own_goal, id_match_team_player )
SELECT
    RANDOM_UUID(),
    mt.id_match_team,
    FALSE,
    mtp.id_match_team_player
FROM
    player p 
    INNER JOIN team t ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
    INNER JOIN match m ON
    mt.id_match = m.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
WHERE
    p.nm_given = 'Chloe' AND
    p.nm_family = 'Walker' AND
    r.no_round = 1 AND
    t.nm_team = 'X';
INSERT INTO goal ( id_goal, id_match_team, fl_opponent_own_goal, id_match_team_player )
SELECT
    RANDOM_UUID(),
    mt.id_match_team,
    FALSE,
    mtp.id_match_team_player
FROM
    player p 
    INNER JOIN team t ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
    INNER JOIN match m ON
    mt.id_match = m.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
WHERE
    p.nm_given = 'Ryan' AND
    p.nm_family = 'Campbell' AND
    r.no_round = 1 AND
    t.nm_team = '2KOOL4SKOOL';
INSERT INTO goal ( id_goal, id_match_team, fl_opponent_own_goal, id_match_team_player )
SELECT
    RANDOM_UUID(),
    mt.id_match_team,
    FALSE,
    mtp.id_match_team_player
FROM
    player p 
    INNER JOIN team t ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
    INNER JOIN match m ON
    mt.id_match = m.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
WHERE
    p.nm_given = 'Joshua' AND
    p.nm_family = 'Smith' AND
    r.no_round = 2 AND
    t.nm_team = 'The Motherf***ing Meme Team';
INSERT INTO goal ( id_goal, id_match_team, fl_opponent_own_goal, id_match_team_player )
SELECT
    RANDOM_UUID(),
    mt.id_match_team,
    FALSE,
    mtp.id_match_team_player
FROM
    player p 
    INNER JOIN team t ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
    INNER JOIN match m ON
    mt.id_match = m.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
WHERE
    p.nm_given = 'Emily' AND
    p.nm_family = 'Brown' AND
    r.no_round = 2 AND
    t.nm_team = 'The Motherf***ing Meme Team';
INSERT INTO goal ( id_goal, id_match_team, fl_opponent_own_goal, id_match_team_player )
SELECT
    RANDOM_UUID(),
    mt.id_match_team,
    FALSE,
    mtp.id_match_team_player
FROM
    player p 
    INNER JOIN team t ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
    INNER JOIN match m ON
    mt.id_match = m.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
WHERE
    p.nm_given = 'Emily' AND
    p.nm_family = 'Brown' AND
    r.no_round = 2 AND
    t.nm_team = 'The Motherf***ing Meme Team';
INSERT INTO goal ( id_goal, id_match_team, fl_opponent_own_goal, id_match_team_player )
SELECT
    RANDOM_UUID(),
    mt.id_match_team,
    FALSE,
    mtp.id_match_team_player
FROM
    player p 
    INNER JOIN team t ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
    INNER JOIN match m ON
    mt.id_match = m.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
WHERE
    p.nm_given = 'Emily' AND
    p.nm_family = 'Brown' AND
    r.no_round = 2 AND
    t.nm_team = 'The Motherf***ing Meme Team';
INSERT INTO goal ( id_goal, id_match_team, fl_opponent_own_goal, id_match_team_player )
SELECT
    RANDOM_UUID(),
    mt.id_match_team,
    FALSE,
    mtp.id_match_team_player
FROM
    player p 
    INNER JOIN team t ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
    INNER JOIN match m ON
    mt.id_match = m.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
WHERE
    p.nm_given = 'Emily' AND
    p.nm_family = 'Brown' AND
    r.no_round = 2 AND
    t.nm_team = 'The Motherf***ing Meme Team';
    
INSERT INTO golden_ball_vote ( id_golden_ball_vote, id_match_team_player, id_match_team, no_votes )
SELECT
    RANDOM_UUID(),
    mtp.id_match_team_player,
    mt.id_match_team,
    3
FROM
    team t
    INNER JOIN player p ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    m.id_match = mt.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
WHERE
    p.nm_given = 'Joshua' AND
    p.nm_family = 'Smith' AND
    r.no_round = 1 AND
    t.nm_team = 'The Motherf***ing Meme Team';
INSERT INTO golden_ball_vote ( id_golden_ball_vote, id_match_team_player, id_match_team, no_votes )
SELECT
    RANDOM_UUID(),
    mtp.id_match_team_player,
    mt.id_match_team,
    2
FROM
    team t
    INNER JOIN player p ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    m.id_match = mt.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
WHERE
    p.nm_given = 'Jack' AND
    p.nm_family = 'Williams' AND
    r.no_round = 1 AND
    t.nm_team = 'The Motherf***ing Meme Team';
INSERT INTO golden_ball_vote ( id_golden_ball_vote, id_match_team_player, id_match_team, no_votes )
SELECT
    RANDOM_UUID(),
    mtp.id_match_team_player,
    mt.id_match_team,
    1
FROM
    team t
    INNER JOIN player p ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    m.id_match = mt.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
WHERE
    p.nm_given = 'Sarah' AND
    p.nm_family = 'Taylor' AND
    r.no_round = 1 AND
    t.nm_team = 'The Motherf***ing Meme Team';
INSERT INTO golden_ball_vote ( id_golden_ball_vote, id_match_team_player, id_match_team, no_votes )
SELECT
    RANDOM_UUID(),
    mtp.id_match_team_player,
    mt.id_match_team,
    3
FROM
    team t
    INNER JOIN player p ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    m.id_match = mt.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
WHERE
    p.nm_given = 'Georgia' AND
    p.nm_family = 'White' AND
    r.no_round = 1 AND
    t.nm_team = 'Testers FC';
INSERT INTO golden_ball_vote ( id_golden_ball_vote, id_match_team_player, id_match_team, no_votes )
SELECT
    RANDOM_UUID(),
    mtp.id_match_team_player,
    mt.id_match_team,
    2
FROM
    team t
    INNER JOIN player p ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    m.id_match = mt.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
WHERE
    p.nm_given = 'Olivia' AND
    p.nm_family = 'Anderson' AND
    r.no_round = 1 AND
    t.nm_team = 'Testers FC';
INSERT INTO golden_ball_vote ( id_golden_ball_vote, id_match_team_player, id_match_team, no_votes )
SELECT
    RANDOM_UUID(),
    mtp.id_match_team_player,
    mt.id_match_team,
    1
FROM
    team t
    INNER JOIN player p ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    m.id_match = mt.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
WHERE
    p.nm_given = 'Emma' AND
    p.nm_family = 'Nguyen' AND
    r.no_round = 1 AND
    t.nm_team = 'Testers FC';
INSERT INTO golden_ball_vote ( id_golden_ball_vote, id_match_team_player, id_match_team, no_votes )
SELECT
    RANDOM_UUID(),
    mtp.id_match_team_player,
    mt.id_match_team,
    3
FROM
    team t
    INNER JOIN player p ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    m.id_match = mt.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
WHERE
    p.nm_given = 'Chloe' AND
    p.nm_family = 'Walker' AND
    r.no_round = 1 AND
    t.nm_team = 'X';
INSERT INTO golden_ball_vote ( id_golden_ball_vote, id_match_team_player, id_match_team, no_votes )
SELECT
    RANDOM_UUID(),
    mtp.id_match_team_player,
    mt.id_match_team,
    2
FROM
    team t
    INNER JOIN player p ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    m.id_match = mt.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
WHERE
    p.nm_given = 'Daniel' AND
    p.nm_family = 'Thomas' AND
    r.no_round = 1 AND
    t.nm_team = 'X';
INSERT INTO golden_ball_vote ( id_golden_ball_vote, id_match_team_player, id_match_team, no_votes )
SELECT
    RANDOM_UUID(),
    mtp.id_match_team_player,
    mt.id_match_team,
    1
FROM
    team t
    INNER JOIN player p ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    m.id_match = mt.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
WHERE
    p.nm_given = 'Nicholas' AND
    p.nm_family = 'Harris' AND
    r.no_round = 1 AND
    t.nm_team = 'X';
INSERT INTO golden_ball_vote ( id_golden_ball_vote, id_match_team_player, id_match_team, no_votes )
SELECT
    RANDOM_UUID(),
    mtp.id_match_team_player,
    mt.id_match_team,
    3
FROM
    team t
    INNER JOIN player p ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    m.id_match = mt.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
WHERE
    p.nm_given = 'Jacob' AND
    p.nm_family = 'Roberts' AND
    r.no_round = 1 AND
    t.nm_team = '2KOOL4SKOOL';
INSERT INTO golden_ball_vote ( id_golden_ball_vote, id_match_team_player, id_match_team, no_votes )
SELECT
    RANDOM_UUID(),
    mtp.id_match_team_player,
    mt.id_match_team,
    2
FROM
    team t
    INNER JOIN player p ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    m.id_match = mt.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
WHERE
    p.nm_given = 'Ryan' AND
    p.nm_family = 'Campbell' AND
    r.no_round = 1 AND
    t.nm_team = '2KOOL4SKOOL';
INSERT INTO golden_ball_vote ( id_golden_ball_vote, id_match_team_player, id_match_team, no_votes )
SELECT
    RANDOM_UUID(),
    mtp.id_match_team_player,
    mt.id_match_team,
    1
FROM
    team t
    INNER JOIN player p ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    m.id_match = mt.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
WHERE
    p.nm_given = 'William' AND
    p.nm_family = 'Kelly' AND
    r.no_round = 1 AND
    t.nm_team = '2KOOL4SKOOL';
INSERT INTO golden_ball_vote ( id_golden_ball_vote, id_match_team_player, id_match_team, no_votes )
SELECT
    RANDOM_UUID(),
    mtp.id_match_team_player,
    mt.id_match_team,
    3
FROM
    team t
    INNER JOIN player p ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    m.id_match = mt.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
WHERE
    p.nm_given = 'Jack' AND
    p.nm_family = 'Williams' AND
    r.no_round = 2 AND
    t.nm_team = 'The Motherf***ing Meme Team';
INSERT INTO golden_ball_vote ( id_golden_ball_vote, id_match_team_player, id_match_team, no_votes )
SELECT
    RANDOM_UUID(),
    mtp.id_match_team_player,
    mt.id_match_team,
    2
FROM
    team t
    INNER JOIN player p ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    m.id_match = mt.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
WHERE
    p.nm_given = 'Joshua' AND
    p.nm_family = 'Smith' AND
    r.no_round = 2 AND
    t.nm_team = 'The Motherf***ing Meme Team';
INSERT INTO golden_ball_vote ( id_golden_ball_vote, id_match_team_player, id_match_team, no_votes )
SELECT
    RANDOM_UUID(),
    mtp.id_match_team_player,
    mt.id_match_team,
    1
FROM
    team t
    INNER JOIN player p ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    m.id_match = mt.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
WHERE
    p.nm_given = 'Emily' AND
    p.nm_family = 'Brown' AND
    r.no_round = 2 AND
    t.nm_team = 'The Motherf***ing Meme Team';
INSERT INTO golden_ball_vote ( id_golden_ball_vote, id_match_team_player, id_match_team, no_votes )
SELECT
    RANDOM_UUID(),
    mtp.id_match_team_player,
    mt.id_match_team,
    3
FROM
    team t
    INNER JOIN player p ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    m.id_match = mt.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
WHERE
    p.nm_given = 'Benjamin' AND
    p.nm_family = 'Ryan' AND
    r.no_round = 2 AND
    t.nm_team = 'X';
INSERT INTO golden_ball_vote ( id_golden_ball_vote, id_match_team_player, id_match_team, no_votes )
SELECT
    RANDOM_UUID(),
    mtp.id_match_team_player,
    mt.id_match_team,
    2
FROM
    team t
    INNER JOIN player p ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    m.id_match = mt.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
WHERE
    p.nm_given = 'Hannah' AND
    p.nm_family = 'Robinson' AND
    r.no_round = 2 AND
    t.nm_team = 'X';
INSERT INTO golden_ball_vote ( id_golden_ball_vote, id_match_team_player, id_match_team, no_votes )
SELECT
    RANDOM_UUID(),
    mtp.id_match_team_player,
    mt.id_match_team,
    1
FROM
    team t
    INNER JOIN player p ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    m.id_match = mt.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
WHERE
    p.nm_given = 'Sophie' AND
    p.nm_family = 'Lee' AND
    r.no_round = 2 AND
    t.nm_team = 'X';
INSERT INTO golden_ball_vote ( id_golden_ball_vote, id_match_team_player, id_match_team, no_votes )
SELECT
    RANDOM_UUID(),
    mtp.id_match_team_player,
    mt.id_match_team,
    3
FROM
    team t
    INNER JOIN player p ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    m.id_match = mt.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
WHERE
    p.nm_given = 'Emma' AND
    p.nm_family = 'Nguyen' AND
    r.no_round = 2 AND
    t.nm_team = 'Testers FC';
INSERT INTO golden_ball_vote ( id_golden_ball_vote, id_match_team_player, id_match_team, no_votes )
SELECT
    RANDOM_UUID(),
    mtp.id_match_team_player,
    mt.id_match_team,
    2
FROM
    team t
    INNER JOIN player p ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    m.id_match = mt.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
WHERE
    p.nm_given = 'Matthew' AND
    p.nm_family = 'Martin' AND
    r.no_round = 2 AND
    t.nm_team = 'Testers FC';
INSERT INTO golden_ball_vote ( id_golden_ball_vote, id_match_team_player, id_match_team, no_votes )
SELECT
    RANDOM_UUID(),
    mtp.id_match_team_player,
    mt.id_match_team,
    1
FROM
    team t
    INNER JOIN player p ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    m.id_match = mt.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
WHERE
    p.nm_given = 'Georgia' AND
    p.nm_family = 'White' AND
    r.no_round = 2 AND
    t.nm_team = 'Testers FC';
INSERT INTO golden_ball_vote ( id_golden_ball_vote, id_match_team_player, id_match_team, no_votes )
SELECT
    RANDOM_UUID(),
    mtp.id_match_team_player,
    mt.id_match_team,
    3
FROM
    team t
    INNER JOIN player p ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    m.id_match = mt.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
WHERE
    p.nm_given = 'Jacob' AND
    p.nm_family = 'Roberts' AND
    r.no_round = 2 AND
    t.nm_team = '2KOOL4SKOOL';
INSERT INTO golden_ball_vote ( id_golden_ball_vote, id_match_team_player, id_match_team, no_votes )
SELECT
    RANDOM_UUID(),
    mtp.id_match_team_player,
    mt.id_match_team,
    2
FROM
    team t
    INNER JOIN player p ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    m.id_match = mt.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
WHERE
    p.nm_given = 'Isabella' AND
    p.nm_family = 'King' AND
    r.no_round = 2 AND
    t.nm_team = '2KOOL4SKOOL';
INSERT INTO golden_ball_vote ( id_golden_ball_vote, id_match_team_player, id_match_team, no_votes )
SELECT
    RANDOM_UUID(),
    mtp.id_match_team_player,
    mt.id_match_team,
    1
FROM
    team t
    INNER JOIN player p ON
    p.id_team = t.id_team
    INNER JOIN match_team mt ON
    mt.id_team = t.id_team
    INNER JOIN match m ON
    m.id_match = mt.id_match
    INNER JOIN round r ON
    m.id_round = r.id_round
    INNER JOIN match_team_player mtp ON
    mtp.id_player = p.id_player AND
    mtp.id_match_team = mt.id_match_team
WHERE
    p.nm_given = 'Caitlin' AND
    p.nm_family = 'Davis' AND
    r.no_round = 2 AND
    t.nm_team = '2KOOL4SKOOL';