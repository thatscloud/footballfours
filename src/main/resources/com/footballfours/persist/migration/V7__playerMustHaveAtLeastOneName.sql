ALTER TABLE player ADD CONSTRAINT chk2_player CHECK 
    nm_given IS NOT NULL AND LENGTH( nm_given ) > 0 OR
    nm_family IS NOT NULL AND LENGTH( nm_family ) > 0;