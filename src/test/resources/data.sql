INSERT INTO users (first_name, last_name, email, password, balance)
VALUES
    ('Laurent','GINA','laurentgina@gmail.com','$2a$12$vpAdeeCL0BDROgmnqjrGdeCQcbQxu5HPjy4w7LNzHz5yZNRgWj3p.',3000),
    ('Sophie','FONCEK','sophiefoncek@gmail.com','$2a$12$vpAdeeCL0BDROgmnqjrGdeCQcbQxu5HPjy4w7LNzHz5yZNRgWj3p.',4000),
    ('Agathe','FEELING','agathefeeling@gmail.com','$2a$12$vpAdeeCL0BDROgmnqjrGdeCQcbQxu5HPjy4w7LNzHz5yZNRgWj3p.',5000),
    ('firstName','LASTNAME','test@gmail.com','$2a$12$vpAdeeCL0BDROgmnqjrGdeCQcbQxu5HPjy4w7LNzHz5yZNRgWj3p.', 0.0),
    ('Admin','ADAM','admin@gmail.com','$2a$12$vpAdeeCL0BDROgmnqjrGdeCQcbQxu5HPjy4w7LNzHz5yZNRgWj3p.',0.0);

INSERT INTO connections (user_id, target_id)
VALUES
    (1,2),
    (1,3),
    (3,2),
    (2,1);

INSERT INTO user_bank_account(bank_name, bank_location, code_iban, code_bic,app_user_id)
VALUES
    ('LCL','Paris 75015','FR76','FR75',1),
    ('CIC','Lyon', 'FR50','FR70',2),
    ('CréditAgricole','Paris 75018','FR18','FR20',3);

INSERT INTO roles (role_name)
VALUES ('USER'),
       ('ADMIN');

INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1),
       (2, 1),
       (3, 1),
       (4, 1),
       (5, 2);
INSERT INTO `transaction` VALUES (1,4,'2022-06-28','descriptionDDD',0.02,1,2),
                                 (2,4,'2022-06-28','descriptionDDD',0.02,1,3),
                                 (3,55,'2022-06-28','descriptionDDD',0.275,1,3),
                                 (4,50,'2022-06-28','descriptionDDD',0.25,1,2),
                                 (5,8,'2022-06-28','descriptionDDD',0.04,1,2),
                                 (6,55,'2022-06-28','descriptionDDD',0.275,1,2),
                                 (7,1,'2022-06-28','descriptionDDD ddfsdfs',0.005,1,2);

INSERT INTO `transfer` VALUES (1,1000,'Premier Virement vers PayMyBuddy','DEBIT','2022-06-29',1),
                              (2,500,'virement ','DEBIT','2022-06-30',2),
                              (3,600,'test','DEBIT','2022-06-30',2),
                              (4,200,'test','DEBIT','2022-06-30',2),
                              (5,5000,'Premier Virement vers PayMyBuddy','DEBIT','2022-07-01',1),
                              (6,3000,'Just a test','CREDIT','2022-07-01',3),
                              (7,2100,'Just a test','CREDIT','2022-07-01',2);



