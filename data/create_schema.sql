CREATE DATABASE IF NOT EXISTS GameDB;
USE GameDB;

CREATE TABLE Player (
    player_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    player_level INT NOT NULL,
    player_rank INT NOT NULL,
    blue_essence INT NOT NULL DEFAULT 0
);

CREATE TABLE Champion (
    champion_name VARCHAR(50) PRIMARY KEY,
    price INT NOT NULL
);

CREATE TABLE MatchHistory (
    match_id INT PRIMARY KEY AUTO_INCREMENT,
    match_date DATETIME NOT NULL,
    game_mode VARCHAR(50) NOT NULL,
    game_length DECIMAL(5, 2) NOT NULL,
    win_loss ENUM('Win', 'Loss') NOT NULL,
    kills INT NOT NULL,
    deaths INT NOT NULL,
    assists INT NOT NULL
);

CREATE TABLE MatchParticipant (
    match_id INT,
    player_id INT,
    rank_awarded INT NOT NULL,
    PRIMARY KEY (match_id, player_id),
    FOREIGN KEY (match_id) REFERENCES MatchHistory(match_id),
    FOREIGN KEY (player_id) REFERENCES Player(player_id)
);

CREATE TABLE ChampionsOwned (
    player_id INT,
    champion_name VARCHAR(50),
    PRIMARY KEY (player_id, champion_name),
    FOREIGN KEY (player_id) REFERENCES Player(player_id),
    FOREIGN KEY (champion_name) REFERENCES Champion(champion_name)
);
