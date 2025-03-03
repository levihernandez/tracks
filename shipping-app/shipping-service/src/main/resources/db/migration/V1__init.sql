-- Initial schema
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

-- Indexes
CREATE INDEX idx_order_number ON shipping_orders(order_number);
CREATE INDEX idx_status ON shipping_orders(status);

-- Trigger for updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_shipping_orders_updated_at
    BEFORE UPDATE ON shipping_orders
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();