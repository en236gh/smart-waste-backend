ALTER TABLE bins
    ADD COLUMN lid_state VARCHAR(10) NOT NULL DEFAULT 'CLOSED'
        CHECK (lid_state IN ('OPEN', 'CLOSED')),
    ADD COLUMN lid_command VARCHAR(10) NOT NULL DEFAULT 'AUTO'
        CHECK (lid_command IN ('AUTO', 'OPEN', 'CLOSED'));