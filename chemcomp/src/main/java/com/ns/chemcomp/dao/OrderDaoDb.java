package com.ns.chemcomp.dao;

import com.ns.chemcomp.dao.RoleDaoDb.RoleMapper;
import com.ns.chemcomp.dao.StateDaoDb.StateMapper;
import com.ns.chemcomp.dao.UserDaoDb.UserMapper;
import com.ns.chemcomp.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class OrderDaoDb implements OrderDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public Order createOrder(Order order) {
        String insert = "INSERT INTO `order` (orderDate, quantity, netPrice, tax, total, userId) " +
                "VALUES(?,?,?,?,?,?);";
        jdbc.update(insert,
                order.getOrderDate(),
                order.getQuantity(),
                order.getNetPrice(),
                order.getTax(),
                order.getTotal(),
                order.getUser().getUserId());

        //grab id
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID();", Integer.class);
        order.setOrderId(newId);

        //update bridge tables
        insertOrderState(order);
        insertOrderProduct(order);

        return order;
    }

    @Override
    public Order readOrderById(int id) {
        try {
            String read = "SELECT * FROM `order` " +
                    "WHERE orderId = ?;";
            Order order = jdbc.queryForObject(read, new OrderMapper(), id);
            associateOrderUser(order);
            associateOrderState(order);
            associateOrderProduct(order);

            return order;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Order> readOrderByDate(LocalDate date) {
        String readByDate = "SELECT * FROM `order` " +
                "WHERE orderDate = ?;";
        List<Order> orders = jdbc.query(readByDate, new OrderMapper(), date);
        for (Order order : orders) {
            associateOrderUser(order);
            associateOrderState(order);
            associateOrderProduct(order);
        }

        return orders;
    }

    @Override
    public List<Order> readOrdersByProduct(Product product) {
        String readByProduct = "SELECT o.* FROM `order` o " +
                "JOIN orderProduct op ON op.orderId = o.orderId " +
                "WHERE op.productId = ?;";
        List<Order> orders = jdbc.query(readByProduct, new OrderMapper(), product.getProductId());
        for (Order order : orders) {
            associateOrderUser(order);
            associateOrderState(order);
            associateOrderProduct(order);
        }

        return orders;
    }

    @Override
    public List<Order> readOrdersByUser(User user) {
        String readByUser = "SELECT o.* FROM `order` o " +
                "JOIN user u ON u.userId = o.userId " +
                "WHERE u.userId = ?;";
        List<Order> orders = jdbc.query(readByUser, new OrderMapper(), user.getUserId());
        for (Order order : orders) {
            associateOrderUser(order);
            associateOrderState(order);
            associateOrderProduct(order);
        }

        return orders;
    }

    @Override
    public List<Order> readOrdersByState(State state) {
        String readByState = "SELECT o.* FROM `order` o " +
                "JOIN orderState os ON os.orderId = o.orderId " +
                "JOIN state s ON s.stateId = os.stateId " +
                "WHERE s.stateId = ?;";
        List<Order> orders = jdbc.query(readByState, new OrderMapper(), state.getStateId());
        for (Order order : orders) {
            associateOrderUser(order);
            associateOrderState(order);
            associateOrderProduct(order);
        }

        return orders;
    }

    @Override
    public List<Order> readAllOrders() {
        String readAll = "SELECT * FROM `order`;";
        List<Order> orders = jdbc.query(readAll, new OrderMapper());

        for (Order o : orders) {
            associateOrderState(o);
            associateOrderProduct(o);
            associateOrderUser(o);
        }

        return orders;
    }

    @Override
    @Transactional
    public Order updateOrder(Order order) {
        String update = "UPDATE `order` " +
                "SET " +
                "orderDate = ?, " +
                "quantity = ?, " +
                "netPrice = ?, " +
                "tax = ?, " +
                "total = ?, " +
                "userId = ? " +
                "WHERE orderId = ?;";
        int updated = jdbc.update(update,
                order.getOrderDate(),
                order.getQuantity(),
                order.getNetPrice(),
                order.getTax(),
                order.getTotal(),
                order.getUser().getUserId(),
                order.getOrderId());

        if (updated == 1) {
            //delete and reinsert to State bridge
            String delOS = "DELETE FROM orderState " +
                    "WHERE orderId = ?;";
            jdbc.update(delOS, order.getOrderId());
            insertOrderState(order);

            //delete and reinsert to Product bridge
            String delOP = "DELETE FROM orderProduct " +
                    "WHERE orderId = ?;";
            jdbc.update(delOP, order.getOrderId());
            insertOrderProduct(order);

            return order;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public boolean deleteOrder(int id) {
        //delete from State bridge
        String delOS = "DELETE FROM orderState " +
                "WHERE orderId = ?;";
        jdbc.update(delOS, id);

        //delete from Product bridge
        String delOP = "DELETE FROM orderProduct " +
                "WHERE orderId = ?;";
        jdbc.update(delOP, id);

        //delete Order itself
        String deleteOrder = "DELETE FROM `order` " +
                "WHERE orderId = ?;";
        return jdbc.update(deleteOrder, id) == 1;
    }

    /*HELPERS*/

    /**
     * Update the Order State bridge table
     *
     * @param order {Order} well formed obj
     */
    private void insertOrderState(Order order) {
        String insertBridge = "INSERT INTO orderState (orderId, stateId) " +
                "VALUES(?,?);";
        jdbc.update(insertBridge, order.getOrderId(), order.getState().getStateId());
    }

    /**
     * Update the Order Product bridge table
     *
     * @param order {Order} well formed obj
     */
    private void insertOrderProduct(Order order) {
        String insertBridge = "INSERT INTO orderProduct (orderId, productId) " +
                "VALUES(?,?);";
        jdbc.update(insertBridge, order.getOrderId(), order.getProduct().getProductId());
    }

    /**
     * Retrieve User for an Order and associate in memory
     *
     * @param order {Order} a well formed obj
     * @throws DataAccessException if cannot retrieve User
     */
    private void associateOrderUser(Order order) throws DataAccessException {
        //read User
        String readUser = "SELECT u.* FROM user u " +
                "JOIN `order` o ON o.userId = u.userId " +
                "WHERE o.orderId = ?;";
        User u = jdbc.queryForObject(readUser, new UserMapper(), order.getOrderId());

        //read and associate roles for user
        String readRoles = "SELECT r.* FROM role r " +
                "JOIN userRole ur ON ur.roleId = r.roleId " +
                "WHERE ur.userId = ?;";
        Set<Role> userRoles = new HashSet<>(jdbc.query(readRoles, new RoleMapper(), u.getUserId()));
        u.setRoles(userRoles);

        order.setUser(u);
    }

    /**
     * Retrieve State for an Order and associate in memory
     *
     * @param order {Order} a well formed obj
     * @throws DataAccessException if cannot retrieve State
     */
    private void associateOrderState(Order order) throws DataAccessException {
        String readState = "SELECT s.* FROM state s " +
                "JOIN orderState os ON os.stateId = s.stateId " +
                "WHERE os.orderId = ?;";
        State s = jdbc.queryForObject(readState, new StateMapper(), order.getOrderId());

        order.setState(s);
    }

    /**
     * Retrieve Product for an Order and associate in memory
     *
     * @param order {Order} a well formed obj
     * @throws DataAccessException if cannot retrieve Product
     */
    private void associateOrderProduct(Order order) throws DataAccessException {
        String readProduct = "SELECT p.* FROM product p " +
                "JOIN orderProduct op ON op.productId = p.productId " +
                "WHERE op.orderId = ?;";
        Product p = jdbc.queryForObject(readProduct, new ProductDaoDb.ProductMapper(), order.getOrderId());

        order.setProduct(p);
    }

    /**
     * RowMapper impl
     */
    public static final class OrderMapper implements RowMapper<Order> {
        @Override
        public Order mapRow(ResultSet rs, int i) throws SQLException {
            Order o = new Order();
            o.setOrderId(rs.getInt("orderId"));
            o.setOrderDate(rs.getDate("orderDate").toLocalDate());
            o.setQuantity(rs.getInt("quantity"));
            o.setNetPrice(rs.getBigDecimal("netPrice"));
            o.setTax(rs.getBigDecimal("tax"));
            o.setTotal(rs.getBigDecimal("total"));
            //User, State, Product are associated with helper methods

            return o;
        }
    }
}
