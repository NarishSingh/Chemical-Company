package com.ns.chemcomp.dao;

import com.ns.chemcomp.dao.RoleDaoDb.RoleMapper;
import com.ns.chemcomp.dto.Role;
import com.ns.chemcomp.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class UserDaoDb implements UserDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public User createUser(User user) {
        String insertQuery = "INSERT INTO user (username, password, enabled, name, phone, email, address, photoFilename) " +
                "VALUES(?,?,?,?,?,?,?,?);";
        jdbc.update(insertQuery,
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.getName(),
                user.getPhone(),
                user.getEmail(),
                user.getAddress(),
                user.getPhotoFilename());

        //grab id
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID();", Integer.class);
        user.setUserId(newId);

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
    public User readEnabledUserByUsername(String username) {
        try {
            String readQuery = "SELECT * FROM user " +
                    "WHERE username = ? AND enabled != 0;";
            User activeUser = jdbc.queryForObject(readQuery, new UserMapper(), username);
            associateUserRoles(activeUser);

            return activeUser;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<User> readEnabledUsers() {
        String readQuery = "SELECT * FROM user " +
                "WHERE enabled != 0;";
        List<User> enabledUsers = jdbc.query(readQuery, new UserMapper());
        for (User user : enabledUsers) {
            associateUserRoles(user);
        }

        return enabledUsers;
    }

    @Override
    public List<User> readAllUsers() {
        String readAll = "SELECT * FROM user;";
        List<User> users = jdbc.query(readAll, new UserMapper());

        for (User u : users) {
            associateUserRoles(u);
        }

        return users;
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        String updateQuery = "UPDATE user " +
                "SET " +
                "username = ?, " +
                "password = ?, " +
                "enabled = ?, " +
                "name = ?, " +
                "phone = ?, " +
                "email = ?, " +
                "address = ?, " +
                "photoFilename = ? " +
                "WHERE userId = ?;";
        int updated = jdbc.update(updateQuery,
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.getName(),
                user.getPhone(),
                user.getEmail(),
                user.getAddress(),
                user.getPhotoFilename(),
                user.getUserId());

        if (updated == 1) {
            //delete from bridge
            String delUR = "DELETE FROM userRole " +
                    "WHERE userId = ?;";
            jdbc.update(delUR, user.getUserId());

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
        //delete from State bridge
        String delOS = "DELETE FROM orderState " +
                "WHERE orderId IN " +
                "(SELECT orderId FROM `order` " +
                "WHERE userId = ?);";
        jdbc.update(delOS, id);

        //delete from Product bridge
        String delOP = "DELETE FROM orderProduct " +
                "WHERE orderId IN " +
                "(SELECT orderId FROM `order` " +
                "WHERE userId = ?);";
        jdbc.update(delOP, id);

        //delete order
        String delOrder = "DELETE FROM `order` " +
                "WHERE userId = ?;";
        jdbc.update(delOrder, id);

        /*delete user*/
        String deleteUser = "DELETE FROM user " +
                "WHERE userId = ?;";
        return jdbc.update(deleteUser, id) == 1;
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
            jdbc.update(insertUR, user.getUserId(), role.getRoleId());
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
        Set<Role> roles = new HashSet<>(jdbc.query(selectQuery, new RoleMapper(), user.getUserId()));

        user.setRoles(roles);
    }

    /**
     * Row Mapper impl
     */
    public static final class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            User u = new User();
            u.setUserId(rs.getInt("userId"));
            u.setUsername(rs.getString("username"));
            u.setPassword(rs.getString("password"));
            u.setEnabled(rs.getBoolean("enabled"));
            u.setName(rs.getString("name"));
            u.setPhone(rs.getString("phone"));
            u.setEmail(rs.getString("email"));
            u.setAddress(rs.getString("address"));
            u.setPhotoFilename(rs.getString("photoFilename"));

            return u;
        }
    }
}
