ALTER TABLE player DROP CONSTRAINT chk1_player;
ALTER TABLE player ADD CONSTRAINT chk1_player CHECK no_team_player >= 1;
ALTER TABLE match_team_player DROP CONSTRAINT chk1_match_team_player;
ALTER TABLE match_team_player ADD CONSTRAINT chk1_match_team_player CHECK 
    no_match_team_player >= 1 AND no_match_team_player <= 8;