--liquibase formatted sql

--changeset bmv:1

-- -----------------------------------------------------
-- Schema acc_pimp
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema acc_pimp
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `product_review` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `product_review` ;

-- -----------------------------------------------------
-- Table `review`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `review` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `product_id` varchar(255) NOT NULL,
  `score` INT NOT NULL,
  `user_email` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_product_user` (`product_id`,`user_email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--changeset bmv:2
-- -----------------------------------------------------
-- prepopulate review table for products M20324, AC7836, C77154
-- -----------------------------------------------------
INSERT INTO review(product_id, score, user_email) values ('M20324', 5, 'user0@test.ee');
INSERT INTO review(product_id, score, user_email) values ('M20324', 3, 'user1@test.ee');
INSERT INTO review(product_id, score, user_email) values ('M20324', 7, 'user2@test.ee');
INSERT INTO review(product_id, score, user_email) values ('AC7836', 1, 'user3@test.ee');
INSERT INTO review(product_id, score, user_email) values ('AC7836', 2, 'user4@test.ee');
INSERT INTO review(product_id, score, user_email) values ('C77154', 8, 'user5@test.ee');
