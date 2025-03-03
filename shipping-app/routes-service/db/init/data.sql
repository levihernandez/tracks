INSERT INTO routes (origin_lat, origin_lng, destination_lat, destination_lng, current_lat, current_lng, status, estimated_duration)
VALUES
    -- East to West Coast Routes
    (40.7128, -74.0060, 34.0522, -118.2437, 41.0359, -92.4086, 'ACTIVE', 259200),  -- NYC to LA
    (42.3601, -71.0589, 37.7749, -122.4194, 41.8781, -87.6298, 'ACTIVE', 345600),  -- Boston to SF
    (25.7617, -80.1918, 47.6062, -122.3321, 32.7767, -96.7970, 'ACTIVE', 432000),  -- Miami to Seattle
    (39.9526, -75.1652, 45.5155, -122.6789, 39.9526, -75.1652, 'PENDING', 345600), -- Philly to Portland
    (38.9072, -77.0369, 32.7157, -117.1611, 38.9072, -94.8617, 'ACTIVE', 259200);  -- DC to San Diego
