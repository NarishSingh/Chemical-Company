package com.ns.chemcomp.dao;

import com.ns.chemcomp.DTO.Role;
import com.ns.chemcomp.DTO.User;
import com.ns.chemcomp.dao.RoleDaoDb.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Repository
public class UserDaoDb implements UserDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public User createUser(User user) {
        String insertQuery = "INSERT INTO chemcomp.user (username, password, enabled, name, phone, email, address) " +
                "VALUES(?,?,?,?,?,?,?);";
        jdbc.update(insertQuery,
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.getName(),
                user.getPhone(),
                user.getEmail(),
                user.getAddress());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID();", Integer.class);
        user.setId(newId);

        //insert to bridge
        insertUserRoles(user);

        return user;
    }

    @Override
    public User readUserById(int id) {
        try {
            String readQuery = "SELECT * FROM user " +
                    "WHERE userId = ?;";
            User user = jdbc.queryForObject(readQuery, new UserMapper(), id);
            associateUserRoles(user);

            return user;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public User readUserByUsername(String username) {
        try {
            String readQuery = "SELECT * FROM user " +
                    "WHERE username = ?;";
            User user = jdbc.queryForObject(readQuery, new UserMapper(), username);
            associateUserRoles(user);

            return user;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public User readEnabledUserById(int id) {
        try {
            String readQuery = "SELECT * FROM chemcomp.user " +
                    "WHERE userId = ? AND enabled != 0;";
            User user = jdbc.queryForObject(readQuery, new UserMapper(), id);
            associateUserRoles(user);

            return user;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        String updateQuery = "UPDATE chemcomp.user " +
                "SET " +
                "username = ?, " +
                "password = ?, " +
                "enabled = ?, " +
                "name = ?, " +
                "phone = ?, " +
                "email = ?, " +
                "address = ? " +
                "WHERE userId = ?;";
        int updated = jdbc.update(updateQuery,
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.getName(),
                user.getPhone(),
                user.getEmail(),
                user.getAddress(),
                user.getId());

        if (updated == 1) {
            //delete from bridge
            String delUR = "DELETE FROM userRole " +
                    "WHERE userId = ?;";
            jdbc.update(delUR, user.getId());

            //reinsert to bridge
            insertUserRoles(user);

            return user;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public boolean deleteUser(int id) {
        /*delete bridge*/
        String delUR = "DELETE FROM userRole " +
                "WHERE userId = ?;";
        jdbc.update(delUR, id);

        /*delete order*/
        //bridge
        //FIXME need to fix ddl's and dto's, I think order, state, product need bridge tables

        //actual orders

        /*delete user*/
    }

    /*Helpers*/

    /**
     * Update the user role bridge table in db
     *
     * @param user {User} well formed obj
     */
    private void insertUserRoles(User user) {
        for (Role role : user.getRoles()) {
            String insertUR = "INSERT INTO userRole (userId, roleId) " +
                    "VALUES(?,?);";
            jdbc.update(insertUR, user.getId(), role.getId());
        }
    }

    /**
     * Associate in memory the set of Role obj's for User
     *
     * @param user {User} well formed obj
     */
    private void associateUserRoles(User user) {
        String selectQuery = "SELECT r.* FROM userRole ur " +
                "JOIN role r ON r.roleId = ur.roleId " +
                "WHERE ur.userId = ?;";
        Set<Role> roles = new HashSet<>(jdbc.query(selectQuery, new RoleMapper(), user.getId()));

        user.setRoles(roles);
    }

    /**
     * Row Mapper impl
     */
    public static final class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            User u = new User();
            u.setId(rs.getInt("userId"));
            u.setUsername(rs.getString("username"));
            u.setPassword(rs.getString("password"));
            u.setEnabled(rs.getBoolean("enabled"));
            u.setName(rs.getString("name"));
            u.setPhone(rs.getString("phone"));
            u.setEmail(rs.getString("email"));
            u.setAddress(rs.getString("address"));

            return u;
        }
    }
}
