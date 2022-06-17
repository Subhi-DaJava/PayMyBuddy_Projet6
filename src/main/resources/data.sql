INSERT INTO user (first_name, last_name, email, password, balance)
VALUES
    ('Laurent','GINA','laurentgina@gmail.com','$2a$12$DSKZyrRjPrvGHA/mPLQCLORpvO6bV4ax7mFEFixTGWboBpSSYZAJa',3000),
    ('Sophie','FONCEK','sophiefoncek@gmail.com','$2a$12$DSKZyrRjPrvGHA/mPLQCLORpvO6bV4ax7mFEFixTGWboBpSSYZAJa',4000),
    ('Agathe','FEELING','agathefeeling@gmail.com','$2a$12$DSKZyrRjPrvGHA/mPLQCLORpvO6bV4ax7mFEFixTGWboBpSSYZAJa',5000),
    ('Adam','Admin','admin@gmail.com','$2a$12$8tJ8n.csEovV0cv5F76jLOAefavGwcYX9NmbZJpG1.QUIRy56krDO
',5000);

INSERT INTO contact (user_id, contact_id)
VALUES
    (1,2),
    (3,2),
    (2,1);

INSERT INTO user_bank_account(bank_name, bank_location, code_iban, code_bic,balance, user_id)
VALUES
    ('LCL','Paris 75015','FR76','FR75',20000,1),
    ('CIC','Lyon', 'FR50','FR70',35000,2),
    ('CréditAgricole','Paris 75018','FR18','FR20',15600,3);

INSERT INTO roles (role_name)
VALUES ('USER'),
       ('ADMIN');

INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1),
       (2, 1),
       (3, 1),
       (4, 2);

