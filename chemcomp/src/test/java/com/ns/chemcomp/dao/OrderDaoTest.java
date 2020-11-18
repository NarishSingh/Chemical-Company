package com.ns.chemcomp.dao;

import com.ns.chemcomp.dto.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
class OrderDaoTest {

    @Autowired
    RoleDao rDao;
    @Autowired
    UserDao uDao;
    @Autowired
    StateDao sDao;
    @Autowired
    ProductDao pDao;
    @Autowired
    OrderDao oDao;

    static Role adminRole;
    static Role userRole;

    static User adminAcc;
    static User userAcc1;
    static User userAcc2;

    static State nyState;
    static State caState;
    static State flState;

    static Product alcohol;
    static Product lye;
    static Product glycerol;

    static Order o1;
    static Order o2;
    static Order o3;
    static Order o4;

    @BeforeEach
    void setUp() {
        /*clean db*/
        for (Role r : rDao.readAllRoles()) {
            rDao.deleteRole(r.getId());
        }

        for (User u : uDao.readAllUsers()) {
            uDao.deleteUser(u.getId());
        }

        for (State s : sDao.readAllStates()) {
            sDao.deleteState(s.getId());
        }

        for (Product p : pDao.readAllProducts()) {
            pDao.deleteProduct(p.getId());
        }

        for (Order o : oDao.readAllOrders()) {
            oDao.deleteOrder(o.getId());
        }

        /*Create dependent obj's*/
        //roles with lists
        Role r1 = new Role();
        r1.setRole("ROLE_USER");
        userRole = rDao.createRole(r1);

        Role r2 = new Role();
        r2.setRole("ROLE_ADMIN");
        adminRole = rDao.createRole(r2);

        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(userRole);
        adminRoles.add(adminRole);

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(adminRole);

        //users
        User adm = new User();
        adm.setUsername("TestAdmin");
        adm.setPassword("password1");
        adm.setEnabled(true);
        adm.setName("Test Admin");
        adm.setPhone("555-555-5555");
        adm.setEmail("theAdmin@mail.com");
        adm.setAddress("123-45 678th St");
        adm.setPhotoFilename(null);
        adm.setRoles(adminRoles);
        adminAcc = uDao.createUser(adm);

        User u1 = new User();
        u1.setUsername("TestUser1");
        u1.setPassword("password2");
        u1.setEnabled(true);
        u1.setName("Test User");
        u1.setPhone("555-555-5555");
        u1.setEmail("user01@mail.com");
        u1.setAddress("987-65 321st ave");
        u1.setPhotoFilename(null);
        u1.setRoles(userRoles);
        userAcc1 = uDao.createUser(u1);

        User u2 = new User();
        u2.setUsername("TestUser2");
        u2.setPassword("password3");
        u2.setEnabled(false);
        u2.setName("Test Other User");
        u2.setPhone("555-555-5555");
        u2.setEmail("user02@mail.com");
        u2.setAddress("468-32 123th Blvd");
        u2.setPhotoFilename(null);
        u2.setRoles(userRoles);
        userAcc2 = uDao.createUser(u2);

        //States
        State s1 = new State();
        s1.setName("New York");
        s1.setAbbreviation("NY");
        s1.setTaxRate(new BigDecimal("1.25"));
        nyState = sDao.createState(s1);

        State s2 = new State();
        s2.setName("California");
        s2.setAbbreviation("CA");
        s2.setTaxRate(new BigDecimal("2.00"));
        caState = sDao.createState(s2);

        State s3 = new State();
        s3.setName("Florida");
        s3.setAbbreviation("FL");
        s3.setTaxRate(new BigDecimal("0.75"));
        flState = sDao.createState(s3);

        //Products
        Product p1 = new Product();
        p1.setName("Denatured Alcohol 100%");
        p1.setChemicalName("Ethanol");
        p1.setMeasurement("pt");
        p1.setUnitCost(new BigDecimal("20.00"));
        p1.setHandlingCost(new BigDecimal("0.05"));
        p1.setPhotoFilename(null);
        alcohol = pDao.createProduct(p1);

        Product p2 = new Product();
        p2.setName("Lye 50% Solution");
        p2.setChemicalName("Sodium Hydroxide");
        p2.setMeasurement("L");
        p2.setUnitCost(new BigDecimal("26.00"));
        p2.setHandlingCost(new BigDecimal("0.10"));
        p2.setPhotoFilename(null);
        lye = pDao.createProduct(p2);

        Product p3 = new Product();
        p3.setName("Glycerol Reagant");
        p3.setChemicalName("Glycerin");
        p3.setMeasurement("L");
        p3.setUnitCost(new BigDecimal("12.50"));
        p3.setHandlingCost(new BigDecimal("0.25"));
        p3.setPhotoFilename(null);
        glycerol = pDao.createProduct(p3);

        /*setup Orders*/
        o1 = new Order();
        o1.setUser(adminAcc);
        o1.setState(nyState);
        o1.setProduct(alcohol);
        o1.setOrderDate(LocalDate.now());
        int o1quan = 1;
        o1.setQuantity(o1quan);
        o1.setNetPrice(new BigDecimal(o1quan).multiply(alcohol.getUnitCost()));
        o1.setTax(o1.getNetPrice().multiply(nyState.getTaxRate()));
        o1.setTotal(o1.getNetPrice().add(o1.getTax()));


        o2 = new Order();
        o2.setUser(adminAcc);
        o2.setState(nyState);
        o2.setProduct(glycerol);
        o2.setOrderDate(LocalDate.now().plusMonths(1));
        int o2quan = 3;
        o2.setQuantity(o2quan);
        o2.setNetPrice(new BigDecimal(o2quan).multiply(glycerol.getUnitCost()));
        o2.setTax(o2.getNetPrice().multiply(nyState.getTaxRate()));
        o2.setTotal(o2.getNetPrice().add(o2.getTax()));

        o3 = new Order();
        o3.setUser(userAcc1);
        o3.setState(caState);
        o3.setProduct(lye);
        o3.setOrderDate(LocalDate.now());
        int o3quan = 20;
        o3.setQuantity(o3quan);
        o3.setNetPrice(new BigDecimal(o3quan).multiply(lye.getUnitCost()));
        o3.setTax(o3.getNetPrice().multiply(caState.getTaxRate()));
        o3.setTotal(o3.getNetPrice().add(o3.getTax()));

        o4 = new Order();
        o4.setUser(userAcc2);
        o4.setState(flState);
        o4.setProduct(glycerol);
        o4.setOrderDate(LocalDate.now().plusWeeks(3));
        int o4quan = 100;
        o4.setQuantity(o4quan);
        o4.setNetPrice(new BigDecimal(o4quan).multiply(glycerol.getUnitCost()));
        o4.setTax(o4.getNetPrice().multiply(flState.getTaxRate()));
        o4.setTotal(o4.getNetPrice().add(o4.getTax()));

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createOrderReadOrderById() {
    }

    @Test
    void readOrderByDate() {
    }

    @Test
    void readOrdersByProduct() {
    }

    @Test
    void readOrdersByUser() {
    }

    @Test
    void readOrdersByState() {
    }

    @Test
    void readAllOrders() {
    }

    @Test
    void updateOrder() {
    }

    @Test
    void deleteOrder() {
    }
}