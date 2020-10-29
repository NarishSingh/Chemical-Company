package com.ns.chemcomp.DTO;

import javax.validation.constraints.*;
import java.util.Objects;

public class Product {
    private int id;

    @Size(max = 50, message = "Common name must not exceed 50 char")
    private String name;

    @NotBlank(message = "Scientific name required for product")
    @Size(max = 50, message = "Scientific name cannot exceed 50 chars")
    private String chemicalName;

    @NotBlank(message = "Unit of measure required for product")
    @Size(max = 10, message = "Measurement abbreviation cannot exceed 10 chars")
    private String measurement;

    @NotNull(message = "Product needs a unit cost")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price cannot be null")
    @Digits(integer = 4, fraction = 2, message = "Price must be well formed, and cannot exceed $9,999.99")
    private double unitCost;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price cannot be 0 if not null")
    @Digits(integer = 4, fraction = 2, message = "Price must be well formed, and cannot exceed $9,999.99")
    private double handlingCost;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChemicalName() {
        return chemicalName;
    }

    public void setChemicalName(String chemicalName) {
        this.chemicalName = chemicalName;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public double getHandlingCost() {
        return handlingCost;
    }

    public void setHandlingCost(double handlingCost) {
        this.handlingCost = handlingCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return getId() == product.getId() &&
                Double.compare(product.getUnitCost(), getUnitCost()) == 0 &&
                Double.compare(product.getHandlingCost(), getHandlingCost()) == 0 &&
                Objects.equals(getName(), product.getName()) &&
                Objects.equals(getChemicalName(), product.getChemicalName()) &&
                Objects.equals(getMeasurement(), product.getMeasurement());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getChemicalName(), getMeasurement(), getUnitCost(), getHandlingCost());
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", chemicalName='" + chemicalName + '\'' +
                ", measurement='" + measurement + '\'' +
                ", unitCost=" + unitCost +
                ", handlingCost=" + handlingCost +
                '}';
    }
}
