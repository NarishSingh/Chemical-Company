package com.ns.chemcomp.service;

import com.ns.chemcomp.dao.*;
import com.ns.chemcomp.dto.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class OrderServiceTest {

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
    @Autowired
    CategoryDao cDao;
    @Autowired
    OrderService serv;

    static Role adminRole;
    static Role buyerRole;

    static User adminAcc;
    static User buyerAcc1;
    static User buyerAcc2;

    static State nyState;
    static State caState;
    static State flState;

    static Category c1;
    static Category c2;
    static Category c3;

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
            rDao.deleteRole(r.getRoleId());
        }

        for (User u : uDao.readAllUsers()) {
            uDao.deleteUser(u.getUserId());
        }

        for (State s : sDao.readAllStates()) {
            sDao.deleteState(s.getStateId());
        }

        for (Product p : pDao.readAllProducts()) {
            pDao.deleteProduct(p.getProductId());
        }

        for (Category c : cDao.readAllCategories()) {
            cDao.deleteCategory(c.getCategoryId());
        }

        for (Order o : oDao.readAllOrders()) {
            oDao.deleteOrder(o.getOrderId());
        }

        /*Create dependent obj's*/
        //roles with lists
        Role r1 = new Role();
        r1.setRole("ROLE_BUYER");
        buyerRole = rDao.createRole(r1);

        Role r2 = new Role();
        r2.setRole("ROLE_ADMIN");
        adminRole = rDao.createRole(r2);

        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(buyerRole);
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
        u1.setRoles(userRoles);
        buyerAcc1 = uDao.createUser(u1);

        User u2 = new User();
        u2.setUsername("TestUser2");
        u2.setPassword("password3");
        u2.setEnabled(false);
        u2.setName("Test Other User");
        u2.setPhone("555-555-5555");
        u2.setEmail("user02@mail.com");
        u2.setAddress("468-32 123th Blvd");
        u2.setRoles(userRoles);
        buyerAcc2 = uDao.createUser(u2);

        //States
        State s1 = new State();
        s1.setName("New York");
        s1.setAbbreviation("NY");
        s1.setTaxRate(new BigDecimal("1.25").setScale(2, RoundingMode.HALF_UP));
        nyState = sDao.createState(s1);

        State s2 = new State();
        s2.setName("California");
        s2.setAbbreviation("CA");
        s2.setTaxRate(new BigDecimal("2.00").setScale(2, RoundingMode.HALF_UP));
        caState = sDao.createState(s2);

        State s3 = new State();
        s3.setName("Florida");
        s3.setAbbreviation("FL");
        s3.setTaxRate(new BigDecimal("0.75").setScale(2, RoundingMode.HALF_UP));
        flState = sDao.createState(s3);

        //Categories
        Category category1 = new Category();
        category1.setCategoryName("Alcohol");

        Category category2 = new Category();
        category2.setCategoryName("Solution");

        Category category3 = new Category();
        category3.setCategoryName("Reagent");

        c1 = cDao.createCategory(category1);
        c2 = cDao.createCategory(category2);
        c3 = cDao.createCategory(category3);

        //Products
        Product p1 = new Product();
        p1.setName("Denatured Alcohol 100%");
        p1.setChemicalName("Ethanol");
        p1.setMassVolume(new BigDecimal("1").setScale(2, RoundingMode.HALF_UP));
        p1.setMeasurement("pt");
        p1.setUnitCost(new BigDecimal("20").setScale(2, RoundingMode.HALF_UP));
        p1.setHandlingCost(new BigDecimal("0.05").setScale(2, RoundingMode.HALF_UP));
        p1.setPhotoFilename(null);
        p1.setCategory(c1);
        alcohol = pDao.createProduct(p1);

        Product p2 = new Product();
        p2.setName("Lye 50% Solution");
        p2.setChemicalName("Sodium Hydroxide");
        p2.setMassVolume(new BigDecimal("1.00").setScale(2, RoundingMode.HALF_UP));
        p2.setMeasurement("L");
        p2.setUnitCost(new BigDecimal("26.00").setScale(2, RoundingMode.HALF_UP));
        p2.setHandlingCost(new BigDecimal("0.10").setScale(2, RoundingMode.HALF_UP));
        p2.setPhotoFilename(null);
        p2.setCategory(c2);
        lye = pDao.createProduct(p2);

        Product p3 = new Product();
        p3.setName("Glycerol Reagent");
        p3.setChemicalName("Glycerin");
        p3.setMassVolume(new BigDecimal("30.00").setScale(2, RoundingMode.HALF_UP));
        p3.setMeasurement("ml");
        p3.setUnitCost(new BigDecimal("12.50").setScale(2, RoundingMode.HALF_UP));
        p3.setHandlingCost(new BigDecimal("0.25").setScale(2, RoundingMode.HALF_UP));
        p3.setPhotoFilename(null);
        p3.setCategory(c3);
        glycerol = pDao.createProduct(p3);

        /*setup Orders using partial ctor*/
        o1 = new Order(LocalDate.now(), 1, adminAcc, nyState, alcohol);
        o2 = new Order(LocalDate.now().plusMonths(1), 3, adminAcc, nyState, glycerol);
        o3 = new Order(LocalDate.now(), 20, buyerAcc1, caState, lye);
        o4 = new Order(LocalDate.now().plusWeeks(3), 100, buyerAcc2, flState, glycerol);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createOrderReadOrderById() {
        final BigDecimal testNetPrice = new BigDecimal(String.valueOf(o1.getQuantity()))
                .multiply(o1.getProduct().getUnitCost())
                .add(o1.getProduct().getHandlingCost())
                .setScale(2, RoundingMode.HALF_UP);
        final BigDecimal testTax = o1.getState().getTaxRate().divide(new BigDecimal("100"), RoundingMode.HALF_UP)
                .multiply(testNetPrice)
                .setScale(2, RoundingMode.HALF_UP);
        final BigDecimal testTotal = testNetPrice.add(testTax)
                .setScale(2, RoundingMode.HALF_UP);

        Order order = serv.createOrder(o1);

        Order fromDao = null;
        try {
            fromDao = serv.readOrderById(order.getOrderId());
        } catch (DataAccessException e) {
            fail("Valid order");
        }

        assertEquals(fromDao, order);
        assertNotNull(order.getNetPrice());
        assertEquals(order.getNetPrice(), testNetPrice);
        assertNotNull(order.getTax());
        assertEquals(order.getTax(), testTax);
        assertNotNull(order.getTotal());
        assertEquals(order.getTotal(), testTotal);
    }

    @Test
    void readOrderByIdFail() {
        Order order = serv.createOrder(o1);
        Order notFromDao = serv.readOrderById(0);

        assertNull(notFromDao);
        assertNotEquals(order, notFromDao);
    }

    @Test
    void readOrdersByDate() {
        final LocalDate testNow = LocalDate.now();

        Order order1 = serv.createOrder(o1);
        Order order2 = serv.createOrder(o2);
        Order order3 = serv.createOrder(o3);
        Order order4 = serv.createOrder(o4);

        List<Order> fromToday = new ArrayList<>();
        try {
            fromToday = serv.readOrdersByDate(testNow);
        } catch (NoOrdersOnDateException e) {
            fail("Valid date for read");
        }

        assertNotNull(fromToday);
        assertEquals(2, fromToday.size());
        assertTrue(fromToday.contains(order1));
        assertFalse(fromToday.contains(order2));
        assertTrue(fromToday.contains(order3));
        assertFalse(fromToday.contains(order4));
    }

    @Test
    void readOrdersByDateFail() {
        final LocalDate badDate = LocalDate.parse("9999-12-31"); //must use MySQL's max date

        Order order1 = serv.createOrder(o1);
        Order order2 = serv.createOrder(o2);
        Order order3 = serv.createOrder(o3);
        Order order4 = serv.createOrder(o4);

        try {
            List<Order> fromEndOfTime = serv.readOrdersByDate(badDate);
            fail("Should fail, no orders on MAX date");
        } catch (NoOrdersOnDateException e) {
            return;
        }
    }

    @Test
    void readOrdersByProduct() {
        Order order1 = serv.createOrder(o1);
        Order order2 = serv.createOrder(o2);
        Order order3 = serv.createOrder(o3);
        Order order4 = serv.createOrder(o4);

        List<Order> glyOrders = new ArrayList<>();
        try {
            glyOrders = serv.readOrdersByProduct(glycerol);
        } catch (NoOrdersForProductException e) {
            fail("Valid product for read");
        }

        assertNotNull(glyOrders);
        assertEquals(2, glyOrders.size());
        assertFalse(glyOrders.contains(order1));
        assertTrue(glyOrders.contains(order2));
        assertFalse(glyOrders.contains(order3));
        assertTrue(glyOrders.contains(order4));
    }

    @Test
    void readOrdersByProductFail() {
        final Product badProduct = new Product();

        Order order1 = serv.createOrder(o1);
        Order order2 = serv.createOrder(o2);
        Order order3 = serv.createOrder(o3);
        Order order4 = serv.createOrder(o4);

        try {
            List<Order> badOrders = serv.readOrdersByProduct(badProduct);
            fail("Should fail, no orders with product/product not in db");
        } catch (NoOrdersForProductException e) {
            return;
        }
    }

    @Test
    void readOrdersByUser() {
        Order order1 = serv.createOrder(o1);
        Order order2 = serv.createOrder(o2);
        Order order3 = serv.createOrder(o3);
        Order order4 = serv.createOrder(o4);

        List<Order> adminOrders = new ArrayList<>();
        try {
            adminOrders = serv.readOrdersByUser(adminAcc);
        } catch (NoOrdersForUserException e) {
            fail("Valid user for read");
        }

        assertNotNull(adminOrders);
        assertEquals(2, adminOrders.size());
        assertTrue(adminOrders.contains(order1));
        assertTrue(adminOrders.contains(order2));
        assertFalse(adminOrders.contains(order3));
        assertFalse(adminOrders.contains(order4));
    }

    @Test
    void readOrdersByUserFail() {
        final User badUser = new User();

        Order order1 = serv.createOrder(o1);
        Order order2 = serv.createOrder(o2);
        Order order3 = serv.createOrder(o3);
        Order order4 = serv.createOrder(o4);

        try {
            List<Order> badOrders = serv.readOrdersByUser(badUser);
            fail("Should fail, no orders for user/user not in db");
        } catch (NoOrdersForUserException e) {
            return;
        }
    }

    @Test
    void readOrdersByState() {
        Order order1 = serv.createOrder(o1);
        Order order2 = serv.createOrder(o2);
        Order order3 = serv.createOrder(o3);
        Order order4 = serv.createOrder(o4);

        List<Order> nyOrders = new ArrayList<>();
        try {
            nyOrders = serv.readOrdersByState(nyState);
        } catch (NoOrdersInStateException e) {
            fail("Valid state for read");
        }

        assertNotNull(nyOrders);
        assertEquals(2, nyOrders.size());
        assertTrue(nyOrders.contains(order1));
        assertTrue(nyOrders.contains(order2));
        assertFalse(nyOrders.contains(order3));
        assertFalse(nyOrders.contains(order4));
    }

    @Test
    void readOrdersByStateFail() {
        final State badState = new State();

        Order order1 = serv.createOrder(o1);
        Order order2 = serv.createOrder(o2);
        Order order3 = serv.createOrder(o3);
        Order order4 = serv.createOrder(o4);

        try {
            List<Order> badOrders = serv.readOrdersByState(badState);
            fail("Should fail, no orders for state/state not in db");
        } catch (NoOrdersInStateException e) {
            return;
        }
    }

    @Test
    void readAllOrders() {
        Order order1 = serv.createOrder(o1);
        Order order2 = serv.createOrder(o2);
        Order order3 = serv.createOrder(o3);
        Order order4 = serv.createOrder(o4);

        List<Order> orders = serv.readAllOrders();

        assertNotNull(orders);
        assertEquals(4, orders.size());
        assertTrue(orders.contains(order1));
        assertTrue(orders.contains(order2));
        assertTrue(orders.contains(order3));
        assertTrue(orders.contains(order4));
    }

    @Test
    void updateOrder() {
        Order order1 = serv.createOrder(o1);
        Order original = serv.readOrderById(order1.getOrderId());

        order1.setProduct(lye);
        Order update = serv.updateOrder(order1);

        assertNotNull(original);
        assertNotNull(update);
        assertEquals(order1, update);
        assertNotEquals(order1, original);
        assertNotEquals(original, update);
    }

    @Test
    void deleteOrder() {
        Order order1 = serv.createOrder(o1);
        Order order2 = serv.createOrder(o2);
        Order order3 = serv.createOrder(o3);
        Order order4 = serv.createOrder(o4);
        List<Order> original = serv.readAllOrders();

        boolean deleted = serv.deleteOrder(order4.getOrderId());
        List<Order> afterDel = serv.readAllOrders();

        assertNotNull(original);
        assertNotNull(afterDel);
        assertEquals(4, original.size());
        assertNotEquals(original, afterDel);
        assertEquals(3, afterDel.size());
        assertTrue(deleted);
        assertTrue(afterDel.contains(order1));
        assertTrue(afterDel.contains(order2));
        assertTrue(afterDel.contains(order3));
        assertFalse(afterDel.contains(order4));
    }
}