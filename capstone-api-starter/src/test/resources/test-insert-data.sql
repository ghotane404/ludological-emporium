/* INSERT Categories */
INSERT INTO categories (category_id, name, description)
VALUES
    (1, 'Console Games', 'Games for PlayStation, Xbox, and Nintendo.'),
    (2, 'PC Games', 'PC gaming titles.'),
    (3, 'Gaming Accessories', 'Gaming gear and accessories.');

/* INSERT Products */
INSERT INTO products (product_id, name, price, category_id, description, image_url, stock, featured, subcategory)
VALUES
    (1, 'The Legend of Zelda: Breath of the Wild', 59.99, 1, 'Open-world adventure game.', 'zelda-botw.jpg', 50, 1, 'Adventure'),
    (2, 'God of War', 49.99, 1, 'Action-adventure game.', 'god-of-war.jpg', 40, 1, 'Action'),
    (3, 'Halo Infinite', 59.99, 1, 'Sci-fi shooter.', 'halo-infinite.jpg', 35, 0, 'Shooter'),
    (4, 'Cyberpunk 2077', 29.99, 2, 'Open-world RPG.', 'cyberpunk-2077.jpg', 100, 0, 'RPG'),
    (5, 'Baldur''s Gate 3', 59.99, 2, 'Turn-based RPG.', 'baldurs-gate-3.jpg', 75, 1, 'RPG'),
    (6, 'Counter-Strike 2', 0.00, 2, 'Competitive tactical shooter.', 'counter-strike-2.jpg', 999, 1, 'Shooter'),
    (7, 'Xbox Wireless Controller', 59.99, 3, 'Xbox controller.', 'xbox-controller.jpg', 50, 1, 'Controller'),
    (8, 'Gaming Mousepad XL', 19.99, 3, 'Large gaming mousepad.', 'xl-mousepad.jpg', 100, 0, 'Peripheral');