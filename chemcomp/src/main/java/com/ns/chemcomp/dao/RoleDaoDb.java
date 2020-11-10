package com.ns.chemcomp.dao;

import com.ns.chemcomp.dto.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RoleDaoDb implements RoleDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Role createRole(Role role) {
        String createQuery = "INSERT INTO role (role) " +
                "VALUES(?);";
        jdbc.update(createQuery, role.getRole());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID();", Integer.class);
        role.setId(newId);

        return role;
    }

    @Override
    public Role readRoleById(int id) {
        try {
            String readQuery = "SELECT * FROM role " +
                    "WHERE roleId = ?;";
            return jdbc.queryForObject(readQuery, new RoleMapper(), id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public Role readRoleByRole(String role) {
        try {
            String readQuery = "SELECT * FROM role " +
                    "WHERE role = ?;";
            return jdbc.queryForObject(readQuery, new RoleMapper(), role);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Role> readAllRoles() {
        String readAllQuery = "SELECT * FROM role;";
        return jdbc.query(readAllQuery, new RoleMapper());
    }

    @Override
    public Role updateRole(Role role) {
        String updateQuery = "UPDATE role " +
                "SET role = ? " +
                "WHERE roleId = ?;";

        if (jdbc.update(updateQuery, role.getRole(), role.getId()) == 1) {
            return role;
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteRole(int id) {
        //delete from bridge
        String bridgeDel = "DELETE FROM userRole " +
                "WHERE roleId = ?;";
        jdbc.update(bridgeDel, id);

        //delete role
        String delQuery = "DELETE FROM role " +
                "WHERE roleId = ?;";
        return jdbc.update(delQuery, id) == 1;
    }

    /**
     * Row mapper impl
     */
    public static final class RoleMapper implements RowMapper<Role> {

        @Override
        public Role mapRow(ResultSet rs, int i) throws SQLException {
            Role r = new Role();
            r.setId(rs.getInt("roleId"));
            r.setRole(rs.getString("role"));

            return r;
        }
    }
}
