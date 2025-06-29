-- Insert realistic product categories with beautiful colors and icons
INSERT INTO product_categories (name, description, color_hex, icon_url) VALUES
('Electronics', 'Smartphones, laptops, gadgets and electronic accessories', '#FF6B6B', 'https://images.unsplash.com/photo-1531482615713-2afd69097998?w=100'),
('Fashion', 'Clothing, shoes, accessories and fashion items', '#4ECDC4', 'https://images.unsplash.com/photo-1445205170230-053b83016050?w=100'),
('Home & Garden', 'Furniture, decor, gardening and home improvement', '#45B7D1', 'https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=100'),
('Sports & Outdoors', 'Sports equipment, outdoor gear and fitness accessories', '#96CEB4', 'https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=100'),
('Beauty & Health', 'Cosmetics, skincare, health and wellness products', '#FFEAA7', 'https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=100'),
('Books & Media', 'Books, movies, music and educational content', '#DDA0DD', 'https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=100'),
('Toys & Games', 'Toys, board games, video games and entertainment', '#98D8C8', 'https://images.unsplash.com/photo-1558060370-d7248509c7dd?w=100'),
('Automotive', 'Car accessories, tools and automotive equipment', '#F7DC6F', 'https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=100');

-- Insert realistic users with profile images
INSERT INTO users (username, email, password, first_name, last_name, profile_image_url, bio, location, created_at) VALUES
('sarah_johnson', 'sarah.johnson@email.com', 'hashed_password_1', 'Sarah', 'Johnson', 'https://images.unsplash.com/photo-1494790108755-2616b612b47c?w=150', 'Fashion enthusiast and lifestyle blogger. Love discovering new trends!', 'New York, NY', '2024-01-15 10:30:00'),
('mike_chen', 'mike.chen@techmail.com', 'hashed_password_2', 'Mike', 'Chen', 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150', 'Tech geek and gadget reviewer. Always looking for the latest innovations.', 'San Francisco, CA', '2024-01-20 14:45:00'),
('emma_williams', 'emma.w@homedesign.co', 'hashed_password_3', 'Emma', 'Williams', 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150', 'Interior designer with a passion for cozy homes and sustainable living.', 'Austin, TX', '2024-02-01 09:15:00'),
('david_rodriguez', 'david.rodriguez@fitness.net', 'hashed_password_4', 'David', 'Rodriguez', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150', 'Fitness trainer and outdoor adventure enthusiast. Living life to the fullest!', 'Denver, CO', '2024-02-10 16:20:00'),
('lisa_kim', 'lisa.kim@beauty.pro', 'hashed_password_5', 'Lisa', 'Kim', 'https://images.unsplash.com/photo-1489424731084-a5d8b219a5bb?w=150', 'Beauty influencer and skincare expert. Helping people feel confident and beautiful.', 'Los Angeles, CA', '2024-02-15 11:30:00'),
('alex_thompson', 'alex.thompson@bookworm.io', 'hashed_password_6', 'Alex', 'Thompson', 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150', 'Avid reader and literature professor. Books are my passion and escape.', 'Boston, MA', '2024-02-20 13:45:00'),
('maria_gonzalez', 'maria.gonzalez@family.com', 'hashed_password_7', 'Maria', 'Gonzalez', 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150', 'Mother of three and toy safety advocate. Making childhood magical and safe.', 'Miami, FL', '2024-02-25 08:00:00'),
('james_carter', 'james.carter@autoexpert.net', 'hashed_password_8', 'James', 'Carter', 'https://images.unsplash.com/photo-1560250097-0b93528c311a?w=150', 'Automotive engineer and car enthusiast. Restoring classic cars in my spare time.', 'Detroit, MI', '2024-03-01 17:30:00');

-- Insert beautiful stores with banner and logo images
INSERT INTO stores (name, description, owner_id, banner_image_url, logo_image_url, established_year, total_products, total_sales, created_at) VALUES
('TechHub Pro', 'Premium electronics and cutting-edge technology for professionals and enthusiasts', 2, 'https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=800', 'https://images.unsplash.com/photo-1531482615713-2afd69097998?w=200', 2018, 156, 2847, '2024-01-15 10:00:00'),
('Fashion Forward', 'Trendy clothing and accessories for the modern lifestyle', 1, 'https://images.unsplash.com/photo-1441984904996-e0b6ba687e04?w=800', 'https://images.unsplash.com/photo-1445205170230-053b83016050?w=200', 2019, 342, 1923, '2024-01-20 11:00:00'),
('Cozy Home Studio', 'Beautiful home decor and furniture for every space', 3, 'https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=800', 'https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=200', 2020, 198, 1456, '2024-02-01 12:00:00'),
('Outdoor Adventures Co', 'Premium outdoor gear and sports equipment for adventurers', 4, 'https://images.unsplash.com/photo-1544966503-7cc4ac882d24?w=800', 'https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=200', 2017, 287, 3102, '2024-02-10 13:00:00'),
('Glow Beauty', 'Luxury skincare and cosmetics for radiant beauty', 5, 'https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=800', 'https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=200', 2021, 134, 892, '2024-02-15 14:00:00'),
('BookVerse', 'Curated collection of books and literary treasures', 6, 'https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=800', 'https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=200', 2016, 523, 2145, '2024-02-20 15:00:00'),
('PlayTime Paradise', 'Educational toys and games for creative kids', 7, 'https://images.unsplash.com/photo-1558060370-d7248509c7dd?w=800', 'https://images.unsplash.com/photo-1558060370-d7248509c7dd?w=200', 2022, 167, 743, '2024-02-25 16:00:00'),
('AutoCraft', 'Quality automotive parts and accessories', 8, 'https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=800', 'https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=200', 2015, 298, 1834, '2024-03-01 17:00:00');

-- Insert realistic products with beautiful images
-- Electronics
INSERT INTO products (name, description, price, store_id, category_id, image_url, gallery_images, discount_percentage, review_count, is_featured, brand, sku, rating, created_at) VALUES
('iPhone 15 Pro Max', 'Latest Apple smartphone with titanium design and advanced camera system', 1199.99, 1, 1, 'https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=400', '["https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=400", "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=400"]', 5.0, 234, true, 'Apple', 'IPH15PM-256', 4.8, '2024-01-15 10:30:00'),
('Samsung Galaxy S24 Ultra', 'Premium Android smartphone with S Pen and 200MP camera', 1299.99, 1, 1, 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=400', '["https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=400"]', 10.0, 189, true, 'Samsung', 'SGS24U-512', 4.7, '2024-01-16 11:00:00'),
('MacBook Pro 16-inch', 'Powerful laptop with M3 Pro chip for creative professionals', 2499.99, 1, 1, 'https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=400', '["https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=400"]', 0.0, 156, true, 'Apple', 'MBP16-M3P', 4.9, '2024-01-17 12:00:00'),
('Sony WH-1000XM5', 'Industry-leading noise canceling wireless headphones', 399.99, 1, 1, 'https://images.unsplash.com/photo-1484704849700-f032a568e944?w=400', '["https://images.unsplash.com/photo-1484704849700-f032a568e944?w=400"]', 15.0, 432, false, 'Sony', 'WH1000XM5', 4.6, '2024-01-18 13:00:00'),

-- Fashion
('Nike Air Max 270', 'Comfortable running shoes with Max Air technology', 149.99, 2, 2, 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400', '["https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400"]', 20.0, 345, true, 'Nike', 'AM270-BLK', 4.5, '2024-01-20 14:00:00'),
('Levi''s 501 Original Jeans', 'Classic straight-fit jeans in vintage wash', 89.99, 2, 2, 'https://images.unsplash.com/photo-1542272604-787c3835535d?w=400', '["https://images.unsplash.com/photo-1542272604-787c3835535d?w=400"]', 0.0, 567, false, 'Levi''s', 'L501-VNT', 4.4, '2024-01-21 15:00:00'),
('Patagonia Down Jacket', 'Lightweight and warm down-filled jacket for outdoor adventures', 249.99, 2, 2, 'https://images.unsplash.com/photo-1544966503-7cc4ac882d24?w=400', '["https://images.unsplash.com/photo-1544966503-7cc4ac882d24?w=400"]', 10.0, 123, true, 'Patagonia', 'PDJ-BLU', 4.7, '2024-01-22 16:00:00'),

-- Home & Garden
('IKEA HEMNES Bed Frame', 'Solid wood bed frame with timeless design', 299.99, 3, 3, 'https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=400', '["https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=400"]', 0.0, 234, false, 'IKEA', 'HMN-QN', 4.3, '2024-02-01 17:00:00'),
('Philips Hue Smart Bulbs', 'Color-changing smart LED bulbs (4-pack)', 199.99, 3, 3, 'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400', '["https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400"]', 25.0, 456, true, 'Philips', 'HUE-4PK', 4.6, '2024-02-02 18:00:00'),

-- Sports & Outdoors
('Yeti Rambler Tumbler', 'Stainless steel insulated tumbler for hot and cold drinks', 39.99, 4, 4, 'https://images.unsplash.com/photo-1544966503-7cc4ac882d24?w=400', '["https://images.unsplash.com/photo-1544966503-7cc4ac882d24?w=400"]', 0.0, 678, false, 'Yeti', 'YRT-30OZ', 4.8, '2024-02-10 19:00:00'),
('REI Co-op Hiking Boots', 'Waterproof hiking boots for all-terrain adventures', 179.99, 4, 4, 'https://images.unsplash.com/photo-1544966503-7cc4ac882d24?w=400', '["https://images.unsplash.com/photo-1544966503-7cc4ac882d24?w=400"]', 15.0, 234, true, 'REI Co-op', 'HB-WP', 4.5, '2024-02-11 20:00:00'),

-- Beauty & Health
('The Ordinary Niacinamide', 'Niacinamide 10% + Zinc 1% serum for blemish-prone skin', 24.99, 5, 5, 'https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=400', '["https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=400"]', 0.0, 892, true, 'The Ordinary', 'TO-NIAC', 4.4, '2024-02-15 21:00:00'),
('Fenty Beauty Gloss Bomb', 'Universal lip luminizer with explosive shine', 19.99, 5, 5, 'https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=400', '["https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=400"]', 10.0, 345, false, 'Fenty Beauty', 'FB-GB', 4.6, '2024-02-16 22:00:00'),

-- Books & Media
('Atomic Habits by James Clear', 'Life-changing guide to building good habits and breaking bad ones', 16.99, 6, 6, 'https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=400', '["https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=400"]', 20.0, 1234, true, 'Avery', 'AH-JC', 4.9, '2024-02-20 23:00:00'),
('The Seven Moons of Maali Almeida', 'Award-winning magical realism novel', 14.99, 6, 6, 'https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=400', '["https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=400"]', 0.0, 267, false, 'Sort Of Books', 'TSMA-HB', 4.3, '2024-02-21 00:00:00'),

-- Toys & Games
('LEGO Creator Expert Taj Mahal', 'Detailed architectural LEGO set with 5923 pieces', 369.99, 7, 7, 'https://images.unsplash.com/photo-1558060370-d7248509c7dd?w=400', '["https://images.unsplash.com/photo-1558060370-d7248509c7dd?w=400"]', 5.0, 189, true, 'LEGO', 'LG-TM', 4.8, '2024-02-25 01:00:00'),
('Nintendo Switch OLED', 'Handheld gaming console with vibrant OLED screen', 349.99, 7, 7, 'https://images.unsplash.com/photo-1606144042614-b2417e99c4e3?w=400', '["https://images.unsplash.com/photo-1606144042614-b2417e99c4e3?w=400"]', 0.0, 567, true, 'Nintendo', 'NSW-OLED', 4.7, '2024-02-26 02:00:00'),

-- Automotive
('Michelin Pilot Sport Tires', 'High-performance tires for sports cars (set of 4)', 899.99, 8, 8, 'https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=400', '["https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=400"]', 10.0, 123, false, 'Michelin', 'MPS-SET4', 4.6, '2024-03-01 03:00:00'),
('Garmin DashCam 67W', 'Ultra-wide field of view dash camera with GPS', 199.99, 8, 8, 'https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=400', '["https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=400"]', 15.0, 234, true, 'Garmin', 'GDC-67W', 4.5, '2024-03-02 04:00:00');