INSERT INTO users (first_name, last_name, email, password, balance)
VALUES
    ('Laurent','GINA','laurentgina@gmail.com','user',3000),
    ('Sophie','FONCEK','sophiefoncek@gmail.com','user',4000),
    ('Agathe','FEELING','agathefeeling@gmail.com','user',5000),
    ('Admin','ADAM','admin@gmail.com','admin',0.0);

INSERT INTO contact (user_id, target_id)
VALUES
    (1,2),
    (3,2),
    (2,1);

INSERT INTO user_bank_account(bank_name, bank_location, code_iban, code_bic,balance, app_user_id)
VALUES
    ('LCL','Paris 75015','FR76','FR75',20000,1),
    ('CIC','Lyon', 'FR50','FR70',35000,2),
    ('CréditAgricole','Paris 75018','FR18','FR20',15600,3);

INSERT INTO role (role_name)
VALUES ('USER'),
       ('ADMIN');

INSERT INTO users_roles (app_user_id, roles_id)
VALUES (1, 1),
       (2, 1),
       (3, 1),
       (4, 2);


