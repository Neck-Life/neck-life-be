CREATE
    USER 'necklife-local'@'localhost' IDENTIFIED BY 'necklife-local';
CREATE
    USER 'necklife-local'@'%' IDENTIFIED BY 'necklife-local';

GRANT ALL PRIVILEGES ON *.* TO
    'necklife-local'@'localhost';
GRANT ALL PRIVILEGES ON *.* TO
    'necklife-local'@'%';

CREATE
    DATABASE api DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
