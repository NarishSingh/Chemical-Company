package com.ns.chemcomp.dao;

import com.ns.chemcomp.dto.Product;

import java.util.List;

public interface ProductDao {

    /**
     * Create Product in db
     *
     * @param product {Product} well formed obj
     * @return {Product} successfully added obj from db
     */
    Product createProduct(Product product);

    /**
     * Read Product from db
     *
     * @param id {int} a valid id
     * @return {Product} obj from db, null if read fails
     */
    Product readProductById(int id);

    /**
     * Read all Products from db
     *
     * @return {List} all objs existing in db
     */
    List<Product> readAllProducts();

    /**
     * Read all products from a given category
     *
     * @param categoryId {int} a valid category id
     * @return {List} all products for a category
     */
    List<Product> readProductsByCategory(int categoryId);

    /**
     * Update a Product in db
     *
     * @param product {Product} well formed obj with matching id
     * @return {Product} updated obj from db, null if update failed
     */
    Product updateProduct(Product product);

    /**
     * Delete a Product from db
     *
     * @param id {int} a valid id
     * @return {boolean} true if deleted, false if delete fails
     */
    boolean deleteProduct(int id);
}
