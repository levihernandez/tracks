-- Function to generate a random point along a route
CREATE OR REPLACE FUNCTION random_route_point(
    start_lat FLOAT, 
    start_lng FLOAT, 
    end_lat FLOAT, 
    end_lng FLOAT
) RETURNS TABLE (lat FLOAT, lng FLOAT) AS $$
BEGIN
    -- Generate a random progress (0 to 1) along the route
    RETURN QUERY SELECT 
        start_lat + (end_lat - start_lat) * random() AS lat,
        start_lng + (end_lng - start_lng) * random() AS lng;
END;
$$ LANGUAGE plpgsql;