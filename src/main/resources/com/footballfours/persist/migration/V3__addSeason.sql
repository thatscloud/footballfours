CREATE TABLE season
(
    id_season UUID PRIMARY KEY,
    nm_season VARCHAR( 255 ) NOT NULL,
    CONSTRAINT ak1_season UNIQUE ( nm_season ),
);

ALTER TABLE team ADD COLUMN id_season UUID;

ALTER TABLE round ADD COLUMN id_season UUID;

ALTER TABLE match ADD COLUMN id_season UUID;

ALTER TABLE match_team ADD COLUMN id_season UUID;

ALTER TABLE team ADD CONSTRAINT fk1_team FOREIGN KEY ( id_season )
    REFERENCES season ( id_season )
    ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE round ADD CONSTRAINT fk1_round FOREIGN KEY ( id_season )
    REFERENCES season ( id_season )
    ON DELETE RESTRICT ON UPDATE RESTRICT;
    
ALTER TABLE match ADD CONSTRAINT fk2_match FOREIGN KEY ( id_round, id_season )
    REFERENCES round ( id_round, id_season )
    ON DELETE RESTRICT ON UPDATE RESTRICT;
    
ALTER TABLE match_team ADD CONSTRAINT fk3_match_team FOREIGN KEY ( id_match, id_season )
    REFERENCES match ( id_match, id_season )
    ON DELETE RESTRICT ON UPDATE RESTRICT;
    
ALTER TABLE match_team ADD CONSTRAINT fk4_match_team FOREIGN KEY ( id_team, id_season )
    REFERENCES team ( id_team, id_season )
    ON DELETE RESTRICT ON UPDATE RESTRICT;
    
CREATE TABLE parameter
(
    id_parameter UUID PRIMARY KEY,
    cd_parameter VARCHAR( 255 ) NOT NULL,
    tx_parameter TEXT( 4096 ),
    CONSTRAINT ak1_parameter UNIQUE( cd_parameter )
);