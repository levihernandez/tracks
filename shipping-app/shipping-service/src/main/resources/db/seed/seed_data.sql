-- Sample shipping orders
INSERT INTO shipping_orders (order_number, origin, destination, status, cost, route_id)
VALUES 
    ('SHP001', 'New York, NY', 'Los Angeles, CA', 'IN_TRANSIT', 249.99, 1),
    ('SHP002', 'Chicago, IL', 'Miami, FL', 'PENDING', 189.99, 2),
    ('SHP003', 'Seattle, WA', 'Boston, MA', 'DELIVERED', 299.99, 3);