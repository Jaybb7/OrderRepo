-- ==========================
-- Items (base table)
-- ==========================
INSERT INTO items (id, price, quantity) VALUES (1, 1200.00, 10);
INSERT INTO items (id, price, quantity) VALUES (2, 1500.00, 8);
INSERT INTO items (id, price, quantity) VALUES (3, 1800.00, 6);
INSERT INTO items (id, price, quantity) VALUES (4, 2000.00, 5);
INSERT INTO items (id, price, quantity) VALUES (5, 2200.00, 3);

INSERT INTO items (id, price, quantity) VALUES (6, 800.00, 20);
INSERT INTO items (id, price, quantity) VALUES (7, 950.00, 18);
INSERT INTO items (id, price, quantity) VALUES (8, 1100.00, 15);
INSERT INTO items (id, price, quantity) VALUES (9, 1300.00, 12);
INSERT INTO items (id, price, quantity) VALUES (10, 1400.00, 10);

-- ==========================
-- Laptops (subclass of Items)
-- ==========================
INSERT INTO laptop (id, processor, ram_size, storage)
VALUES (1, 'Intel i5', 8, 256);

INSERT INTO laptop (id, processor, ram_size, storage)
VALUES (2, 'Intel i7', 16, 512);

INSERT INTO laptop (id, processor, ram_size, storage)
VALUES (3, 'AMD Ryzen 5', 16, 512);

INSERT INTO laptop (id, processor, ram_size, storage)
VALUES (4, 'Intel i9', 32, 1024);

INSERT INTO laptop (id, processor, ram_size, storage)
VALUES (5, 'Apple M2', 16, 512);

-- ==========================
-- Mobile Phones (subclass of Items)
-- ==========================
INSERT INTO mobile_phone (id, brand, battery_capacity, has5g)
VALUES (6, 'Samsung', 4500, true);

INSERT INTO mobile_phone (id, brand, battery_capacity, has5g)
VALUES (7, 'Apple', 4000, true);

INSERT INTO mobile_phone (id, brand, battery_capacity, has5g)
VALUES (8, 'Google', 4200, true);

INSERT INTO mobile_phone (id, brand, battery_capacity, has5g)
VALUES (9, 'OnePlus', 4800, true);

INSERT INTO mobile_phone (id, brand, battery_capacity, has5g)
VALUES (10, 'Xiaomi', 5000, true);

-- ==========================
-- Delivery Drivers
-- ==========================
INSERT INTO delivery_driver (driver_id, driver_name, driver_phone, driver_email, driver_address)
VALUES (1, 'Alice Johnson', '0400000001', 'alice.johnson@example.com', '123 King St, Sydney');

INSERT INTO delivery_driver (driver_id, driver_name, driver_phone, driver_email, driver_address)
VALUES (2, 'Bob Smith', '0400000002', 'bob.smith@example.com', '456 Queen St, Melbourne');

INSERT INTO delivery_driver (driver_id, driver_name, driver_phone, driver_email, driver_address)
VALUES (3, 'Charlie Brown', '0400000003', 'charlie.brown@example.com', '789 George St, Brisbane');

INSERT INTO delivery_driver (driver_id, driver_name, driver_phone, driver_email, driver_address)
VALUES (4, 'Diana Prince', '0400000004', 'diana.prince@example.com', '321 Crown St, Perth');

INSERT INTO delivery_driver (driver_id, driver_name, driver_phone, driver_email, driver_address)
VALUES (5, 'Ethan Hunt', '0400000005', 'ethan.hunt@example.com', '654 Harbour Rd, Adelaide');