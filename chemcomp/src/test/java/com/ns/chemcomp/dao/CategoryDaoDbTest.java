package com.ns.chemcomp.dao;

import com.ns.chemcomp.dto.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class CategoryDaoDbTest {

    @Autowired
    CategoryDao cDao;

    static Category c1;
    static Category c2;
    static Category c3;
    static Category c4;

    @BeforeEach
    void setUp() {
        /*clear db*/
        for (Category c : cDao.readAllCategories()) {
            cDao.deleteCategory(c.getCategoryId());
        }

        /*setup categories*/
        c1 = new Category();
        c1.setCategoryName("Alcohol");

        c2 = new Category();
        c2.setCategoryName("Solution");

        c3 = new Category();
        c3.setCategoryName("Reagent");

        c4 = new Category();
        c4.setCategoryName("Acid");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createCategoryReadCategoryById() {
        Category category1 = cDao.createCategory(c1);
        Category category2 = cDao.createCategory(c2);
        Category category3 = cDao.createCategory(c3);
        Category category4 = cDao.createCategory(c4);

        Category c1FromDao = cDao.readCategoryById(category1.getCategoryId());
        Category c2FromDao = cDao.readCategoryById(category2.getCategoryId());
        Category c3FromDao = cDao.readCategoryById(category3.getCategoryId());
        Category c4FromDao = cDao.readCategoryById(category4.getCategoryId());

        assertNotNull(category1);
        assertNotNull(c1FromDao);
        assertNotNull(category2);
        assertNotNull(c2FromDao);
        assertNotNull(category3);
        assertNotNull(c3FromDao);
        assertNotNull(category4);
        assertNotNull(c4FromDao);
        assertEquals(category1, c1FromDao);
        assertEquals(category2, c2FromDao);
        assertEquals(category3, c3FromDao);
        assertEquals(category4, c4FromDao);
    }

    @Test
    void readAllCategories() {
        Category category1 = cDao.createCategory(c1);
        Category category2 = cDao.createCategory(c2);
        Category category3 = cDao.createCategory(c3);
        Category category4 = cDao.createCategory(c4);

        List<Category> categories = cDao.readAllCategories();

        assertNotNull(categories);
        assertEquals(4, categories.size());
        assertTrue(categories.contains(category1));
        assertTrue(categories.contains(category2));
        assertTrue(categories.contains(category3));
        assertTrue(categories.contains(category4));
    }

    @Test
    void updateCategory() {
        Category category1 = cDao.createCategory(c1);
        Category original = cDao.readCategoryById(category1.getCategoryId());

        category1.setCategoryName("Edit");
        Category edit = cDao.updateCategory(category1);
        Category updated = cDao.readCategoryById(category1.getCategoryId());

        assertNotNull(original);
        assertNotNull(edit);
        assertNotNull(updated);
        assertNotEquals(original, updated);
        assertNotEquals(original, edit);
        assertEquals(edit, updated);
    }

    @Test
    void deleteCategory() {
        Category category1 = cDao.createCategory(c1);
        Category category2 = cDao.createCategory(c2);
        Category category3 = cDao.createCategory(c3);
        Category category4 = cDao.createCategory(c4);

        List<Category> original = cDao.readAllCategories();

        boolean deleted = cDao.deleteCategory(category1.getCategoryId());
        List<Category> afterDel = cDao.readAllCategories();

        assertNotNull(original);
        assertEquals(4, original.size());
        assertNotNull(afterDel);
        assertTrue(deleted);
        assertEquals(3, afterDel.size());
        assertFalse(afterDel.contains(category1));
        assertTrue(afterDel.contains(category2));
        assertTrue(afterDel.contains(category3));
        assertTrue(afterDel.contains(category4));
    }
}