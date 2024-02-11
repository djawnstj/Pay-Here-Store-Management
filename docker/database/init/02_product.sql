DROP TABLE IF EXISTS `product_category`;
CREATE TABLE `product_category`
(
    `id`            bigint      NOT NULL AUTO_INCREMENT,
    `category_name` varchar(20) NOT NULL,
    `created_at`    datetime    NOT NULL,
    `modified_at`   datetime    NOT NULL,
    `deleted_at`    datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `category_name_unique` (`category_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`
(
    `id`              bigint        NOT NULL AUTO_INCREMENT,
    `category_id`     bigint        NOT NULL,
    `selling_price`   decimal(8, 2) NOT NULL,
    `cost_price`      decimal(8, 2) NOT NULL,
    `product_name`    varchar(20)   NOT NULL,
    `size`            varchar(5)    NOT NULL,
    `description`     text,
    `barcode`         varchar(255)  NOT NULL,
    `expiration_date` date          NOT NULL,
    `created_at`      datetime      NOT NULL,
    `modified_at`     datetime      NOT NULL,
    `deleted_at`      datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`category_id`) REFERENCES `product_category` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;