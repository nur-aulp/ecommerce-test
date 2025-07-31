-- Users
INSERT INTO users (username, email, password_hash, full_name, address, phone) VALUES
('alice', 'alice@example.com', 'hashed_pw1', 'Alice Smith', '123 Main St', '081234567890'),
('bob', 'bob@example.com', 'hashed_pw2', 'Bob Johnson', '456 Oak Ave', '082345678901');

-- Categories
INSERT INTO categories (name, description) VALUES
('Electronics', 'Electronic gadgets and devices'),
('Books', 'Various kinds of books');

-- Products
INSERT INTO products (name, description, price, stock, category_id, image_url) VALUES
('Smartphone', 'Latest model smartphone', 3500000.00, 10, 1, 'img/smartphone.jpg'),
('Laptop', 'High performance laptop', 8500000.00, 5, 1, 'img/laptop.jpg'),
('Novel', 'Bestselling novel', 120000.00, 20, 2, 'img/novel.jpg');

-- Product Images
INSERT INTO product_images (product_id, image_url) VALUES
(1, 'img/smartphone_1.jpg'),
(1, 'img/smartphone_2.jpg'),
(2, 'img/laptop_1.jpg'),
(3, 'img/novel_1.jpg');

-- Carts
INSERT INTO carts (user_id) VALUES
(1),
(2);

-- Cart Items
INSERT INTO cart_items (cart_id, product_id, quantity) VALUES
(1, 1, 2),
(1, 3, 1),
(2, 2, 1);

-- Orders
INSERT INTO orders (user_id, total_amount, status, shipping_address, payment_method) VALUES
(1, 3620000.00, 'pending', '123 Main St', 'Credit Card'),
(2, 8500000.00, 'paid', '456 Oak Ave', 'Bank Transfer');

-- Order Items
INSERT INTO order_items (order_id, product_id, product_name, price, quantity) VALUES
(1, 1, 'Smartphone', 3500000.00, 1),
(1, 3, 'Novel', 120000.00, 1),
(2, 2, 'Laptop', 8500000.00, 1);

-- Transactions
INSERT INTO transactions (order_id, amount, payment_status, payment_reference) VALUES
(2, 8500000.00, 'success', 'INV-20250731-001');