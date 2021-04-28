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

DELETE
FROM productcategory
WHERE productId = 4;
DELETE
FROM product
WHERE productId = 4;

INSERT INTO product (name, chemicalName, massVolume, measurement, unitCost, handlingCost, photoFilename)
    VALUE ('Alcohol 100%', 'Ethanol', 1.00, 'pt', 20.00, 0.05, null);
INSERT INTO productCategory (productId, categoryId)
        (SELECT LAST_INSERT_ID(), 7);

SELECT p.*, c.categoryName
FROM product p
         JOIN productCategory pc ON p.productId = pc.productId
         JOIN category c ON c.categoryId = pc.categoryId;