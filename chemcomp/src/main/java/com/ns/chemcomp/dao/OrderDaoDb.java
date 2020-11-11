package com.ns.chemcomp.dao;

import com.ns.chemcomp.dto.Order;
import com.ns.chemcomp.dto.Product;
import com.ns.chemcomp.dto.State;
import com.ns.chemcomp.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

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
        String read = "SELECT * FROM chemComp.order " +
                "WHERE orderId = ?;";
        Order order = jdbc.queryForObject(read, new OrderMapper(), id);

        //FIXME verify how to associate user?
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

    private void associateOrderUser(Order order) {

    }

    private void associateOrderState(Order order) {

    }

    private void associateOrderProduct(Order order) {

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
