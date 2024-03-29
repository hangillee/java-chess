USE chess;

DROP TABLE IF EXISTS `position`;
DROP TABLE IF EXISTS `piece`;
DROP TABLE IF EXISTS `square`;

CREATE TABLE `position`
(
    `id`         INT NOT NULL AUTO_INCREMENT,
    `file_index` INT NOT NULL,
    `rank_index` INT NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `piece`
(
    `id`    INT          NOT NULL AUTO_INCREMENT,
    `color` VARCHAR(255) NOT NULL,
    `type`  VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `square`
(
    `id`          INT NOT NULL AUTO_INCREMENT,
    `position_id` INT NOT NULL,
    `piece_id`    INT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`position_id`) REFERENCES `position` (`id`),
    FOREIGN KEY (`piece_id`) REFERENCES `piece` (`id`)
);

CREATE TABLE `game`
(
    `id`         INT          NOT NULL AUTO_INCREMENT,
    `turn`       VARCHAR(255) NOT NULL,
    `is_started` BOOLEAN      NOT NULL,
    `is_over`    BOOLEAN      NOT NULL,
    PRIMARY KEY (`id`)
);
