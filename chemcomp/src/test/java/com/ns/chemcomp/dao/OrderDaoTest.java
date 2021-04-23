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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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
    @Autowired
    CategoryDao cDao;

    static Role adminRole;
    static Role userRole;

    static User adminAcc;
    static User userAcc1;
    static User userAcc2;

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
        p3.setName("Glycerol Reagant");
        p3.setChemicalName("Glycerin");
        p3.setMassVolume(new BigDecimal("30.00").setScale(2, RoundingMode.HALF_UP));
        p3.setMeasurement("ml");
        p3.setUnitCost(new BigDecimal("12.50").setScale(2, RoundingMode.HALF_UP));
        p3.setHandlingCost(new BigDecimal("0.25").setScale(2, RoundingMode.HALF_UP));
        p3.setPhotoFilename(null);
        p3.setCategory(c3);
        glycerol = pDao.createProduct(p3);

        /*setup Orders*/
        o1 = new Order();
        o1.setUser(adminAcc);
        o1.setState(nyState);
        o1.setProduct(alcohol);
        o1.setOrderDate(LocalDate.now());
        int o1quan = 1;
        o1.setQuantity(o1quan);
        o1.setNetPrice(new BigDecimal(o1quan).multiply(alcohol.getUnitCost()).setScale(2, RoundingMode.HALF_UP));
        o1.setTax(o1.getNetPrice().multiply(nyState.getTaxRate()).setScale(2, RoundingMode.HALF_UP));
        o1.setTotal(o1.getNetPrice().add(o1.getTax()).setScale(2, RoundingMode.HALF_UP));


        o2 = new Order();
        o2.setUser(adminAcc);
        o2.setState(nyState);
        o2.setProduct(glycerol);
        o2.setOrderDate(LocalDate.now().plusMonths(1));
        int o2quan = 3;
        o2.setQuantity(o2quan);
        o2.setNetPrice(new BigDecimal(o2quan).multiply(glycerol.getUnitCost()).setScale(2, RoundingMode.HALF_UP));
        o2.setTax(o2.getNetPrice().multiply(nyState.getTaxRate()).setScale(2, RoundingMode.HALF_UP));
        o2.setTotal(o2.getNetPrice().add(o2.getTax()).setScale(2, RoundingMode.HALF_UP));

        o3 = new Order();
        o3.setUser(userAcc1);
        o3.setState(caState);
        o3.setProduct(lye);
        o3.setOrderDate(LocalDate.now());
        int o3quan = 20;
        o3.setQuantity(o3quan);
        o3.setNetPrice(new BigDecimal(o3quan).multiply(lye.getUnitCost()).setScale(2, RoundingMode.HALF_UP));
        o3.setTax(o3.getNetPrice().multiply(caState.getTaxRate()).setScale(2, RoundingMode.HALF_UP));
        o3.setTotal(o3.getNetPrice().add(o3.getTax()).setScale(2, RoundingMode.HALF_UP));

        o4 = new Order();
        o4.setUser(userAcc2);
        o4.setState(flState);
        o4.setProduct(glycerol);
        o4.setOrderDate(LocalDate.now().plusWeeks(3));
        int o4quan = 100;
        o4.setQuantity(o4quan);
        o4.setNetPrice(new BigDecimal(o4quan).multiply(glycerol.getUnitCost()).setScale(2, RoundingMode.HALF_UP));
        o4.setTax(o4.getNetPrice().multiply(flState.getTaxRate()).setScale(2, RoundingMode.HALF_UP));
        o4.setTotal(o4.getNetPrice().add(o4.getTax()).setScale(2, RoundingMode.HALF_UP));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createOrderReadOrderById() {
        Order order1 = oDao.createOrder(o1);
        Order order2 = oDao.createOrder(o2);
        Order order3 = oDao.createOrder(o3);
        Order order4 = oDao.createOrder(o4);

        Order dao1 = oDao.readOrderById(order1.getOrderId());
        Order dao2 = oDao.readOrderById(order2.getOrderId());
        Order dao3 = oDao.readOrderById(order3.getOrderId());
        Order dao4 = oDao.readOrderById(order4.getOrderId());

        assertNotNull(order1);
        assertNotNull(order2);
        assertNotNull(order3);
        assertNotNull(order4);
        assertNotNull(dao1);
        assertNotNull(dao2);
        assertNotNull(dao3);
        assertNotNull(dao4);
        assertEquals(order1, dao1);
        assertEquals(order2, dao2);
        assertEquals(order3, dao3);
        assertEquals(order4, dao4);
    }

    @Test
    void readOrderByDate() {
        Order order1 = oDao.createOrder(o1);
        Order order2 = oDao.createOrder(o2);
        Order order3 = oDao.createOrder(o3);
        Order order4 = oDao.createOrder(o4);

        List<Order> daoToday = oDao.readOrderByDate(LocalDate.now()); //o1 and o3
        List<Order> dao1mo = oDao.readOrderByDate(order2.getOrderDate()); //o2
        List<Order> dao3wks = oDao.readOrderByDate(order4.getOrderDate()); //o4

        assertNotNull(daoToday);
        assertNotNull(dao1mo);
        assertNotNull(dao3wks);
        assertEquals(2, daoToday.size());
        assertTrue(daoToday.contains(order1));
        assertTrue(daoToday.contains(order3));
        assertEquals(1, dao1mo.size());
        assertTrue(dao1mo.contains(order2));
        assertEquals(1, dao3wks.size());
        assertTrue(dao3wks.contains(order4));
    }

    @Test
    void readOrdersByProduct() {
        Order order1 = oDao.createOrder(o1);
        Order order2 = oDao.createOrder(o2);
        Order order3 = oDao.createOrder(o3);
        Order order4 = oDao.createOrder(o4);

        List<Order> daoAlcohol = oDao.readOrdersByProduct(alcohol); //o1
        List<Order> daoLye = oDao.readOrdersByProduct(lye); //o3
        List<Order> daoGlycerol = oDao.readOrdersByProduct(glycerol); //o2 and o4

        assertNotNull(daoAlcohol);
        assertNotNull(daoLye);
        assertNotNull(daoGlycerol);
        assertEquals(1, daoAlcohol.size());
        assertTrue(daoAlcohol.contains(order1));
        assertEquals(1, daoLye.size());
        assertTrue(daoLye.contains(order3));
        assertEquals(2, daoGlycerol.size());
        assertTrue(daoGlycerol.contains(order2));
        assertTrue(daoGlycerol.contains(order4));
    }

    @Test
    void readOrdersByUser() {
        Order order1 = oDao.createOrder(o1);
        Order order2 = oDao.createOrder(o2);
        Order order3 = oDao.createOrder(o3);
        Order order4 = oDao.createOrder(o4);

        List<Order> adminOrders = oDao.readOrdersByUser(adminAcc); //o1 o2
        List<Order> user1Orders = oDao.readOrdersByUser(userAcc1); //o3
        List<Order> user2Orders = oDao.readOrdersByUser(userAcc2); //o4

        assertNotNull(adminOrders);
        assertNotNull(user1Orders);
        assertNotNull(user2Orders);
        assertEquals(2, adminOrders.size());
        assertTrue(adminOrders.contains(order1));
        assertTrue(adminOrders.contains(order2));
        assertEquals(1, user1Orders.size());
        assertTrue(user1Orders.contains(order3));
        assertEquals(1, user2Orders.size());
        assertTrue(user2Orders.contains(order4));
    }

    @Test
    void readOrdersByState() {
        Order order1 = oDao.createOrder(o1);
        Order order2 = oDao.createOrder(o2);
        Order order3 = oDao.createOrder(o3);
        Order order4 = oDao.createOrder(o4);

        List<Order> nyOrders = oDao.readOrdersByState(nyState);
        List<Order> caOrders = oDao.readOrdersByState(caState);
        List<Order> flOrders = oDao.readOrdersByState(flState);

        assertNotNull(nyOrders);
        assertNotNull(caOrders);
        assertNotNull(flOrders);
        assertEquals(2, nyOrders.size());
        assertTrue(nyOrders.contains(order1));
        assertTrue(nyOrders.contains(order2));
        assertEquals(1, caOrders.size());
        assertTrue(caOrders.contains(order3));
        assertEquals(1, flOrders.size());
        assertTrue(flOrders.contains(order4));
    }

    @Test
    void readAllOrders() {
        Order order1 = oDao.createOrder(o1);
        Order order2 = oDao.createOrder(o2);
        Order order3 = oDao.createOrder(o3);
        Order order4 = oDao.createOrder(o4);

        List<Order> orders = oDao.readAllOrders();

        assertEquals(4, orders.size());
        assertTrue(orders.contains(order1));
        assertTrue(orders.contains(order2));
        assertTrue(orders.contains(order3));
        assertTrue(orders.contains(order4));
    }

    @Test
    void updateOrder() {
        Order order1 = oDao.createOrder(o1);
        Order original = oDao.readOrderById(order1.getOrderId());

        order1.setQuantity(50);
        order1.setNetPrice(new BigDecimal(50).multiply(alcohol.getUnitCost()).setScale(2, RoundingMode.HALF_UP));
        order1.setTax(order1.getNetPrice().multiply(nyState.getTaxRate()).setScale(2, RoundingMode.HALF_UP));
        order1.setTotal(order1.getNetPrice().add(order1.getTax()).setScale(2, RoundingMode.HALF_UP));
        Order edit = oDao.updateOrder(order1);

        assertNotNull(original);
        assertNotNull(edit);
        assertEquals(order1, edit);
        assertNotEquals(original, edit);
    }

    @Test
    void deleteOrder() {
        Order order1 = oDao.createOrder(o1);
        Order order2 = oDao.createOrder(o2);
        Order order3 = oDao.createOrder(o3);
        Order order4 = oDao.createOrder(o4);
        List<Order> original = oDao.readAllOrders();

        boolean deleted = oDao.deleteOrder(order4.getOrderId());
        List<Order> afterDel = oDao.readAllOrders();

        assertNotNull(original);
        assertNotNull(afterDel);
        assertTrue(deleted);
        assertEquals(4, original.size());
        assertEquals(3, afterDel.size());
        assertTrue(afterDel.contains(order1));
        assertTrue(afterDel.contains(order2));
        assertTrue(afterDel.contains(order3));
        assertFalse(afterDel.contains(order4));
    }
}
