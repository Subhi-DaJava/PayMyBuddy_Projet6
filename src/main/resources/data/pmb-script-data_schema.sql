CREATE DATABASE  IF NOT EXISTS `paymybuddy`;
USE `paymybuddy`;

-- Host: localhost    Database: paymybuddy
-- ------------------------------------------------------
-- Server version	8.0.29
-- Table structure for table `connections`

DROP TABLE IF EXISTS `connections`;

CREATE TABLE `connections` (
  `user_id` int NOT NULL,
  `target_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`target_id`),
  KEY `FKmpw1c0n07nv4uqhr0gjvsij0y` (`target_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `connections` WRITE;
INSERT INTO `connections` VALUES (1,6),(1,7),(1,8),(1,20),(1,21),(7,8),(7,11),(11,7),(11,8),(15,8),(19,7),(21,11),(21,16),(23,1),(23,3),(35,3);
UNLOCK TABLES;

-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_716hgxp60ym1lifrdgp67xt5k` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (2,'ADMIN'),(1,'USER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction`
--

DROP TABLE IF EXISTS `transaction`;
CREATE TABLE `transaction` (
  `transaction_id` int NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `transaction` WRITE;
INSERT INTO `transaction` VALUES (1,4,'2022-06-28','descriptionDDD',0.02,1,2),(2,4,'2022-06-28','descriptionDDD',0.02,1,3),(3,55,'2022-06-28','descriptionDDD',0.275,1,3),(4,50,'2022-06-28','descriptionDDD',0.25,1,2),(5,8,'2022-06-28','descriptionDDD',0.04,1,2),(6,55,'2022-06-28','descriptionDDD',0.275,1,2),(7,1,'2022-06-28','descriptionDDD ddfsdfs',0.005,1,2),(8,5,'2022-06-29','descriptionDDD',0.025,1,2),(9,5,'2022-06-29','nouveau',0.025,1,2),(10,500,'2022-06-29','Just a test',2.5,1,7),(11,50,'2022-06-29','Just a test',0.25,7,3),(12,200,'2022-06-29','Just a test 2',1,1,8),(13,50,'2022-06-29','Just a test',0.25,1,6),(14,100,'2022-06-29','testRememberMe',0.5,1,6),(15,4,'2022-06-29','test',0.02,7,3),(16,4,'2022-06-29','test2',0.02,7,1),(17,5,'2022-06-29','test3',0.025,7,1),(18,15,'2022-06-30','TestGoogle ',0.075,7,11),(19,50,'2022-06-30','test2',0.25,7,11),(20,2,'2022-06-30','test',0.01,11,8),(21,4,'2022-06-30','test',0.02,7,11),(22,2,'2022-06-30','test30',0.01,11,7),(23,4,'2022-06-30','test30',0.02,15,8),(24,22,'2022-06-30','test',0.11,15,8),(25,5,'2022-06-30','test',0.025,15,8),(26,36,'2022-06-30','test',0.18,15,8),(27,100,'2022-06-30','testNouveauForma',0.5,1,8),(28,50,'2022-06-30','Just a test',0.25,1,2),(29,15,'2022-07-01','Just a test',0.075,21,16),(30,18,'2022-07-01','Just a test',0.09,21,16),(31,17,'2022-07-01','Just a test',0.085,21,16),(32,300,'2022-07-01','Premier Payement vers Un google Compte',1.5,21,11),(33,300,'2022-07-01','Premier Payement vers Un google Compte',1.5,21,11),(34,200,'2022-07-01','test',1,21,11),(35,300,'2022-07-01','Just a test',1.5,21,11),(36,50,'2022-07-02','Just a test',0.25,23,3),(37,17,'2022-07-02','Test',0.085,23,1),(38,5,'2022-07-02','Just a test',0.025,35,3),(39,50,'2022-07-03','test2',0.25,7,8);
UNLOCK TABLES;


DROP TABLE IF EXISTS `transfer`;

CREATE TABLE `transfer` (
  `transfer_id` int NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `operation_type` varchar(255) DEFAULT NULL,
  `transaction_date` date DEFAULT NULL,
  `bank_account_id` int NOT NULL,
  PRIMARY KEY (`transfer_id`),
  KEY `FKfrdtgs1i2v2bx3djfhqk5p244` (`bank_account_id`),
  CONSTRAINT `FKfrdtgs1i2v2bx3djfhqk5p244` FOREIGN KEY (`bank_account_id`) REFERENCES `user_bank_account` (`bank_account_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `transfer`
--

LOCK TABLES `transfer` WRITE;
INSERT INTO `transfer` VALUES (1,1000,'Premier Virement vers PayMyBuddy','DEBIT','2022-06-29',1),(2,500,'virement ','DEBIT','2022-06-30',2),(3,600,'test','DEBIT','2022-06-30',2),(4,200,'test','DEBIT','2022-06-30',4),(6,5000,'Premier Virement vers PayMyBuddy','DEBIT','2022-07-01',7),(7,3000,'Just a test','CREDIT','2022-07-01',7),(8,2100,'Just a test','CREDIT','2022-07-01',7),(9,200,'Premier virement vers Bank','DEBIT','2022-07-01',7),(10,3000,'Just a test','CREDIT','2022-07-02',8),(11,300,'Test For MyTransfers','CREDIT','2022-07-02',1),(12,3000,'Just a test','CREDIT','2022-07-02',9),(13,30000,'descriptionDDD','DEBIT','2022-07-02',9),(14,3000,'Just a test','CREDIT','2022-07-02',16),(15,50,'Just a test','DEBIT','2022-07-02',16),(16,200,'test2','CREDIT','2022-07-03',19),(17,500,'test2','CREDIT','2022-07-03',22),(18,200,'test','DEBIT','2022-07-03',22);
UNLOCK TABLES;

--
-- Table structure for table `user_bank_account`
--

DROP TABLE IF EXISTS `user_bank_account`;

CREATE TABLE `user_bank_account` (
  `bank_account_id` int NOT NULL AUTO_INCREMENT,
  `bank_location` varchar(255) DEFAULT NULL,
  `bank_name` varchar(255) DEFAULT NULL,
  `code_bic` varchar(100) DEFAULT NULL,
  `code_iban` varchar(100) DEFAULT NULL,
  `app_user_id` int DEFAULT NULL,
  PRIMARY KEY (`bank_account_id`),
  UNIQUE KEY `UK_pewlys9fmlk1ynw13we7ekpdy` (`code_bic`),
  UNIQUE KEY `UK_1dc66y9h6hq0dpjk1j3clna99` (`code_iban`),
  KEY `FKq7act822x0mkd043uvnlc6r5f` (`app_user_id`),
  CONSTRAINT `FKq7act822x0mkd043uvnlc6r5f` FOREIGN KEY (`app_user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


--
-- Dumping data for table `user_bank_account`
--

LOCK TABLES `user_bank_account` WRITE;
INSERT INTO `user_bank_account` VALUES (1,'Paris 75015','LCL','FR75','FR76',1),(2,'Lyon','CIC','FR70','FR50',2),(3,'Paris 75018','CréditAgricole','FR20','FR18',3),(4,'Paris','test','test','FR01',15),(6,'Paris10','La Post','BYJKK VGTDDDD','FR762255',19),(7,'Paris17','Bank Populaire','PFTRHKVDD','FR08220PPO',21),(8,'Paris8','LCL','FR30568FY','FR7508FRANCE',23),(9,'Paris11','LCL','GLLBPOOLLFR','FR56218812',24),(10,'Paris17','LCL','BICFFEFS','FR563859513',25),(11,'Paris12','LCL','BICFRGLLGLY','FR00003336522',NULL),(12,'Paris11','LCL','BICKKJHFGR','FR76563321389',NULL),(14,'PARIS13','LCL','BIChyJJK','FR02314583',33),(15,'Paris11','LCL','BICFRGJKKKB','FR689526',33),(16,'Paris16','La Post','BIVCYYHGG5622','FR76HJLBGT',35),(17,'PAris05','LCL ','BINC566KLLM','FR032154823',40),(18,'Paris01','LCL','BIXKKLLL','BHYHKKLFR',41),(19,'Paris14','Bank Populaire','BICHYTGFFR75','FR0325647866',11),(20,'Paris06','CIC','BICVGHJLKR','FR056314663241',NULL),(21,'Paris06','CIC','BICHMMLOY','FRPOK1235410',NULL),(22,'Paris06','CIC','BICVGTRPOLK','FR023564566GHH',7);
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles` (
  `user_id` int NOT NULL,
  `role_id` int NOT NULL,
  KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`),
  KEY `FKhfh9dx7w3ubf1co1vdev94g3f` (`user_id`),
  CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
INSERT INTO `user_roles` VALUES (1,1),(2,1),(3,1),(4,2),(6,1),(7,1),(8,1),(9,1),(10,1),(11,1),(15,1),(16,1),(19,1),(20,1),(21,1),(24,1),(25,1),(26,1),(27,1),(28,1),(29,1),(30,1),(31,1),(32,1),(33,1),(34,1),(35,1),(36,1),(37,1),(38,1),(39,1),(40,1),(41,1),(42,1);
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `balance` double NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(120) DEFAULT NULL,
  `auth_provider` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
INSERT INTO `users` VALUES (1,3133.0649999999996,'laurentgina_update@gmail.com',NULL,'GINA_Update','$2a$12$vpAdeeCL0BDROgmnqjrGdeCQcbQxu5HPjy4w7LNzHz5yZNRgWj3p.',NULL),(2,4178,'sophiefoncek@gmail.com','Sophie','FONCEK','$2a$12$vpAdeeCL0BDROgmnqjrGdeCQcbQxu5HPjy4w7LNzHz5yZNRgWj3p.',NULL),(3,5168,'agathefeeling@gmail.com','Agathe','FEELING','$2a$12$vpAdeeCL0BDROgmnqjrGdeCQcbQxu5HPjy4w7LNzHz5yZNRgWj3p.',NULL),(4,0,'admin@gmail.com','Admin','ADAM','$2a$12$vpAdeeCL0BDROgmnqjrGdeCQcbQxu5HPjy4w7LNzHz5yZNRgWj3p.',NULL),(6,150,'adamS@gmail.com','Adam','SULTAN','$2a$10$PeT2ZUS0RqKvNViLHyDbiegzY74FJxG/sCNjQgLWIk7maH5s9/ece',NULL),(7,619.0900000000001,'user1@gmail.com','User1','userLastName','$2a$10$.FcyIK4uH7DrDAjji/zIl.C855ZxSFmsrNbSspIeth6SrsLFVRu/K',NULL),(8,419,'user2@gmail.com','user2','User2LastName','$2a$10$i2sxPawxZ6kClTKOEEnlCeBntoZqaCE1Fe9QVVLxOe.51HP1OE2ZC',NULL),(9,0,'user3@gmail.com','User3','User3LastName','$2a$10$/Hzo1LizA3D3ldOlyAmpU..E0LkM.yTkYZ/2ASx3xvjZz1XTE0bWG',NULL),(10,0,'user4@gmail.com','user4','User4LastName','$2a$10$QDb6InBxjNUPRfVmo/ZKG.uLOGPoc3yiAk79dhOKd.H0sGw6v.iXq',NULL),(11,1364.98,'javaachquchi@gmail.com','uyghurJava JavaAchquchi','EMPTY_LastName','$2a$10$/1BousVxi8zrJile8yV67eGprGl.nmiCjcfOKUip1hdcuRHBPWvZm','GOOGLE'),(15,1232.665,'test@gmail.com','Test','LastName','$2a$10$4wdtbxGVWz6/P1AfCdFSEur8dowD7Oa2nDysGyc8HakWtFxwHJhPi',NULL),(16,50,'new1@gmail.com','newUser','newUserLN','$2a$10$GkHH3j4jwgLuVipawKwk0eYph.1G6ZrxxQzQpqqfofX9/kg51TMte',NULL),(19,0,'new4@gmail.com','firstName_Update','new4LN','$2a$10$sTL1BC/tto3jnbYQCzY4YuVuAPi4/1Tzl8PyESkD0w6UTkZZw.jaC',NULL),(20,0,'new5@gmail.com','new5','new5LN','$2a$10$qFbdRhpq3q7/71noTGqrZuYTEq1QETIygGN1f411v0fI3zfsKTa7G',NULL),(21,8744.25,'new6@gmail.com','new6','new6LN','$2a$10$Dq15bcPz.qPiGxw8GUQrjevOG2VWzd22Un5IwD.Q0G5gUsHjxUGJa',NULL),(22,0,'subhy yari','subhy809fr@gmail.com','EMPTY',NULL,'GOOGLE'),(23,2932.665,'subhy809fr@gmail.com','subhy yari','EMPTY_LastName',NULL,'GOOGLE'),(24,-27000,'TEST_LA@gmail.com','Test1','TEST_LN','$2a$10$jzvGi5krw/M6iNCMt4h2O.zAuZVGSAayWKlpE6v3UdtLf7kXMEgbS',NULL),(25,0,'test2@gmail.com','Test2','test2','$2a$10$AqLPx6IGLaA593oksLhDfOyupUMn6Nvb06ILsHbPWGbORXoft4dpi',NULL),(26,0,'test3@gmail.com','Test3','Test3LN','$2a$10$P.r.NkiduFkWH3QeD9scueHyZb.KUBxjDOCvs53f/0wZSOio2lcUi',NULL),(27,0,'test4@gmail.com','Test4','Test4LN','$2a$10$/Ae8TwQZwI0rEkGk2wHsbOK7zQa4qi7Wio3U1v.gbmmAppL8z5hq.',NULL),(28,0,'test5@gmail.com','Test5','Test5','$2a$10$6ZXOnzA/QtcGJeU5mfyVRO4fh/tXwpmLY.xCMZ.nagpbQxYgHTD6a',NULL),(29,0,'test6@gmail.com','Test6','TestLN','$2a$10$kiQdUrWhewRWC5VKcUJOaeuntchVJ6Nzzr.OCG.Y709gjKksGkUzi',NULL),(30,0,'test7@gmail.com','Test7','Test7LN','$2a$10$Cz4w3CqBVGolVfQIccmpxuHdY.HPD4nlZHR3ghKHRco7kXpgHaEmi',NULL),(31,0,'test8@gmail.com','Test8','Test8Ln','$2a$10$AECTVfhAzH4z/C8/tW7ilOg0XSUiHxPhRwogdhd4xjx5uG/xcPLlq',NULL),(32,0,'t9@gmail.com','T9','t9Ln','$2a$10$1Z2lDr3OmrjVCVSVp0atY.NgRXf67JAL3DKzAiHphKLYoUv4YawcK',NULL),(33,0,'t10@gmail.com','t10','t10LN','$2a$10$C739Qqjvdr.YMIh/jYyqeOOLFvDrnPg6rri4jgZMAovNYfN3eDD.G',NULL),(34,0,'t11@gmail.com','t11','t11LN','$2a$10$/zceHlTHO3hx7DpuJyQQQ.h3VSi5VjeehV/GAwKBQb5yYxSGVlO9i',NULL),(35,2944.975,'t12@gmail.com','t12','t12LN','$2a$10$dimah2l4LzjGVJkd6kNkSegszeV1ZR8V3l59n7sMt5x0cY.vI8.dy',NULL),(36,0,'t13@gmail.com','T13','t13@gmail.com','$2a$10$cmebR06DTDrRzo0B6g10B..M39F5Pff2A8Yg5QoUzu8F45Y.fDoJy',NULL),(37,0,'t14@gmail.com','t14','t14LN','$2a$10$jxf.R7LCg/2RotrETMFVduHLHYLAxptfrg76.elmkjcOu198YvtPG',NULL),(38,0,'t15@gmail.com','t15','t15LN','$2a$10$0IYPL3pVmyUnK4bX7rB.G.LFSfQJjiVaUmHm2CqQTU8uoNDh/Odue',NULL),(39,0,'16@gmail.com','t16','t16ln','$2a$10$Hc4Z3NPg86tgCAH.5iCy0O3f/qPm.qnirKhx9OAU9Hlpg/9/sYLlC',NULL),(40,0,'t17@gmail.com','t17','t17LN','$2a$10$V/BSdq6y03lYhoDXRQ6glu6wtzmr59dM1v0khB/JNu78mZlPfPHbq',NULL),(41,0,'t18@gmail.com','t1','t18ln','$2a$10$jqgtAU7cRfQ2HmqedJ3Ra.IRg9PzvRloHbAQh9XoHStAu3UyEo2eq',NULL),(42,0,'email@gmail.com','firstName',NULL,'$2a$10$d43FdSHGGpbDjDCDORDqwOpHL2bi0U7zZQwT4WpOY1zjx6FXkKhoq',NULL);
UNLOCK TABLES;


