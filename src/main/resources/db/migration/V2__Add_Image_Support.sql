-- Add image support to existing tables

-- Add profile image to users
ALTER TABLE users ADD COLUMN profile_image_url VARCHAR(500);
ALTER TABLE users ADD COLUMN bio TEXT;
ALTER TABLE users ADD COLUMN location VARCHAR(100);

-- Add banner and logo images to stores  
ALTER TABLE stores ADD COLUMN banner_image_url VARCHAR(500);
ALTER TABLE stores ADD COLUMN logo_image_url VARCHAR(500);
ALTER TABLE stores ADD COLUMN established_year INTEGER;
ALTER TABLE stores ADD COLUMN total_products INTEGER DEFAULT 0;
ALTER TABLE stores ADD COLUMN total_sales INTEGER DEFAULT 0;

-- Add multiple images to products
ALTER TABLE products ADD COLUMN image_url VARCHAR(500);
ALTER TABLE products ADD COLUMN gallery_images TEXT; -- JSON array of image URLs
ALTER TABLE products ADD COLUMN discount_percentage DECIMAL(5,2) DEFAULT 0;
ALTER TABLE products ADD COLUMN review_count INTEGER DEFAULT 0;
ALTER TABLE products ADD COLUMN is_featured BOOLEAN DEFAULT FALSE;
ALTER TABLE products ADD COLUMN brand VARCHAR(100);
ALTER TABLE products ADD COLUMN sku VARCHAR(50);

-- Create product categories table
CREATE TABLE product_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    icon_url VARCHAR(500),
    color_hex VARCHAR(7), -- e.g., #FF6B6B
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Add category reference to products
ALTER TABLE products ADD COLUMN category_id BIGINT;
ALTER TABLE products ADD CONSTRAINT fk_product_category 
    FOREIGN KEY (category_id) REFERENCES product_categories(id);

-- Create reviews table
CREATE TABLE product_reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create wishlist table
CREATE TABLE user_wishlist (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    UNIQUE KEY unique_user_product (user_id, product_id)
);