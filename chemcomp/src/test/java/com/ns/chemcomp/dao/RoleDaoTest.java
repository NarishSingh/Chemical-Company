package com.ns.chemcomp.dao;

import com.ns.chemcomp.dto.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class RoleDaoTest {

    @Autowired
    RoleDao rDao;

    static Role r1; //user
    static Role r2; //admin

    @BeforeEach
    void setUp() {
        /*clean db*/
        for (Role r : rDao.readAllRoles()) {
            rDao.deleteRole(r.getId());
        }

        /*setup roles*/
        r1 = new Role();
        r1.setRole("ROLE_USER");

        r2 = new Role();
        r2.setRole("ROLE_ADMIN");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createRoleReadById() {
        Role role1 = rDao.createRole(r1);
        Role role2 = rDao.createRole(r2);

        Role fromDao1 = rDao.readRoleById(role1.getId());
        Role fromDao2 = rDao.readRoleById(role2.getId());

        assertNotNull(role1);
        assertNotNull(role2);
        assertNotNull(fromDao1);
        assertNotNull(fromDao2);
        assertEquals(role1, fromDao1);
        assertEquals(role2, fromDao2);
    }

    @Test
    void readRoleByRole() {
        Role role1 = rDao.createRole(r1);
        Role role2 = rDao.createRole(r2);

        Role fromDao1 = rDao.readRoleByRole("ROLE_USER");
        Role fromDao2 = rDao.readRoleByRole("ROLE_ADMIN");

        assertNotNull(fromDao1);
        assertNotNull(fromDao2);
        assertEquals(role1, fromDao1);
        assertEquals(role2, fromDao2);
    }

    @Test
    void readAllRoles() {
        Role role1 = rDao.createRole(r1);
        Role role2 = rDao.createRole(r2);

        List<Role> roles = rDao.readAllRoles();

        assertNotNull(roles);
        assertEquals(2, roles.size());
        assertTrue(roles.contains(role1));
        assertTrue(roles.contains(role2));
    }

    @Test
    void updateRole() {
        Role role1 = rDao.createRole(r1);
        Role original = rDao.readRoleById(role1.getId());

        role1.setRole("ROLE_QUALITY_TESTER");
        Role edit = rDao.updateRole(role1);

        assertNotNull(original);
        assertNotNull(edit);
        assertNotEquals(role1, original);
        assertEquals(role1, edit);
        assertNotEquals(original, edit);
    }

    @Test
    void deleteRole() {
        Role role1 = rDao.createRole(r1);
        Role role2 = rDao.createRole(r2);
        List<Role> originalRoles = rDao.readAllRoles();

        boolean deleted = rDao.deleteRole(role1.getId());
        List<Role> afterDel = rDao.readAllRoles();

        assertNotNull(originalRoles);
        assertEquals(2, originalRoles.size());
        assertTrue(originalRoles.contains(role1));
        assertTrue(originalRoles.contains(role2));
        assertNotNull(afterDel);
        assertTrue(deleted);
        assertEquals(1, afterDel.size());
        assertFalse(afterDel.contains(role1));
        assertTrue(afterDel.contains(role2));
    }
}