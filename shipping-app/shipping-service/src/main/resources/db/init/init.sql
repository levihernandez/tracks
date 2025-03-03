-- Create user if not exists
DO
$do$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'shipping_user') THEN
      CREATE USER shipping_user WITH PASSWORD 'shipping_password';
   END IF;
END
$do$;
-- Create database
CREATE DATABASE shipping_db;
GRANT ALL PRIVILEGES ON DATABASE shipping_db TO shipping_user;

\c shipping_db;

-- Create tables
CREATE TABLE IF NOT EXISTS shipping_orders (
    id BIGSERIAL PRIMARY KEY,
    order_number VARCHAR(50) UNIQUE NOT NULL,
    origin VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    cost DECIMAL(10,2) NOT NULL,
    route_id BIGINT,
    current_lat DOUBLE PRECISION,
    current_lng DOUBLE PRECISION,
    estimated_time_arrival TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Grant permissions
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO shipping_user;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO shipping_user;

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_order_number ON shipping_orders(order_number);
CREATE INDEX IF NOT EXISTS idx_status ON shipping_orders(status);

-- Insert sample data
INSERT INTO shipping_orders (order_number, origin, destination, status, cost, route_id)
VALUES 
    ('SHP001', 'New York, NY', 'Los Angeles, CA', 'IN_TRANSIT', 249.99, 1),
    ('SHP002', 'Chicago, IL', 'Miami, FL', 'PENDING', 189.99, 2),
    ('SHP003', 'Seattle, WA', 'Boston, MA', 'DELIVERED', 299.99, 3);

SELECT 'SHP001', 'New York, NY', 'Los Angeles, CA', 'IN_TRANSIT', 249.99, 1
WHERE NOT EXISTS (SELECT 1 FROM shipping_orders WHERE order_number = 'SHP001');