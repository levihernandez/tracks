-- Sample routes
INSERT INTO routes (origin_lat, origin_lng, destination_lat, destination_lng, status, estimated_duration)
VALUES 
    (40.7128, -74.0060, 34.0522, -118.2437, 'ACTIVE', 180),
    (41.8781, -87.6298, 25.7617, -80.1918, 'PENDING', 120),
    (47.6062, -122.3321, 42.3601, -71.0589, 'COMPLETED', 300);

-- Sample checkpoints
INSERT INTO checkpoints (route_id, latitude, longitude, timestamp, status)
VALUES 
    (1, 39.7392, -104.9903, CURRENT_TIMESTAMP - interval '2 hours', 'REACHED'),
    (1, 38.8977, -77.0365, CURRENT_TIMESTAMP - interval '4 hours', 'REACHED'),
    (2, 35.2271, -80.8431, CURRENT_TIMESTAMP - interval '1 hour', 'REACHED');