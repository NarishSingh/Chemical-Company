package com.ns.chemcomp.dao;

import com.ns.chemcomp.dto.Category;
import com.ns.chemcomp.dto.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ProductDaoTest {

    @Autowired
    ProductDao pDao;

    @Autowired
    CategoryDao cDao;

    static Category c1;
    static Category c2;
    static Category c3;
    static Category c4;

    static Product p1;
    static Product p2;
    static Product p3;

    @BeforeEach
    void setUp() {
        /*clear db*/
        for (Product p : pDao.readAllProducts()) {
            pDao.deleteProduct(p.getProductId());
        }

        for (Category c : cDao.readAllCategories()) {
            cDao.deleteCategory(c.getCategoryId());
        }

        /*setup categories*/
        Category category1 = new Category();
        category1.setCategoryName("Alcohol");

        Category category2 = new Category();
        category2.setCategoryName("Solution");

        Category category3 = new Category();
        category3.setCategoryName("Reagent");

        Category category4 = new Category();
        category4.setCategoryName("Acid");

        c1 = cDao.createCategory(category1);
        c2 = cDao.createCategory(category2);
        c3 = cDao.createCategory(category3);
        c4 = cDao.createCategory(category4);

        /*setup products*/
        p1 = new Product();
        p1.setName("Denatured Alcohol 100%");
        p1.setChemicalName("Ethanol");
        p1.setMassVolume(new BigDecimal("1.00").setScale(2, RoundingMode.HALF_UP));
        p1.setMeasurement("pt");
        p1.setUnitCost(new BigDecimal("20.00").setScale(2, RoundingMode.HALF_UP));
        p1.setHandlingCost(new BigDecimal("0.05").setScale(2, RoundingMode.HALF_UP));
        p1.setPhotoFilename(null);
        p1.setCategory(c1);

        p2 = new Product();
        p2.setName("Lye 50% Solution");
        p2.setChemicalName("Sodium Hydroxide (Aqueous)");
        p2.setMassVolume(new BigDecimal("1.00").setScale(2, RoundingMode.HALF_UP));
        p2.setMeasurement("L");
        p2.setUnitCost(new BigDecimal("26.00").setScale(2, RoundingMode.HALF_UP));
        p2.setHandlingCost(new BigDecimal("0.10").setScale(2, RoundingMode.HALF_UP));
        p2.setPhotoFilename(null);
        p2.setCategory(c2);

        p3 = new Product();
        p3.setName("Glycerol Reagent");
        p3.setChemicalName("Glycerin");
        p3.setMassVolume(new BigDecimal("30.00").setScale(2, RoundingMode.HALF_UP));
        p3.setMeasurement("mL");
        p3.setUnitCost(new BigDecimal("5.00").setScale(2, RoundingMode.HALF_UP));
        p3.setHandlingCost(new BigDecimal("0.25").setScale(2, RoundingMode.HALF_UP));
        p3.setPhotoFilename(null);
        p3.setCategory(c3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createProductReadProductById() {
        Product prod1 = pDao.createProduct(p1);
        Product prod2 = pDao.createProduct(p2);
        Product prod3 = pDao.createProduct(p3);

        Product prod1FromDao = pDao.readProductById(prod1.getProductId());
        Product prod2FromDao = pDao.readProductById(prod2.getProductId());
        Product prod3FromDao = pDao.readProductById(prod3.getProductId());

        assertNotNull(prod1);
        assertNotNull(prod1FromDao);
        assertNotNull(prod2);
        assertNotNull(prod2FromDao);
        assertNotNull(prod3);
        assertNotNull(prod3FromDao);
        assertEquals(prod1, prod1FromDao);
        assertEquals(prod2, prod2FromDao);
        assertEquals(prod3, prod3FromDao);
    }

    @Test
    void readAllProducts() {
        Product prod1 = pDao.createProduct(p1);
        Product prod2 = pDao.createProduct(p2);
        Product prod3 = pDao.createProduct(p3);

        List<Product> products = pDao.readAllProducts();

        assertNotNull(products);
        assertEquals(3, products.size());
        assertTrue(products.contains(prod1));
        assertTrue(products.contains(prod2));
        assertTrue(products.contains(prod3));
    }

    @Test
    void readProductsByCategory() {
        Product prod1 = pDao.createProduct(p1);
        Product prod2 = pDao.createProduct(p2);
        Product prod3 = pDao.createProduct(p3);

        List<Product> alcohols = pDao.readProductsByCategory(c1.getCategoryId());

        assertNotNull(alcohols);
        assertEquals(1, alcohols.size());
        assertTrue(alcohols.contains(prod1));
        assertFalse(alcohols.contains(prod2));
        assertFalse(alcohols.contains(prod3));
    }

    @Test
    void updateProduct() {
        Product prod1 = pDao.createProduct(p1);
        Product original = pDao.readProductById(prod1.getProductId());

        prod1.setName("Glacial Acetic Acid");
        prod1.setChemicalName("Acetic Acid/Ethanoic Acid");
        prod1.setMeasurement("L");
        prod1.setUnitCost(new BigDecimal("41.33"));
        prod1.setHandlingCost(new BigDecimal("0.25"));
        prod1.setCategory(c4);

        Product edit = pDao.updateProduct(prod1);
        Product updated = pDao.readProductById(prod1.getProductId());

        assertNotNull(original);
        assertNotNull(edit);
        assertNotNull(updated);
        assertNotEquals(original, updated);
        assertNotEquals(original, edit);
        assertEquals(edit, updated);
    }

    @Test
    void deleteProduct() {
        Product prod1 = pDao.createProduct(p1);
        Product prod2 = pDao.createProduct(p2);
        Product prod3 = pDao.createProduct(p3);
        List<Product> original = pDao.readAllProducts();

        boolean deleted = pDao.deleteProduct(prod3.getProductId());
        List<Product> afterDel = pDao.readAllProducts();

        assertNotNull(original);
        assertEquals(3, original.size());
        assertNotNull(afterDel);
        assertTrue(deleted);
        assertEquals(2, afterDel.size());
        assertTrue(afterDel.contains(prod1));
        assertTrue(afterDel.contains(prod2));
        assertFalse(afterDel.contains(prod3));
    }
}
