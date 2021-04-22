package com.ns.chemcomp.dao;

import com.ns.chemcomp.dto.Category;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryDaoDb implements CategoryDao {

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
