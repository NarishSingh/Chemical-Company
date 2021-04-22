package com.ns.chemcomp.dao;

import com.ns.chemcomp.dto.Category;
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
public class CategoryDaoDb implements CategoryDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Category createCategory(Category category) {
        String insert = "INSERT INTO category (categoryName) " +
                "VALUES (?);";
        jdbc.update(insert, category.getCategoryName());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID();", Integer.class);
        category.setCategoryId(newId);

        return category;
    }

    @Override
    public Category readCategoryById(int id) {
        try {
            String readId = "SELECT * FROM category " +
                    "WHERE categoryId = ?;";
            return jdbc.queryForObject(readId, new CategoryMapper(), id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Category> readAllCategories() {
        String readAll = "SELECT * FROM category;";
        return jdbc.query(readAll, new CategoryMapper());
    }

    @Override
    @Transactional
    public Category updateCategory(Category category) {
        String update = "UPDATE category " +
                "SET " +
                "categoryName = ? " +
                "WHERE categoryId = ?;";
        int updated = jdbc.update(update, category.getCategoryName(), category.getCategoryId());

        if (updated == 1) {
            return category;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public boolean deleteCategory(int id) {
        String delete = "DELETE FROM category " +
                "WHERE categoryId = ?;";
        return jdbc.update(delete, id) == 1;
    }

    /**
     * RowMapper impl
     */
    public static final class CategoryMapper implements RowMapper<Category> {

        @Override
        public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
            Category c = new Category();
            c.setCategoryId(rs.getInt("categoryId"));
            c.setCategoryName(rs.getString("categoryName"));

            return c;
        }
    }
}
