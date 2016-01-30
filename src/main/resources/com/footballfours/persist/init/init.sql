CREATE TABLE db_info
(
    cd_parameter VARCHAR( 255 ) PRIMARY KEY,
    tx_parameter VARCHAR( 255 ) NOT NULL,
);

CREATE TABLE team
(
    id_team UUID PRIMARY KEY,
    nm_team VARCHAR( 255 ) NOT NULL
);

CREATE TABLE round
(
    id_round UUID PRIMARY KEY,
    no_round INT NOT NULL,
    CONSTRAINT chk1_round CHECK no_round >= 1 AND no_round <= 10
);

CREATE TABLE match
(
    id_match UUID PRIMARY KEY,
    id_round UUID NOT NULL,
    dt_scheduled DATETIME NOT NULL,
    dt_played DATETIME NOT NULL,
    cd_status VARCHAR( 255 ) DEFAULT NULL,
    CONSTRAINT chk1_match CHECK cd_status IS NULL OR cd_status = 'COMPLETED',
    CONSTRAINT fk1_match FOREIGN KEY ( id_round ) 
        REFERENCES round ( id_round ) 
        ON DELETE RESTRICT ON UPDATE RESTRICT   
);

CREATE TABLE match_team
(
    id_match_team UUID PRIMARY KEY,
    id_team UUID NOT NULL,
    id_match UUID NOT NULL,
    id_round UUID NOT NULL,
    cd_team_type VARCHAR( 255 ) NOT NULL,
    CONSTRAINT chk1_match_team CHECK cd_team_type = 'HOME' OR cd_team_type = 'AWAY',
    CONSTRAINT ak1_match_team UNIQUE ( id_match, id_team ),
    CONSTRAINT ak2_match_team UNIQUE ( id_match, cd_team_type ),
    CONSTRAINT ak3_match_team UNIQUE ( id_round, id_team ),
    CONSTRAINT fk1_match_team FOREIGN KEY ( id_match, id_round )
        REFERENCES match ( id_match, id_round )
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk2_match_team FOREIGN KEY ( id_team )
        REFERENCES team ( id_team )
        ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE player
(
    id_player UUID PRIMARY KEY,
    id_team UUID NOT NULL,
    no_team_player INT NOT NULL,
    nm_given VARCHAR( 255 ) NOT NULL,
    nm_family VARCHAR( 255 ),
    CONSTRAINT chk1_player CHECK no_team_player >= 1 AND no_team_player <= 7,
    CONSTRAINT ak1_player UNIQUE ( id_team, no_team_player ),
    CONSTRAINT fk1_player FOREIGN KEY ( id_team )
        REFERENCES team ( id_team )
        ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE match_team_player
(
    id_match_team_player UUID PRIMARY KEY,
    id_player UUID NOT NULL,
    id_team UUID NOT NULL,
    id_match_team UUID NOT NULL,
    no_match_team_player INT NOT NULL,
    CONSTRAINT chk1_match_team_player CHECK no_match_team_player >= 1 AND no_match_team_player <= 6,
    CONSTRAINT ak1_match_team_player UNIQUE ( id_player, id_match_team ),
    CONSTRAINT ak2_match_team_player UNIQUE ( no_match_team_player, id_match_team ),
    CONSTRAINT fk1_match_team_player FOREIGN KEY ( id_player, id_team )
        REFERENCES player ( id_player, id_team )
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk2_match_team_player FOREIGN KEY ( id_match_team, id_team )
        REFERENCES match_team ( id_match_team, id_team )
        ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE goal
(
    id_goal UUID PRIMARY KEY,
    id_match_team UUID NOT NULL,
    fl_opponent_own_goal BOOLEAN DEFAULT FALSE NOT NULL,
    id_match_team_player UUID,
    CONSTRAINT chk1_goal CHECK fl_opponent_own_goal AND id_match_team_player IS NULL OR
                    NOT fl_opponent_own_goal AND id_match_team_player IS NOT NULL,
    CONSTRAINT fk1_goal FOREIGN KEY ( id_match_team_player, id_match_team )
        REFERENCES match_team_player ( id_match_team_player, id_match_team )
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk2_goal FOREIGN KEY ( id_match_team )
        REFERENCES match_team ( id_match_team )
        ON DELETE RESTRICT ON UPDATE RESTRICT
        
);

CREATE TABLE golden_ball_vote
(
    id_golden_ball_vote UUID PRIMARY KEY,
    id_match_team_player UUID NOT NULL,
    id_match_team UUID NOT NULL,
    no_votes INT NOT NULL,
    CONSTRAINT chk1_golden_ball_vote CHECK no_votes >= 1 AND no_votes <= 3,
    CONSTRAINT ak1_golden_ball_vote UNIQUE ( id_match_team_player ),
    CONSTRAINT ak2_golden_ball_vote UNIQUE ( id_match_team, no_votes ),
    CONSTRAINT fk1_golden_ball_vote FOREIGN KEY ( id_match_team_player, id_match_team )
        REFERENCES match_team_player ( id_match_team_player, id_match_team )
        ON DELETE RESTRICT ON UPDATE RESTRICT
);

INSERT INTO db_info
(
   cd_parameter,
   tx_parameter
)
VALUES
(
   'VERSION',
   '0.0.1'
);