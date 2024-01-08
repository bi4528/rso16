INSERT INTO seznam_metadata (id, cocktail_id, name, user_name) VALUES (1, 11002, 'Long Island Tea', 'Maja');
INSERT INTO seznam_metadata (id, cocktail_id, name, user_name) VALUES (2, 11102, 'Black Russian', 'Tine');
SELECT setval('mySequence', (SELECT MAX(id) FROM seznam_metadata));

