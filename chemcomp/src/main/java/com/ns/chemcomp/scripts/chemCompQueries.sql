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

SELECT *
FROM product;
SELECT *
FROM productCategory;