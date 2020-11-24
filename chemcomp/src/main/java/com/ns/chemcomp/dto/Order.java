package com.ns.chemcomp.dto;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Order {
    private int id;

    @NotNull(message = "Must log order date")
    @FutureOrPresent(message = "Cannot order in the past")
    private LocalDate orderDate;

    @NotNull(message = "Must specify quantity of order")
    private int quantity;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 6, fraction = 2)
    private BigDecimal netPrice;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 6, fraction = 2)
    private BigDecimal tax;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 6, fraction = 2)
    private BigDecimal total;

    @Valid
    User user;

    @Valid
    State state;

    @Valid
    Product product;

    /*CTORS*/
    public Order() {

    } //d.ctor

    public Order(@NotNull(message = "Must log order date") @FutureOrPresent(message = "Cannot order in the past") LocalDate orderDate,
                 @NotNull(message = "Must specify quantity of order") int quantity, @Valid User user, @Valid State state,
                 @Valid Product product) {
        this.orderDate = orderDate;
        this.quantity = quantity;
        this.user = user;
        this.state = state;
        this.product = product;
    } //order request

    public Order(@NotNull(message = "Must log order date") @FutureOrPresent(message = "Cannot order in the past") LocalDate orderDate,
                 @NotNull(message = "Must specify quantity of order") int quantity,
                 @NotNull @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 6, fraction = 2) BigDecimal netPrice,
                 @NotNull @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 6, fraction = 2) BigDecimal tax,
                 @NotNull @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 6, fraction = 2) BigDecimal total,
                 @Valid User user, @Valid State state, @Valid Product product) {
        this.orderDate = orderDate;
        this.quantity = quantity;
        this.netPrice = netPrice;
        this.tax = tax;
        this.total = total;
        this.user = user;
        this.state = state;
        this.product = product;
    } //validated order, no id as must come from db

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(BigDecimal netPrice) {
        this.netPrice = netPrice;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return getId() == order.getId() &&
                getQuantity() == order.getQuantity() &&
                getOrderDate().equals(order.getOrderDate()) &&
                getNetPrice().equals(order.getNetPrice()) &&
                getTax().equals(order.getTax()) &&
                getTotal().equals(order.getTotal()) &&
                getUser().equals(order.getUser()) &&
                getState().equals(order.getState()) &&
                getProduct().equals(order.getProduct());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOrderDate(), getQuantity(), getNetPrice(), getTax(), getTotal(), getUser(),
                getState(), getProduct());
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", quantity=" + quantity +
                ", netPrice=" + netPrice +
                ", tax=" + tax +
                ", total=" + total +
                ", user=" + user +
                ", state=" + state +
                ", product=" + product +
                '}';
    }
}
