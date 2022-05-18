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

INSERT INTO user_bank_account(balance,account_name, user_id)
VALUES
    (20000,'L_Gina',1),
    (25000,'S_Foncek',2),
    (45000,'A_Feeling',3);


