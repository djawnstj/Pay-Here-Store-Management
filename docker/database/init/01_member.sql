DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`
(
    `id`             bigint      NOT NULL AUTO_INCREMENT,
    `phone_number`   varchar(13) DEFAULT NULL,
    `login_password` varchar(60) NOT NULL,
    `name`           varchar(10) NOT NULL,
    `role`           varchar(20) NOT NULL,
    `created_at`     datetime    NOT NULL,
    `modified_at`    datetime    NOT NULL,
    `deleted_at`     datetime    DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `phone_number_unique` (`phone_number`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;