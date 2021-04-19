package com.ns.chemcomp.dao;

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

        return product;
    }

    @Override
    public Product readProductById(int id) {
        try {
            String readId = "SELECT * FROM product " +
                    "WHERE productId = ?;";
            return jdbc.queryForObject(readId, new ProductMapper(), id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Product> readAllProducts() {
        String readAll = "SELECT * FROM product;";
        return jdbc.query(readAll, new ProductMapper());
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
            return product;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public boolean deleteProduct(int id) {
        //delete from bridge
        String delPO = "DELETE FROM orderProduct " +
                "WHERE productId = ?;";
        jdbc.update(delPO, id);

        //delete product
        String deleteProduct = "DELETE FROM product " +
                "WHERE productId = ?;";
        return jdbc.update(deleteProduct, id) == 1;
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
