package com.ns.chemcomp.service;

import com.ns.chemcomp.dto.Order;
import com.ns.chemcomp.dto.Product;
import com.ns.chemcomp.dto.State;
import com.ns.chemcomp.dto.User;
import org.springframework.dao.DataAccessException;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    /**
     * Create a new Order
     *
     * @param orderRequest {Order} well formed obj
     * @return {Order} successfully added obj from db
     */
    Order createOrder(Order orderRequest);

    /**
     * Read an Order from db by its id
     *
     * @param id {int} a valid id
     * @return {Order} obj from db, null if read fails
     * @throws DataAccessException if read from db fails, will return null
     */
    Order readOrderById(int id) throws DataAccessException;

    /**
     * Read Orders from db by its date
     *
     * @param date {LocalDate} a date
     * @return {List} objs from db, null if read fails
     * @throws NoOrdersOnDateException if list returns null
     */
    List<Order> readOrdersByDate(LocalDate date) throws NoOrdersOnDateException;

    /**
     * Read Orders from db by Product
     *
     * @param product {Product} well formed obj
     * @return {List} objs from db, null if read fails
     * @throws NoOrdersForProductException if list returns null
     */
    List<Order> readOrdersByProduct(Product product) throws NoOrdersForProductException;

    /**
     * Read Orders from db by User
     *
     * @param user {User} well formed obj
     * @return {List} objs from db, null if read fails
     * @throws NoOrdersForUserException if list returns null
     */
    List<Order> readOrdersByUser(User user) throws NoOrdersForUserException;

    /**
     * Read Orders from db by State
     *
     * @param state {State} well formed obj
     * @return {List} objs from db, null if read fails
     * @throws NoOrdersInStateException if list returns null
     */
    List<Order> readOrdersByState(State state) throws NoOrdersInStateException;

    /**
     * Read all Orders from db
     *
     * @return {List} all objs in db
     */
    List<Order> readAllOrders();

    /**
     * Update an Order in db
     *
     * @param updateRequest {Order} well formed obj with the matching id for edit
     * @return {Order} the successfully updated obj, null if edit fails
     */
    Order updateOrder(Order updateRequest);

    /**
     * Delete an Order from db
     *
     * @param id {int} a valid id
     * @return {boolean} true if deleted, false if failed
     */
    boolean deleteOrder(int id);
}
