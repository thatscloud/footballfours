ALTER TABLE round DROP CONSTRAINT chk1_round;
ALTER TABLE round ADD CONSTRAINT chk1_round CHECK no_round >= 1;