-- Sample bins used for local development and API exploration.
INSERT INTO bins (bin_code, name, latitude, longitude, bin_type, capacity_liters, active, created_at)
VALUES
    ('BIN-LSK-001', 'Cairo Road Bus Stop', -15.416667, 28.283333, 'REAL', 240, TRUE, '2026-01-15 08:00:00+00'),
    ('BIN-LSK-002', 'East Park Mall Entrance', -15.391667, 28.322222, 'REAL', 660, TRUE, '2026-01-15 08:05:00+00'),
    ('BIN-LSK-003', 'University of Zambia Gate', -15.389722, 28.332778, 'SIMULATED', 240, TRUE, '2026-01-15 08:10:00+00'),
    ('BIN-LSK-004', 'Kamwala Market', -15.430556, 28.293611, 'SIMULATED', 660, FALSE, '2026-01-15 08:15:00+00');

-- Recent readings for each seeded bin. Bin IDs are resolved from their stable codes.
INSERT INTO fill_readings (bin_id, fill_percentage, distance_cm, temperature_c, recorded_at)
VALUES
    ((SELECT id FROM bins WHERE bin_code = 'BIN-LSK-001'), 72.50, 18.40, 27.20, '2026-01-16 09:00:00+00'),
    ((SELECT id FROM bins WHERE bin_code = 'BIN-LSK-001'), 81.25, 14.20, 28.10, '2026-01-16 12:00:00+00'),
    ((SELECT id FROM bins WHERE bin_code = 'BIN-LSK-002'), 35.00, 42.80, 26.80, '2026-01-16 09:15:00+00'),
    ((SELECT id FROM bins WHERE bin_code = 'BIN-LSK-002'), 48.75, 35.50, 27.60, '2026-01-16 12:15:00+00'),
    ((SELECT id FROM bins WHERE bin_code = 'BIN-LSK-003'), 58.00, 29.70, 25.90, '2026-01-16 09:30:00+00'),
    ((SELECT id FROM bins WHERE bin_code = 'BIN-LSK-003'), 64.50, 24.90, 26.70, '2026-01-16 12:30:00+00'),
    ((SELECT id FROM bins WHERE bin_code = 'BIN-LSK-004'), 92.00, 6.40, 29.30, '2026-01-16 09:45:00+00');
