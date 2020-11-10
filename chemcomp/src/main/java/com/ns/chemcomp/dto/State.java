package com.ns.chemcomp.dto;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

public class State {
    private int id;

    @NotBlank(message = "State name cannot be blank")
    private String name;

    @Size(min = 2, max = 2, message = "Abbreviation should be 2 char")
    private String abbreviation;

    @NotNull(message = "Please list state tax rate")
    @DecimalMin(value = "0.0", inclusive = false, message = "Tax rate cannot be $0")
    @Digits(integer = 2, fraction = 2, message = "Tax rate must be well formed percentage")
    private BigDecimal taxRate;

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

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;
        State state = (State) o;
        return getId() == state.getId() &&
                getName().equals(state.getName()) &&
                Objects.equals(getAbbreviation(), state.getAbbreviation()) &&
                getTaxRate().equals(state.getTaxRate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getAbbreviation(), getTaxRate());
    }
}
