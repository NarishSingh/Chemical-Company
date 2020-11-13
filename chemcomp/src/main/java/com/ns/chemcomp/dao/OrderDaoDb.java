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
        String insert = "INSERT INTO chemComp.order (orderDate, quantity, massVolume, netPrice, tax, total, userId) " +
                "VALUES(?,?,?,?,?,?,?);";
        jdbc.update(insert,
                order.getOrderDate(),
                order.getQuantity(),
                order.getMassVolume(),
                order.getNetPrice(),
                order.getTax(),
                order.getTotal(),
                order.getUser().getId());

        //grab id
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID();", Integer.class);
        order.setId(newId);

        //update bridge tables
        insertOrderState(order);
        insertOrderProduct(order);

        return order;
    }

    @Override
    public Order readOrderById(int id) {
        try {
            String read = "SELECT * FROM chemComp.order " +
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
        return null;
    }

    @Override
    public List<Order> readOrdersByProduct(Product product) {
        return null;
    }

    @Override
    public List<Order> readOrdersByUser(User user) {
        return null;
    }

    @Override
    public List<Order> readOrdersByState(State state) {
        return null;
    }

    @Override
    public List<Order> readAllOrders() {
        return null;
    }

    @Override
    public Order updateOrder(Order order) {
        return null;
    }

    @Override
    public boolean deleteOrder(int id) {
        return false;
    }

    /*HELPERS*/

    /**
     * Update the Order State bridge table
     *
     * @param order {Order} well formed obj
     */
    private void insertOrderState(Order order) {
        String insertBridge = "INSERT INTO chemComp.orderState (orderId, stateId) " +
                "VALUES(?,?);";
        jdbc.update(insertBridge, order.getId(), order.getState().getId());
    }

    /**
     * Update the Order Product bridge table
     *
     * @param order {Order} well formed obj
     */
    private void insertOrderProduct(Order order) {
        String insertBridge = "INSERT INTO chemComp.orderProduct (orderId, productId) " +
                "VALUES(?,?);";
        jdbc.update(insertBridge, order.getId(), order.getProduct().getId());
    }

    /**
     * Retrieve User for an Order and associate in memory
     *
     * @param order {Order} a well formed obj
     * @throws DataAccessException if cannot retrieve User
     */
    private void associateOrderUser(Order order) throws DataAccessException {
        //read User
        String readUser = "SELECT u.* FROM chemComp.user u " +
                "JOIN chemComp.order o ON o.userId = u.userId " +
                "WHERE o.orderId = ?;";
        User u = jdbc.queryForObject(readUser, new UserMapper(), order.getId());

        //read and associate roles
        String readRoles = "SELECT r.* FROM chemComp.role r " +
                "JOIN chemComp.userRole ur ON ur.roleId = r.roleId " +
                "WHERE ur.userId = ?;";
        Set<Role> userRoles = new HashSet<>(jdbc.query(readRoles, new RoleMapper(), u.getId()));

        u.setRoles(userRoles);
    }

    /**
     * Retrieve State for an Order and associate in memory
     *
     * @param order {Order} a well formed obj
     * @throws DataAccessException if cannot retrieve State
     */
    private void associateOrderState(Order order) throws DataAccessException {
        String readState = "SELECT s.* FROM chemComp.state s " +
                "JOIN chemComp.orderState os ON os.stateId = s.stateId " +
                "WHERE os.orderId = ?;";
        State s = jdbc.queryForObject(readState, new StateMapper(), order.getId());

        order.setState(s);
    }

    /**
     * Retrieve Product for an Order and associate in memory
     *
     * @param order {Order} a well formed obj
     * @throws DataAccessException if cannot retrieve Product
     */
    private void associateOrderProduct(Order order) throws DataAccessException {
        String readProduct = "SELECT p.* FROM chemComp.product p " +
                "JOIN chemComp.orderProduct op ON op.productId = p.productId " +
                "WHERE op.orderId = ?;";
        Product p = jdbc.queryForObject(readProduct, new ProductDaoDb.ProductMapper(), order.getId());

        order.setProduct(p);
    }

    /**
     * RowMapper impl
     */
    public static final class OrderMapper implements RowMapper<Order> {

        @Override
        public Order mapRow(ResultSet rs, int i) throws SQLException {
            Order o = new Order();
            o.setId(rs.getInt("orderId"));
            o.setOrderDate(rs.getDate("orderDate").toLocalDate());
            o.setQuantity(rs.getInt("quantity"));
            o.setMassVolume(rs.getBigDecimal("massVolume"));
            o.setNetPrice(rs.getBigDecimal("netPrice"));
            o.setTax(rs.getBigDecimal("tax"));
            o.setTotal(rs.getBigDecimal("total"));
            //User, State, Product are read with helper methods

            return o;
        }
    }
}
