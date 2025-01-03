-- Insert sample cities
INSERT INTO City (city_id, name) VALUES
(1, 'New York'),
(2, 'Los Angeles'),
(3, 'Chicago');

-- Insert sample users with the correct foreign key for city_id
INSERT INTO User (user_id, name, email, birth_year, gender, city_id) VALUES
(1, 'Alice', 'alice@example.com', 1990, 'F', 1), -- New York (city_id = 1)
(2, 'Bob', 'bob@example.com', 1985, 'M', 2),    -- Los Angeles (city_id = 2)
(3, 'Charlie', 'charlie@example.com', 2000, 'M', 3);  -- Chicago (city_id = 3)


-- Insert sample audio tracks
INSERT INTO AudioTrack (audio_id, name, duration, owner_id, upload_time) VALUES
(1, 'Relaxing Sounds', 300, 1, '2025-01-01 12:00:00'),
(2, 'Workout Mix', 600, 2, '2025-01-02 14:30:00'),
(3, 'Podcast Episode 1', 1200, 3, '2025-01-03 09:15:00');

-- Insert sample categories
INSERT INTO Category (category_id, name) VALUES
(1, 'Music'),
(2, 'Podcast'),
(3, 'Relaxation');

-- Map audio tracks to categories
INSERT INTO AudioCategory (audio_id, category_id) VALUES
(1, 3),
(2, 1),
(3, 2);

-- Insert sample packages
INSERT INTO Package (package_id, name, monthly_price) VALUES
(1, 'Basic', 9.99),
(2, 'Premium', 19.99),
(3, 'Family', 29.99);

-- Insert sample subscriptions
INSERT INTO Subscription (subscription_id, user_id, package_id, start_date, price_paid) VALUES
(1, 1, 2, '2025-01-01 10:00:00', 19.99),
(2, 2, 1, '2025-01-01 11:00:00', 9.99),
(3, 3, 3, '2025-01-02 08:00:00', 29.99);

-- Insert sample listening history
INSERT INTO Listening (listening_id, user_id, audio_id, start_time, start_second, duration) VALUES
(1, 1, 1, '2025-01-01 12:30:00', 0, 150),
(2, 2, 2, '2025-01-02 15:00:00', 0, 300),
(3, 3, 3, '2025-01-03 09:30:00', 0, 600);

-- Insert sample ratings
INSERT INTO Rating (rating_id, user_id, audio_id, rating, rating_time) VALUES
(1, 1, 1, 5, '2025-01-01 13:00:00'),
(2, 2, 2, 4, '2025-01-02 16:00:00'),
(3, 3, 3, 5, '2025-01-03 10:00:00');

-- Insert sample favorites
INSERT INTO Favorites (favorite_id, user_id, audio_id) VALUES
(1, 1, 1),
(2, 2, 2),
(3, 3, 3);
