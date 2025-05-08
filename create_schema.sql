CREATE DATABASE IF NOT EXISTS GameDB;
USE GameDB;

CREATE TABLE IF NOT EXISTS Player (
    player_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    player_level INT NOT NULL DEFAULT 0,
    player_rank INT NOT NULL DEFAULT 0,
    blue_essence INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS Champion (
    champion_name VARCHAR(50) PRIMARY KEY,
    price INT NOT NULL
);

CREATE TABLE IF NOT EXISTS MatchHistory (
    match_id INT PRIMARY KEY AUTO_INCREMENT,
    match_date DATETIME NOT NULL,
    game_mode VARCHAR(50) NOT NULL,
    game_length DECIMAL(5, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS MatchParticipant (
    match_id INT,
    player_id INT,
    rank_awarded INT NOT NULL,
    win_loss ENUM('Win', 'Loss') NOT NULL,
    kills INT NOT NULL,
    deaths INT NOT NULL,
    assists INT NOT NULL,
    PRIMARY KEY (match_id, player_id),
    FOREIGN KEY (match_id) REFERENCES MatchHistory(match_id),
    FOREIGN KEY (player_id) REFERENCES Player(player_id)
);

CREATE TABLE IF NOT EXISTS ChampionsOwned (
    player_id INT,
    champion_name VARCHAR(50),
    PRIMARY KEY (player_id, champion_name),
    FOREIGN KEY (player_id) REFERENCES Player(player_id),
    FOREIGN KEY (champion_name) REFERENCES Champion(champion_name)
);

CREATE USER IF NOT EXISTS 'user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON GameDB.* TO 'user'@'localhost';
FLUSH PRIVILEGES;
