package com.ns.chemcomp.dto;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private int id;

    @Size(max = 50, message = "Common name must not exceed 50 char")
    private String name;

    @NotBlank(message = "Scientific name required for product")
    @Size(max = 50, message = "Scientific name cannot exceed 50 chars")
    private String chemicalName;

    @NotNull(message = "Must specify the mass or volume of the dry weight or container of chemical")
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 3, fraction = 2)
    private BigDecimal massVolume;

    @NotBlank(message = "Unit of measure required for product")
    @Size(max = 15, message = "Measurement abbreviation cannot exceed 15 chars")
    private String measurement;

    @NotNull(message = "Product needs a unit cost")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price cannot be null")
    @Digits(integer = 4, fraction = 2, message = "Price must be well formed, and cannot exceed $9,999.99")
    private BigDecimal unitCost;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price cannot be $0 if not null")
    @Digits(integer = 4, fraction = 2, message = "Price must be well formed, and cannot exceed $9,999.99")
    private BigDecimal handlingCost;

    @Size(max = 255, message = "Filename cannot exceed 255 chars")
    private String photoFilename;

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

    public BigDecimal getMassVolume() {
        return massVolume;
    }

    public void setMassVolume(BigDecimal massVolume) {
        this.massVolume = massVolume;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public BigDecimal getHandlingCost() {
        return handlingCost;
    }

    public void setHandlingCost(BigDecimal handlingCost) {
        this.handlingCost = handlingCost;
    }

    public String getPhotoFilename() {
        return photoFilename;
    }

    public void setPhotoFilename(String photoFilename) {
        this.photoFilename = photoFilename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return getId() == product.getId() &&
                Objects.equals(getName(), product.getName()) &&
                getChemicalName().equals(product.getChemicalName()) &&
                getMassVolume().equals(product.getMassVolume()) &&
                getMeasurement().equals(product.getMeasurement()) &&
                getUnitCost().equals(product.getUnitCost()) &&
                getHandlingCost().equals(product.getHandlingCost()) &&
                Objects.equals(getPhotoFilename(), product.getPhotoFilename());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getChemicalName(), getMassVolume(), getMeasurement(), getUnitCost(),
                getHandlingCost(), getPhotoFilename());
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", chemicalName='" + chemicalName + '\'' +
                ", massVolume=" + massVolume +
                ", measurement='" + measurement + '\'' +
                ", unitCost=" + unitCost +
                ", handlingCost=" + handlingCost +
                ", photoFilename='" + photoFilename + '\'' +
                '}';
    }
}
