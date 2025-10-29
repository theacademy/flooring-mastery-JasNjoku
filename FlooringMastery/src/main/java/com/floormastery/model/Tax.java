package com.floormastery.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Tax {
    String state;
    String stateAbr;
    BigDecimal taxRate;

    public String getState() {
        return state;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public String getStateAbr() {
        return stateAbr;
    }

    public void setStateAbr(String stateAbr) {
        this.stateAbr = stateAbr;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tax tax = (Tax) o;
        return Objects.equals(state, tax.state) && Objects.equals(stateAbr, tax.stateAbr) && Objects.equals(taxRate, tax.taxRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, stateAbr, taxRate);
    }

    @Override
    public String toString() {
        return state + "," + stateAbr + "," + taxRate;
    }

}
