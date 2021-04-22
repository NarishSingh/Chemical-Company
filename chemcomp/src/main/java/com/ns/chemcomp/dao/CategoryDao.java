package com.ns.chemcomp.dao;

import com.ns.chemcomp.dto.Category;

import java.util.List;

public interface CategoryDao {

    /**
     * Create a Category for products
     *
     * @param category {Category} well formed obh
     * @return {Category} successfully added obj from db
     */
    Category createCategory(Category category);

    /**
     * Read a category from db
     *
     * @param id {int} a valid id
     * @return {Category} obj from db, null if read fails
     */
    Category readCategoryById(int id);

    /**
     * Read all categories from db
     *
     * @return {List} all objs in db
     */
    List<Category> readAllCategories();

    /**
     * Update a category in db
     *
     * @param category {Category} well formed obj
     * @return {Category} updated obj from db, null if update failed
     */
    Category updateCategory(Category category);

    /**
     * Delete a Category from db
     *
     * @param id {int} a valid id
     * @return {boolean} true if deleted, false if delete failed
     */
    boolean deleteCategory(int id);
}
