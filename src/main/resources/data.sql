INSERT INTO user (first_name, last_name, email, password,balance)
VALUES
    ('Laurent','GINA','laurentgina@gmail.com','laurent2022',3000),
    ('Sophie','FONCEK','sophiefoncek@gmail.com','sophie2021',4000),
    ('Agathe','FEELING','agathefeeling@gmail.com','agathe2000',5000);

INSERT INTO contact (user_id, contact_id)
VALUES
    (1,2),
    (1,3),
    (2,3),
    (3,2),
    (3,1),
    (2,1);



