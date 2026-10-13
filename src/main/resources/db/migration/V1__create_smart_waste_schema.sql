CREATE TABLE bins (
    id BIGSERIAL PRIMARY KEY,
    bin_code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100),
    latitude DECIMAL(9, 6) NOT NULL,
    longitude DECIMAL(9, 6) NOT NULL,
    bin_type VARCHAR(20) NOT NULL
        CHECK (bin_type IN ('REAL', 'SIMULATED')),
    capacity_liters INTEGER,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE fill_readings (
    id BIGSERIAL PRIMARY KEY,
    bin_id BIGINT NOT NULL,
    fill_percentage DECIMAL(5, 2) NOT NULL
        CHECK (fill_percentage >= 0 AND fill_percentage <= 100),
    distance_cm DECIMAL(8, 2),
    temperature_c DECIMAL(5, 2),
    recorded_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_fill_reading_bin
        FOREIGN KEY (bin_id)
        REFERENCES bins(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_fill_readings_bin_time
ON fill_readings (bin_id, recorded_at DESC);