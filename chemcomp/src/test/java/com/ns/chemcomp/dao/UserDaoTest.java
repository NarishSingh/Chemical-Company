package com.ns.chemcomp.dao;

import com.ns.chemcomp.dto.Role;
import com.ns.chemcomp.dto.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserDaoTest {

    @Autowired
    RoleDao rDao;
    @Autowired
    UserDao uDao;

    static Role role1;
    static Role role2;
    static User adm;
    static User u1;
    static User u2;

    @BeforeEach
    void setUp() {
        /*clean db*/
        for (Role r : rDao.readAllRoles()) {
            rDao.deleteRole(r.getId());
        }

        for (User u : uDao.readAllUsers()) {
            uDao.deleteUser(u.getId());
        }

        /*create roles and sets*/
        Role r1 = new Role();
        r1.setRole("ROLE_USER");
        role1 = rDao.createRole(r1);

        Role r2 = new Role();
        r2.setRole("ROLE_ADMIN");
        role2 = rDao.createRole(r2);

        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(role1);
        adminRoles.add(role2);

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role2);

        /*setup users*/
        adm = new User();
        adm.setUsername("TestAdmin");
        adm.setPassword("password1");
        adm.setEnabled(true);
        adm.setName("Test Admin");
        adm.setPhone("555-555-5555");
        adm.setEmail("theAdmin@mail.com");
        adm.setAddress("123-45 678th St");
        adm.setPhotoFilename(null);
        adm.setRoles(adminRoles);

        u1 = new User();
        u1.setUsername("TestUser1");
        u1.setPassword("password2");
        u1.setEnabled(true);
        u1.setName("Test User");
        u1.setPhone("555-555-5555");
        u1.setEmail("user01@mail.com");
        u1.setAddress("987-65 321st ave");
        u1.setPhotoFilename(null);
        u1.setRoles(userRoles);

        u2 = new User();
        u2.setUsername("TestUser2");
        u2.setPassword("password3");
        u2.setEnabled(false);
        u2.setName("Test Other User");
        u2.setPhone("555-555-5555");
        u2.setEmail("user02@mail.com");
        u2.setAddress("468-32 123th Blvd");
        u2.setPhotoFilename(null);
        u2.setRoles(userRoles);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createUserReadUserById() {
        User admin = uDao.createUser(adm);
        User user1 = uDao.createUser(u1);
        User user2 = uDao.createUser(u2);

        User adminFromDao = uDao.readUserById(admin.getId());
        User user1FromDao = uDao.readUserById(user1.getId());
        User user2FromDao = uDao.readUserById(user2.getId());

        assertNotNull(admin);
        assertNotNull(user1);
        assertNotNull(user2);
        assertNotNull(adminFromDao);
        assertNotNull(user1FromDao);
        assertNotNull(user2FromDao);
        assertEquals(admin, adminFromDao);
        assertEquals(user1, user1FromDao);
        assertEquals(user2, user2FromDao);
    }

    @Test
    void readUserByUsername() {
        User admin = uDao.createUser(adm);
        User user1 = uDao.createUser(u1);
        User user2 = uDao.createUser(u2);

        User adminFromDao = uDao.readUserByUsername("TestAdmin");
        User user1FromDao = uDao.readUserByUsername("TestUser1");
        User user2FromDao = uDao.readUserByUsername("TestUser2");

        assertNotNull(adminFromDao);
        assertNotNull(user1FromDao);
        assertNotNull(user2FromDao);
        assertEquals(admin, adminFromDao);
        assertEquals(user1, user1FromDao);
        assertEquals(user2, user2FromDao);
    }

    @Test
    void readEnabledUsersById() {
        User admin = uDao.createUser(adm);
        User user1 = uDao.createUser(u1);
        User user2 = uDao.createUser(u2);

        List<User> enabled = uDao.readEnabledUsers();

        assertNotNull(enabled);
        assertEquals(2, enabled.size());
        assertTrue(enabled.contains(admin));
        assertTrue(enabled.contains(user1));
        assertFalse(enabled.contains(user2));
    }

    @Test
    void readEnabledUserByUsername() {
        User admin = uDao.createUser(adm);
        User user1 = uDao.createUser(u1);
        User user2 = uDao.createUser(u2);

        User enAdm = uDao.readEnabledUserByUsername(admin.getUsername());
        User enU1 = uDao.readEnabledUserByUsername(user1.getUsername());
        User disabled = uDao.readEnabledUserByUsername(user2.getUsername());

        assertNotNull(enAdm);
        assertNotNull(enU1);
        assertNull(disabled);
        assertEquals(admin, enAdm);
        assertEquals(user1, enU1);
        assertNotEquals(user2, disabled);
    }

    @Test
    void readAllUsers() {
        User admin = uDao.createUser(adm);
        User user1 = uDao.createUser(u1);
        User user2 = uDao.createUser(u2);

        List<User> users = uDao.readAllUsers();

        assertNotNull(users);
        assertEquals(3, users.size());
        assertTrue(users.contains(admin));
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void updateUser() {
        User user1 = uDao.createUser(u1);
        User original = uDao.readUserById(user1.getId());

        Set<Role> newRoles = new HashSet<>();
        newRoles.add(role1);
        newRoles.add(role2);
        user1.setName("Test Edit");
        user1.setEmail("edited@mail.com");
        user1.setEnabled(false);
        user1.setRoles(newRoles);
        User editedUser = uDao.updateUser(user1);
        User afterEdit = uDao.readUserById(user1.getId());

        assertNotNull(original);
        assertNotNull(editedUser);
        assertNotNull(afterEdit);
        assertEquals(editedUser, afterEdit);
        assertNotEquals(editedUser, original);
    }

    @Test
    void deleteUser() {
        User admin = uDao.createUser(adm);
        User user1 = uDao.createUser(u1);
        User user2 = uDao.createUser(u2);
        List<User> original = uDao.readAllUsers();

        boolean deleted = uDao.deleteUser(user1.getId());
        List<User> afterDel = uDao.readAllUsers();

        assertNotNull(original);
        assertNotNull(afterDel);
        assertTrue(deleted);
        assertEquals(2, afterDel.size());
        assertTrue(afterDel.contains(admin));
        assertFalse(afterDel.contains(user1));
        assertTrue(afterDel.contains(user2));
    }
}