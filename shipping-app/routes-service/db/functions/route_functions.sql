-- Function to calculate distance between two points
CREATE OR REPLACE FUNCTION calculate_distance(
    lat1 DOUBLE PRECISION,
    lon1 DOUBLE PRECISION,
    lat2 DOUBLE PRECISION,
    lon2 DOUBLE PRECISION
) RETURNS DOUBLE PRECISION AS $$
DECLARE
    R DOUBLE PRECISION := 6371; -- Earth's radius in kilometers
    dlat DOUBLE PRECISION;
    dlon DOUBLE PRECISION;
    a DOUBLE PRECISION;
    c DOUBLE PRECISION;
BEGIN
    dlat := radians(lat2 - lat1);
    dlon := radians(lon2 - lon1);
    a := sin(dlat/2)^2 + cos(radians(lat1)) * cos(radians(lat2)) * sin(dlon/2)^2;
    c := 2 * asin(sqrt(a));
    RETURN R * c;
END;
$$ LANGUAGE plpgsql;

-- Function to update ETA based on traffic conditions
CREATE OR REPLACE FUNCTION update_route_eta(route_id BIGINT) RETURNS TIMESTAMP AS $$
DECLARE
    route_record RECORD;
    base_duration INTEGER;
    traffic_multiplier DOUBLE PRECISION;
BEGIN
    SELECT * INTO route_record FROM routes WHERE id = route_id;
    
    base_duration := route_record.estimated_duration;
    traffic_multiplier := COALESCE((route_record.traffic_conditions->>'factor')::DOUBLE PRECISION, 1.0);
    
    RETURN CURRENT_TIMESTAMP + (base_duration * traffic_multiplier || ' minutes')::INTERVAL;
END;
$$ LANGUAGE plpgsql;