CREATE DATABASE  IF NOT EXISTS `paymybuddy`;
USE `paymybuddy`;
-- Database: paymybuddy
-- ------------------------------------------------------
-- Server version	8.0.29
-- Table structure for table `users`
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `balance` double DEFAULT NULL,
                         `email` varchar(100) DEFAULT NULL,
                         `first_name` varchar(45) DEFAULT NULL,
                         `last_name` varchar(45) DEFAULT NULL,
                         `password` varchar(255) DEFAULT NULL,
                         `auth_provider` varchar(15) DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table structure for table `connections`
DROP TABLE IF EXISTS `connections`;
CREATE TABLE `connections` (
                               `user_id` int NOT NULL,
                               `target_id` int NOT NULL,
                               PRIMARY KEY (`user_id`,`target_id`),
                               KEY `FKmpw1c0n07nv4uqhr0gjvsij0y` (`target_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table structure for table `roles`
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `role_name` varchar(25) DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `UK_716hgxp60ym1lifrdgp67xt5k` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- Table structure for table `user_roles`
DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles` (
                              `user_id` int NOT NULL,
                              `role_id` int NOT NULL,
                              KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`),
                              KEY `FKhfh9dx7w3ubf1co1vdev94g3f` (`user_id`),
                              CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
                              CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table structure for table `transaction`
DROP TABLE IF EXISTS `transaction`;
CREATE TABLE `transaction` (
                               `transaction_id` int NOT NULL AUTO_INCREMENT,
                               `amount` double DEFAULT NULL,
                               `transaction_date` date DEFAULT NULL,
                               `description` varchar(255) DEFAULT NULL,
                               `total_fee_payed` double DEFAULT NULL,
                               `source` int DEFAULT NULL,
                               `target` int DEFAULT NULL,
                               PRIMARY KEY (`transaction_id`),
                               KEY `FK6m2jg6y4wlyan5wrbkin017sd` (`source`),
                               KEY `FKgv6l9h6hyqmvweebscyp7hlii` (`target`),
                               CONSTRAINT `FK6m2jg6y4wlyan5wrbkin017sd` FOREIGN KEY (`source`) REFERENCES `users` (`id`),
                               CONSTRAINT `FKgv6l9h6hyqmvweebscyp7hlii` FOREIGN KEY (`target`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table structure for table `user_bank_account`
DROP TABLE IF EXISTS `user_bank_account`;
CREATE TABLE `user_bank_account` (
                                     `bank_account_id` int NOT NULL AUTO_INCREMENT,
                                     `bank_location` varchar(45) DEFAULT NULL,
                                     `bank_name` varchar(25) DEFAULT NULL,
                                     `code_bic` varchar(45) DEFAULT NULL,
                                     `code_iban` varchar(100) DEFAULT NULL,
                                     `app_user_id` int DEFAULT NULL,
                                     PRIMARY KEY (`bank_account_id`),
                                     UNIQUE KEY `UK_1dc66y9h6hq0dpjk1j3clna99` (`code_iban`),
                                     KEY `FKq7act822x0mkd043uvnlc6r5f` (`app_user_id`),
                                     CONSTRAINT `FKq7act822x0mkd043uvnlc6r5f` FOREIGN KEY (`app_user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- Table structure for table `transfer`
DROP TABLE IF EXISTS `transfer`;
CREATE TABLE `transfer` (
                            `transfer_id` int NOT NULL AUTO_INCREMENT,
                            `amount` double DEFAULT NULL,
                            `description` varchar(255) DEFAULT NULL,
                            `operation_type` varchar(15) DEFAULT NULL,
                            `transaction_date` date DEFAULT NULL,
                            `bank_account_id` int NOT NULL,
                            PRIMARY KEY (`transfer_id`),
                            KEY `FKfrdtgs1i2v2bx3djfhqk5p244` (`bank_account_id`),
                            CONSTRAINT `FKfrdtgs1i2v2bx3djfhqk5p244` FOREIGN KEY (`bank_account_id`) REFERENCES `user_bank_account` (`bank_account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;






