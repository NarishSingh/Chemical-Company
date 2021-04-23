USE chemComp;

SELECT c.*
FROM category c
         JOIN productcategory p on c.categoryId = p.categoryId
WHERE p.productId = 1;