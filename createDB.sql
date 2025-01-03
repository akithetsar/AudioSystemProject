CREATE TABLE City (
    city_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE User (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    birth_year INT NOT NULL,
    gender ENUM('Male', 'Female', 'Other'),
    city_id INT,
    FOREIGN KEY (city_id) REFERENCES City(city_id)
);

CREATE TABLE AudioTrack (
    audio_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    duration INT NOT NULL,
    owner_id INT,
    upload_time DATETIME NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES User(user_id)
);

CREATE TABLE Category (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE AudioCategory (
    audio_id INT,
    category_id INT,
    PRIMARY KEY (audio_id, category_id),
    FOREIGN KEY (audio_id) REFERENCES AudioTrack(audio_id),
    FOREIGN KEY (category_id) REFERENCES Category(category_id)
);

CREATE TABLE Package (
    package_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    monthly_price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE Subscription (
    subscription_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    package_id INT,
    start_date DATETIME NOT NULL,
    price_paid DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (package_id) REFERENCES Package(package_id),
    CONSTRAINT chk_active_subscription CHECK (
        NOT EXISTS (
            SELECT 1 FROM Subscription
            WHERE user_id = NEW.user_id AND start_date <= NOW() AND end_date >= NOW()
        )
    )
);

CREATE TABLE Listening (
    listening_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    audio_id INT,
    start_time DATETIME NOT NULL,
    start_second INT NOT NULL,
    duration INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (audio_id) REFERENCES AudioTrack(audio_id)
);
CREATE TABLE Rating (
    rating_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    audio_id INT,
    rating ENUM('1', '2', '3', '4', '5'),
    rating_time DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (audio_id) REFERENCES AudioTrack(audio_id)
);


CREATE TABLE Favorites (
    favorite_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    audio_id INT,
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (audio_id) REFERENCES AudioTrack(audio_id),
    UNIQUE(user_id, audio_id)
);
