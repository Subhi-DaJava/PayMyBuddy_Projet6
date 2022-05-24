INSERT INTO user (first_name, last_name, user_name, email, password, balance)
VALUES
    ('Laurent','GINA','LG_ami','laurentgina@gmail.com','laurent2022',3000),
    ('Sophie','FONCEK','SF_cousine','sophiefoncek@gmail.com','sophie2021',4000),
    ('Agathe','FEELING','AF_frère','agathefeeling@gmail.com','agathe2000',5000);

INSERT INTO contact (user_id, contact_id)
VALUES
    (1,2),
    (1,3),
    (2,3),
    (3,2),
    (3,1),
    (2,1);

INSERT INTO user_bank_account(bank_name, bank_location, code_iban, code_bic,balance, user_id)
VALUES
    ('LCL','Paris 75015','FR76','FR75',20000,1),
    ('CIC','Lyon', 'FR50','FR70',35000,2),
    ('CréditAgricole','Paris 75018','FR18','FR20',15600,3);


