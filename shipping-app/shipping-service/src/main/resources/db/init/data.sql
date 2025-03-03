-- Sample shipping orders with coordinates along actual routes
INSERT INTO shipping_orders (order_number, origin, destination, status, cost, route_id, current_lat, current_lng, estimated_time_arrival)
VALUES 
    -- East to West Coast Routes
    ('SHP001', 'New York, NY', 'Los Angeles, CA', 'IN_TRANSIT', 249.99, 1, 41.0359, -92.4086, CURRENT_TIMESTAMP + INTERVAL '3 days'),
    ('SHP002', 'Boston, MA', 'San Francisco, CA', 'IN_TRANSIT', 289.99, 2, 41.8781, -87.6298, CURRENT_TIMESTAMP + INTERVAL '4 days'),
    ('SHP003', 'Miami, FL', 'Seattle, WA', 'IN_TRANSIT', 319.99, 3, 32.7767, -96.7970, CURRENT_TIMESTAMP + INTERVAL '5 days'),
    ('SHP004', 'Philadelphia, PA', 'Portland, OR', 'PENDING', 279.99, 4, 39.9526, -75.1652, CURRENT_TIMESTAMP + INTERVAL '4 days'),
    ('SHP005', 'Washington, DC', 'San Diego, CA', 'IN_TRANSIT', 259.99, 5, 38.9072, -94.8617, CURRENT_TIMESTAMP + INTERVAL '3 days');