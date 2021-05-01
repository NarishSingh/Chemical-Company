USE chemComp;

SELECT c.*
FROM category c
         JOIN productcategory p on c.categoryId = p.categoryId
WHERE p.productId = 1;

INSERT INTO category (categoryName)
VALUES ('Acid'),
       ('Solvent'),
       ('Aqueous Solution'),
       ('Reagant'),
       ('Base'),
       ('Buffer'),
       ('Alcohols');

SELECT *
FROM category;

INSERT INTO product (name, chemicalName, massVolume, measurement, unitCost, handlingCost, photoFilename)
    VALUE ('Alcohol 100%', 'Ethanol', 1.00, 'pt', 20.00, 0.05, null);
INSERT INTO productCategory (productId, categoryId)
        (SELECT LAST_INSERT_ID(), 7);

SELECT p.*, c.categoryName
FROM product p
         JOIN productCategory pc ON p.productId = pc.productId
         JOIN category c ON c.categoryId = pc.categoryId;

INSERT INTO role (role)
VALUES ('ROLE_ADMIN'),
       ('ROLE_BUYER');

INSERT INTO user (username, password, enabled, name, phone, email, address, photoFilename)
VALUES ('Narish', 'password', true, 'Narish Singh', '555-555-5555', 'test@mail.com',
        '123-45 Test st., New York, NY, 99999', null);

INSERT INTO userRole (roleId, userId)
VALUES (1, 1);

UPDATE user
SET password = '$2a$10$GdHarNUmmat.d.AAAV/N6e42tKrFWFHhBG69EuCt2OiTdgv.sMaou'
WHERE userId = 1;

SELECT *
FROM role;
SELECT *
FROM user;
SELECT *
FROM userRole;