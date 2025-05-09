USE
GameDB;

INSERT INTO Player (username, player_level, player_rank, blue_essence)
VALUES ('Bob', 15, 2, 10000),
       ('John', 20, 1, 8000),
       ('Joe', 15, 5, 10000),
       ('Jill', 20, 2, 8000),
       ('Doe', 15, 3, 10000),
       ('Alice', 20, 0, 8000),
       ('Trudy', 15, 0, 10000),
       ('Sam', 220, 6, 8000),
       ('Ike', 15, 5, 10000),
       ('xxx', 2, 2, 8000),
       ('yyy', 15, 8, 10000),
       ('Summoner01', 4, 4, 5000),
       ('Summoner02', 3, 55, 4200),
       ('Summoner03', 6, 2, 3000),
       ('Summoner04', 5, 4, 10000),
       ('Summoner05', 20, 1, 8000),
       ('zzz', 20, 32, 8000);

INSERT INTO Champion (champion_name, price)
VALUES ('Ahri', 4800),
       ('Garen', 450),
       ('Yasuo', 6300),
       ('Lux', 3150),
       ('Zed', 6300);

INSERT INTO MatchHistory (match_date, game_mode, game_length)
VALUES ('2025-05-01 14:30:00', 'Ranked Solo', 1000),
       ('2025-05-02 16:00:00', 'Normal Draft', 200),
       ('2025-05-03 18:15:00', 'ARAM', 400),
       ('2025-05-04 20:45:00', 'Ranked Flex', 2000),
       ('2025-05-05 13:00:00', 'Clash', 5000);

INSERT INTO MatchParticipant (match_id, player_id, rank_awarded, win_loss, kills, deaths, assists)
VALUES (1, 1, 105, 'Win', 10, 2, 8),
       (1, 2, 110, 'Loss', 5, 5, 6),
       (2, 3, 95, 'Win', 12, 1, 3),
       (2, 4, 200, 'Loss', 4, 6, 5),
       (3, 5, 260, 'Win', 9, 3, 7),
       (3, 1, 106, 'Loss', 3, 4, 2),
       (4, 2, 111, 'Win', 11, 2, 8),
       (4, 3, 96, 'Loss', 2, 7, 4),
       (5, 4, 202, 'Win', 13, 3, 9),
       (5, 5, 262, 'Loss', 5, 5, 5);

INSERT INTO ChampionsOwned (player_id, champion_name)
VALUES (1, 'Ahri'),
       (1, 'Garen'),
       (2, 'Yasuo'),
       (2, 'Lux'),
       (3, 'Zed'),
       (3, 'Ahri'),
       (4, 'Garen'),
       (4, 'Yasuo'),
       (5, 'Lux'),
       (5, 'Zed');
