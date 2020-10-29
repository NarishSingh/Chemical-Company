package com.ns.chemcomp.dao;

import com.ns.chemcomp.DTO.Role;

import java.util.List;

public interface RoleDao {
    /**
     * Create a new account role
     *
     * @param role {Role} well formed obj
     * @return {Role} the created obj from db
     */
    Role createRole(Role role);

    /**
     * Retrieve a Role from db by its id
     *
     * @param id {int} a valid id
     * @return {Role} the obj from db, null for invalid id
     */
    Role readRoleById(int id);

    /**
     * Retrieve a Role from db by its name
     *
     * @param role {String} role name
     * @return {Role} the obj from db, null for invalid role
     */
    Role readRoleByRole(String role);

    /**
     * Retrieve all Roles from db
     *
     * @return {List} all objs in db
     */
    List<Role> readAllRoles();

    /**
     * Update an existing Role
     *
     * @param role {Role} an edited well formed obj with the corresponding id
     * @return {Role} the updated obj from db, null if failed
     */
    Role updateRole(Role role);

    /**
     * Delete a Role from db
     *
     * @param id {int} a valid id
     * @return {boolean} true if delete, false otherwise
     */
    boolean deleteRole(int id);
}
