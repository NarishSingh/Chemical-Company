USE chemComp;

SELECT c.*
FROM category c
         JOIN productcategory p on c.categoryId = p.categoryId
WHERE p.productId = 1;

DELETE
FROM category;

INSERT INTO category (categoryId, categoryName)
VALUES (1, 'Acid'),
       (2, 'Solvent'),
       (3, 'Aqueous Solution'),
       (4, 'Reagent'),
       (5, 'Base'),
       (6, 'Buffer'),
       (7, 'Alcohols');

DELETE
FROM productcategory;
DELETE
FROM product;

INSERT INTO product (name, chemicalName, massVolume, measurement, unitCost, handlingCost, photoFilename)
    VALUE ('Alcohol 100%', 'Ethanol', 1.00, 'pt', 20.00, 0.05,
           'C:\\Users\\naris\\Documents\\Work\\TECHHIRE\\REPOSITORY\\Chemical-Company\\chemcomp\\src\\main\\resources\\static\\images\\uploads\\chemical.jpg');
INSERT INTO productCategory (productId, categoryId)
        (SELECT LAST_INSERT_ID(), 7);

INSERT INTO product (name, chemicalName, massVolume, measurement, unitCost, handlingCost, photoFilename)
    VALUE ('Glycerol Reagent', 'Glycerin', 30.00, 'mL', 5.00, 0.25, null);
INSERT INTO productCategory (productId, categoryId)
        (SELECT LAST_INSERT_ID(), 4);

SELECT *
FROM product;

SELECT *
FROM category;

SELECT p.*, c.categoryName
FROM product p
         JOIN productCategory pc ON p.productId = pc.productId
         JOIN category c ON c.categoryId = pc.categoryId;

INSERT INTO role (role)
VALUES ('ROLE_ADMIN'),
       ('ROLE_BUYER');

INSERT INTO user (username, password, enabled, name, phone, email, address)
VALUES ('Narish', 'password', true, 'Narish Singh', '555-555-5555', 'test@mail.com',
        '123-45 Test st., New York, NY, 99999');

INSERT INTO userRole (roleId, userId)
VALUES (1, 1);

UPDATE user
SET password = '$2a$10$YeXWyTFmv5I5rtIRjQOfUevizmPZRo07rz42gGM1dPqUbQmv4yRsa'
WHERE userId = 1;

SELECT *
FROM role;
SELECT *
FROM user;
SELECT *
FROM userRole;