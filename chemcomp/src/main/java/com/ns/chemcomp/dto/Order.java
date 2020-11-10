package com.ns.chemcomp.dto;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Order {
    private int id;

    @NotNull(message = "Must log order date")
    private LocalDateTime orderDate;

    @NotNull(message = "Must specify volume/mass/quantity of compound for order")
    @DecimalMin(value = "0.0", inclusive = false, message = "Can't order nothing")
    @Digits(integer = 4, fraction = 2)
    private BigDecimal quantity;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
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
                getOrderDate().equals(order.getOrderDate()) &&
                getQuantity().equals(order.getQuantity()) &&
                getNetPrice().equals(order.getNetPrice()) &&
                getTax().equals(order.getTax()) &&
                getTotal().equals(order.getTotal()) &&
                getUser().equals(order.getUser()) &&
                getState().equals(order.getState()) &&
                getProduct().equals(order.getProduct());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOrderDate(), getQuantity(), getNetPrice(), getTax(), getTotal(), getUser(), getState(), getProduct());
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
