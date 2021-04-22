package com.ns.chemcomp.dao;

import com.ns.chemcomp.dao.CategoryDaoDb.CategoryMapper;
import com.ns.chemcomp.dto.Category;
import com.ns.chemcomp.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductDaoDb implements ProductDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Product createProduct(Product product) {
        String insert = "INSERT INTO product (name, chemicalName, massVolume, measurement, unitCost, handlingCost, photoFilename) " +
                "VALUES(?,?,?,?,?,?,?);";
        jdbc.update(insert,
                product.getName(),
                product.getChemicalName(),
                product.getMassVolume(),
                product.getMeasurement(),
                product.getUnitCost(),
                product.getHandlingCost(),
                product.getPhotoFilename());

        //grab id
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID();", Integer.class);
        product.setProductId(newId);

        //insert to bridge
        insertProductCategory(product);

        return product;
    }

    @Override
    public Product readProductById(int id) {
        try {
            String readId = "SELECT * FROM product " +
                    "WHERE productId = ?;";
            Product product = jdbc.queryForObject(readId, new ProductMapper(), id);
            associateProductCategory(product);

            return product;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Product> readAllProducts() {
        String readAll = "SELECT * FROM product;";
        List<Product> products = jdbc.query(readAll, new ProductMapper());
        for (Product p : products) {
            associateProductCategory(p);
        }

        return products;
    }

    @Override
    @Transactional
    public Product updateProduct(Product product) {
        String update = "UPDATE product " +
                "SET " +
                "name = ?, " +
                "chemicalName = ?, " +
                "massVolume = ?, " +
                "measurement = ?, " +
                "unitCost = ?, " +
                "handlingCost = ?, " +
                "photoFilename = ? " +
                "WHERE productId = ?;";
        int updated = jdbc.update(update,
                product.getName(),
                product.getChemicalName(),
                product.getMassVolume(),
                product.getMeasurement(),
                product.getUnitCost(),
                product.getHandlingCost(),
                product.getPhotoFilename(),
                product.getProductId());

        if (updated == 1) {
            //delete and reinsert to bridge
            String delPc = "DELETE FROM productCategory " +
                    "WHERE productId = ?;";
            jdbc.update(delPc, product.getProductId());

            insertProductCategory(product);

            return product;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public boolean deleteProduct(int id) {
        //delete from bridges
        String delPO = "DELETE FROM orderProduct " +
                "WHERE productId = ?;";
        jdbc.update(delPO, id);

        String delPC = "DELETE FROM productCategory " +
                "WHERE productId = ?;";
        jdbc.update(delPC, id);

        //delete product itself
        String deleteProduct = "DELETE FROM product " +
                "WHERE productId = ?;";
        return jdbc.update(deleteProduct, id) == 1;
    }

    /*HELPERS*/

    /**
     * Update productCategory bridge table in db
     *
     * @param product {Product} well formed obj
     */
    private void insertProductCategory(Product product) {
        String insertQuery = "INSERT INTO productCategory (productId, categoryId) " +
                "VALUES(?,?);";
        jdbc.update(insertQuery, product.getProductId(), product.getCategory().getCategoryId());
    }

    /**
     * Associate Category with Product in memory
     *
     * @param product {Product} well formed obj
     */
    private void associateProductCategory(Product product) {
        String selectQuery = "SELECT c.* FROM category c " +
                "JOIN productCategory pc on c.categoryId = pc.categoryId " +
                "WHERE pc.productId = ?;";
        Category c = jdbc.queryForObject(selectQuery, new CategoryMapper(), product.getProductId());

        product.setCategory(c);
    }

    /**
     * RowMapper impl
     */
    public static final class ProductMapper implements RowMapper<Product> {

        @Override
        public Product mapRow(ResultSet rs, int i) throws SQLException {
            Product p = new Product();
            p.setProductId(rs.getInt("productId"));
            p.setName(rs.getString("name"));
            p.setChemicalName(rs.getString("chemicalName"));
            p.setMassVolume(rs.getBigDecimal("massVolume"));
            p.setMeasurement(rs.getString("measurement"));
            p.setUnitCost(rs.getBigDecimal("unitCost"));
            p.setHandlingCost(rs.getBigDecimal("handlingCost"));
            p.setPhotoFilename(rs.getString("photoFilename"));

            return p;
        }
    }
}
