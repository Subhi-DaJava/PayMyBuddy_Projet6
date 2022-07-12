CREATE DATABASE  IF NOT EXISTS `paymybuddy`;
USE `paymybuddy`;
-- Database: paymybuddy
-- ------------------------------------------------------
-- Server version	8.0.29
INSERT INTO `users` VALUES (1, 5000,'laurentgina@gmail.com','Laurent','GINA','$2a$12$vpAdeeCL0BDROgmnqjrGdeCQcbQxu5HPjy4w7LNzHz5yZNRgWj3p.', null),
                           (2, 4178,'sophiefoncek@gmail.com','Sophie','FONCEK','$2a$12$vpAdeeCL0BDROgmnqjrGdeCQcbQxu5HPjy4w7LNzHz5yZNRgWj3p.',NULL),
                           (3, 5168,'agathefeeling@gmail.com','Agathe','FEELING','$2a$12$vpAdeeCL0BDROgmnqjrGdeCQcbQxu5HPjy4w7LNzHz5yZNRgWj3p.',NULL),
                           (4, 0.0,'test@gmail.com','firstName','LASTNAME','$2a$12$vpAdeeCL0BDROgmnqjrGdeCQcbQxu5HPjy4w7LNzHz5yZNRgWj3p.', null),
                           (5, 0,'admin@gmail.com','Admin','ADAM','$2a$12$vpAdeeCL0BDROgmnqjrGdeCQcbQxu5HPjy4w7LNzHz5yZNRgWj3p.',NULL),
                           (6, 150,'adamS@gmail.com','Adam','SULTAN','$2a$10$PeT2ZUS0RqKvNViLHyDbiegzY74FJxG/sCNjQgLWIk7maH5s9/ece',NULL),
                           (7, 419,'user2@gmail.com','user2','User2LastName','$2a$10$i2sxPawxZ6kClTKOEEnlCeBntoZqaCE1Fe9QVVLxOe.51HP1OE2ZC',NULL),
                           (8, 0,'user3@gmail.com','User3','User3LastName','$2a$10$/Hzo1LizA3D3ldOlyAmpU..E0LkM.yTkYZ/2ASx3xvjZz1XTE0bWG',NULL),
                           (9, 0,'user4@gmail.com','user4','User4LastName','$2a$10$QDb6InBxjNUPRfVmo/ZKG.uLOGPoc3yiAk79dhOKd.H0sGw6v.iXq',NULL),
                           (10, 1364.98,'javaachquchi@gmail.com','uyghurJava JavaAchquchi','EMPTY_LastName',NULL,'GOOGLE'),
                           (11, 0,'subhy yari','subhy809fr@gmail.com','EMPTY',NULL,'GOOGLE');

INSERT INTO `connections` VALUES (1,6),(1,7),(1,8),(1,2),(1,3),(2,1),(3,1),(11,1),(11,2),(3,8),(2,10),(1,11),(2,9),(1,4),(10,11),(2,7), (7,3),(3,7);

INSERT INTO `roles` VALUES (2,'ADMIN'),
                           (1,'USER');

INSERT INTO `user_roles` VALUES (1,1),(2,1),(3,1),(4,1),(5,2),(6,1),(7,1),(8,1),(9,1);

INSERT INTO `transaction` VALUES (1, 4,'2022-06-28','descriptionDDD',0.02, 1,2),
                                 (2, 4,'2022-06-28','descriptionDDD',0.02, 1,3),
                                 (3, 55,'2022-06-28','descriptionDDD',0.275, 1,3),
                                 (4, 50,'2022-06-28','descriptionDDD',0.25, 1,2),
                                 (5, 8,'2022-06-28','descriptionDDD',0.04, 1,2),
                                 (6, 55,'2022-06-28','descriptionDDD',0.275, 1,2),
                                 (7, 1,'2022-06-28','descriptionDDD ddfsdfs', 0.005, 1,2),
                                 (8, 5,'2022-06-29','descriptionDDD',0.025, 1,2),
                                 (9,5,'2022-06-29','nouveau',0.025, 1,2),
                                 (10, 500,'2022-06-29','Just a test', 2.5, 1,7),
                                 (11, 50,'2022-06-29','Just a test', 0.25, 7,3);

INSERT INTO `user_bank_account` VALUES
                                    (1,'Paris 75015','LCL','FR75','FR76',1),
                                    (2,'Lyon','CIC','FR70','FR50',2),
                                    (3,'Paris 75018','CréditAgricole','FR20','FR18',3),
                                    (4,'Paris','test','test','FR01',6),
                                    (5,'Paris10','La Post','BYJKK VGTDDDD','FR762255',7),
                                    (6,'Paris17','Bank Populaire','PFTRHKVDD','FR08220PPO',8),
                                    (7,'Paris8','LCL','FR30568FY','FR7508FRANCE',9),
                                    (8,'Paris11','LCL','GLLBPOOLLFR','FR56218812',10);

INSERT INTO `transfer` VALUES (1, 1000,'Premier Virement vers PayMyBuddy','DEBIT','2022-06-29',1),
                              (2, 500,'virement ','DEBIT','2022-06-30',2),
                              (3, 600,'test','DEBIT','2022-06-30',2),
                              (4, 200,'test','DEBIT','2022-06-30',3),
                              (5, 5000,'Premier Virement vers PayMyBuddy','DEBIT','2022-07-01',4),
                              (6, 3000,'Just a test','CREDIT','2022-07-01',5),
                              (7, 2100,'Just a test','CREDIT','2022-07-01',6),
                              (8, 200,'Premier virement vers Bank','DEBIT','2022-07-01',7),
                              (9, 3000,'Just a test','CREDIT','2022-07-02', 8),
                              (10, 300,'Test For MyTransfers','CREDIT','2022-07-02',1);

