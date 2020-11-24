package com.ns.chemcomp.service;

import com.ns.chemcomp.dao.OrderDao;
import com.ns.chemcomp.dto.Order;
import com.ns.chemcomp.dto.Product;
import com.ns.chemcomp.dto.State;
import com.ns.chemcomp.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceDb implements OrderService {

    @Autowired
    OrderDao oDao;

    @Override
    public Order createOrder(Order orderRequest) {
        Order newOrder = calculateCosts(orderRequest);

        return oDao.createOrder(newOrder);
    }

    @Override
    public Order readOrderById(int id) throws DataAccessException {
        return oDao.readOrderById(id);
    }

    @Override
    public List<Order> readOrderByDate(LocalDate date) {
        return oDao.readOrderByDate(date);
    }

    @Override
    public List<Order> readOrdersByProduct(Product product) {
        return oDao.readOrdersByProduct(product);
    }

    @Override
    public List<Order> readOrdersByUser(User user) {
        return oDao.readOrdersByUser(user);
    }

    @Override
    public List<Order> readOrdersByState(State state) {
        return oDao.readOrdersByState(state);
    }

    @Override
    public List<Order> readAllOrders() {
        return oDao.readAllOrders();
    }

    @Override
    public Order updateOrder(Order updateRequest) {
        Order update = calculateCosts(updateRequest);

        return oDao.updateOrder(update);
    }

    @Override
    public boolean deleteOrder(int id) {
        return oDao.deleteOrder(id);
    }

    /*HELPERS*/

    /**
     * Validate an Order by calculating costs
     *
     * @param orderRequest {Order} a new Order request
     * @return {Order} a validated Order obj
     */
    public Order calculateCosts(Order orderRequest) {
        //net price = order.quantity * product.unitCost + product.handlingCost
        BigDecimal netPrice = new BigDecimal(String.valueOf(orderRequest.getQuantity()))
                .multiply(orderRequest.getProduct().getUnitCost())
                .add(orderRequest.getProduct().getHandlingCost())
                .setScale(2, RoundingMode.HALF_UP);

        //tax = state.taxRate * netPrice
        BigDecimal tax = orderRequest.getState().getTaxRate()
                .multiply(netPrice)
                .setScale(2, RoundingMode.HALF_UP);

        //total = tax + netPrice
        BigDecimal total = netPrice.add(tax)
                .setScale(2, RoundingMode.HALF_UP);

        orderRequest.setNetPrice(netPrice);
        orderRequest.setTax(tax);
        orderRequest.setTotal(total);

        return orderRequest;
    }
}
