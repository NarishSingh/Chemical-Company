package com.ns.chemcomp.dao;

import com.ns.chemcomp.dto.User;

import java.util.List;

public interface UserDao {

    /**
     * Create a new user account
     *
     * @param user {User} well form obj
     * @return {User} successfully added user obj from db
     */
    User createUser(User user);

    /**
     * Retrieve a User by id
     *
     * @param id {int} a valid id
     * @return {User} the obj from db, or null if failed
     */
    User readUserById(int id);

    /**
     * Retrieve a User by username
     *
     * @param username {String} an existing username
     * @return {User} the obj from db, or null if failed
     */
    User readUserByUsername(String username);

    /**
     * Retrieve an enabled User accounts
     *
     * @return {List} objs from db, or null if failed
     */
    List<User> readEnabledUsers();

    /**
     * Read all Users from db
     *
     * @return {List} all obj's in db
     */
    List<User> readAllUsers();

    /**
     * Update a User in db
     *
     * @param user {User} well formed obj with the matching id for id
     * @return {User} the updated obj from db, null if failed
     */
    User updateUser(User user);

    /**
     * Delete a User from db
     *
     * @param id {int} a valid id
     * @return {boolean} true if delete, false if failed
     */
    boolean deleteUser(int id);
}
