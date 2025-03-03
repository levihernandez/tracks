-- Function to move packages along their routes
CREATE OR REPLACE FUNCTION update_package_locations() RETURNS void AS $$
DECLARE
    speed_factor FLOAT := 0.05; -- Controls how far packages move each update
BEGIN
    -- Update active routes
    UPDATE routes
    SET current_lat = CASE 
            WHEN status = 'ACTIVE' THEN 
                current_lat + (destination_lat - origin_lat) * speed_factor * random()
            ELSE current_lat
        END,
        current_lng = CASE 
            WHEN status = 'ACTIVE' THEN 
                current_lng + (destination_lng - origin_lng) * speed_factor * random()
            ELSE current_lng
        END
    WHERE status = 'ACTIVE';
    
    -- Sync shipping orders with routes
    UPDATE shipping_orders so
    SET current_lat = r.current_lat,
        current_lng = r.current_lng
    FROM routes r
    WHERE so.route_id = r.id
    AND so.status = 'IN_TRANSIT';
END;
$$ LANGUAGE plpgsql;

-- Create a function to periodically update locations (every 5 minutes)
SELECT cron.schedule('*/5 * * * *', $$
    SELECT update_package_locations();
$$);