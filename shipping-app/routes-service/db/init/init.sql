-- Create user if not exists
DO
$do$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'routes_user') THEN
      CREATE USER routes_user WITH PASSWORD 'routes_password';
   END IF;
END
$do$;
CREATE DATABASE routes_db;
GRANT ALL PRIVILEGES ON DATABASE routes_db TO routes_user;

\c routes_db;

-- Create tables
CREATE TABLE IF NOT EXISTS routes (
    id BIGSERIAL PRIMARY KEY,
    origin_lat DOUBLE PRECISION NOT NULL,
    origin_lng DOUBLE PRECISION NOT NULL,
    destination_lat DOUBLE PRECISION NOT NULL,
    destination_lng DOUBLE PRECISION NOT NULL,
    current_lat DOUBLE PRECISION,
    current_lng DOUBLE PRECISION,
    estimated_duration INTEGER,
    eta TIMESTAMP,
    status VARCHAR(50),
    traffic_conditions JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS checkpoints (
    id BIGSERIAL PRIMARY KEY,
    route_id BIGINT REFERENCES routes(id),
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Grant permissions
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO routes_user;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO routes_user;

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_route_status ON routes(status);
CREATE INDEX IF NOT EXISTS idx_checkpoints_route ON checkpoints(route_id);

-- Insert sample data
INSERT INTO routes (origin_lat, origin_lng, destination_lat, destination_lng, status, estimated_duration)
VALUES 
    (40.7128, -74.0060, 34.0522, -118.2437, 'ACTIVE', 180),
    (41.8781, -87.6298, 25.7617, -80.1918, 'PENDING', 120),
    (47.6062, -122.3321, 42.3601, -71.0589, 'COMPLETED', 300);
SELECT 40.7128, -74.0060, 34.0522, -118.2437, 'ACTIVE', 180
WHERE NOT EXISTS (SELECT 1 FROM routes LIMIT 1);