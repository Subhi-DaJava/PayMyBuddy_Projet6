INSERT INTO users (first_name, last_name, email, password, balance)
VALUES
    ('Laurent','GINA','laurentgina@gmail.com','$2a$12$vpAdeeCL0BDROgmnqjrGdeCQcbQxu5HPjy4w7LNzHz5yZNRgWj3p.',3000),
    ('Sophie','FONCEK','sophiefoncek@gmail.com','$2a$12$vpAdeeCL0BDROgmnqjrGdeCQcbQxu5HPjy4w7LNzHz5yZNRgWj3p.',4000),
    ('Agathe','FEELING','agathefeeling@gmail.com','$2a$12$vpAdeeCL0BDROgmnqjrGdeCQcbQxu5HPjy4w7LNzHz5yZNRgWj3p.',5000),
    ('Admin','ADAM','admin@gmail.com','$2a$12$vpAdeeCL0BDROgmnqjrGdeCQcbQxu5HPjy4w7LNzHz5yZNRgWj3p.',0.0);

INSERT INTO connections (user_id, target_id)
VALUES
    (1,2),
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
       (4, 2);


