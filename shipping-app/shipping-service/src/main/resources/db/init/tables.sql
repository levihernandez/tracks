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

CREATE INDEX IF NOT EXISTS idx_order_number ON shipping_orders(order_number);
CREATE INDEX IF NOT EXISTS idx_status ON shipping_orders(status);