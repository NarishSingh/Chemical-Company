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