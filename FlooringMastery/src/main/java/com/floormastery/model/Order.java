package com.floormastery.model;

import java.math.BigDecimal;

public class Order {
    private int OrderNumber;
    private String CustomerName;
    public String State;
    public BigDecimal TaxRate;
    public String ProductType;
    public BigDecimal Area;
    public BigDecimal CostPerSquareFoot;
    public BigDecimal LaborCostPerSquareFoot;
    public BigDecimal MaterialCost;
    public BigDecimal LaborCost;
    public BigDecimal Tax;
    public BigDecimal Total;

    public int getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        OrderNumber = orderNumber;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public BigDecimal getTaxRate() {
        return TaxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        TaxRate = taxRate;
    }

    public String getProductType() {
        return ProductType;
    }

    public void setProductType(String productType) {
        ProductType = productType;
    }

    public BigDecimal getArea() {
        return Area;
    }

    public void setArea(BigDecimal area) {
        Area = area;
    }

    public BigDecimal getCostPerSquareFoot() {
        return CostPerSquareFoot;
    }

    public void setCostPerSquareFoot(BigDecimal costPerSquareFoot) {
        CostPerSquareFoot = costPerSquareFoot;
    }

    public BigDecimal getLaborCostPerSquareFoot() {
        return LaborCostPerSquareFoot;
    }

    public void setLaborCostPerSquareFoot(BigDecimal laborCostPerSquareFoot) {
        LaborCostPerSquareFoot = laborCostPerSquareFoot;
    }

    public BigDecimal getMaterialCost() {
        return MaterialCost;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        MaterialCost = materialCost;
    }

    public BigDecimal getLaborCost() {
        return LaborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        LaborCost = laborCost;
    }

    public BigDecimal getTax() {
        return Tax;
    }

    public void setTax(BigDecimal tax) {
        Tax = tax;
    }

    public BigDecimal getTotal() {
        return Total;
    }

    public void setTotal(BigDecimal total) {
        Total = total;
    }

    @Override
    public String toString() {
        return
                OrderNumber +
                "," + CustomerName +
                "," + State +
                "," + TaxRate +
                "," + ProductType +
                "," + Area +
                "," + CostPerSquareFoot +
                "," + LaborCostPerSquareFoot +
                "," + MaterialCost +
                "," + LaborCost +
                "," + Tax +
                "," + Total;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.OrderNumber;
        hash = 89 * hash + java.util.Objects.hashCode(this.CustomerName);
        hash = 89 * hash + java.util.Objects.hashCode(this.State);
        hash = 89 * hash + java.util.Objects.hashCode(this.TaxRate);
        hash = 89 * hash + java.util.Objects.hashCode(this.ProductType);
        hash = 89 * hash + java.util.Objects.hashCode(this.Area);
        hash = 89 * hash + java.util.Objects.hashCode(this.CostPerSquareFoot);
        hash = 89 * hash + java.util.Objects.hashCode(this.LaborCostPerSquareFoot);
        hash = 89 * hash + java.util.Objects.hashCode(this.MaterialCost);
        hash = 89 * hash + java.util.Objects.hashCode(this.LaborCost);
        hash = 89 * hash + java.util.Objects.hashCode(this.Tax);
        hash = 89 * hash + java.util.Objects.hashCode(this.Total);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if (this.OrderNumber != other.OrderNumber) {
            return false;
        }
        if (!java.util.Objects.equals(this.CustomerName, other.CustomerName)) {
            return false;
        }
        if (!java.util.Objects.equals(this.State, other.State)) {
            return false;
        }
        if (!java.util.Objects.equals(this.ProductType, other.ProductType)) {
            return false;
        }
        if (!java.util.Objects.equals(this.TaxRate, other.TaxRate)) {
            return false;
        }
        if (!java.util.Objects.equals(this.Area, other.Area)) {
            return false;
        }
        if (!java.util.Objects.equals(this.CostPerSquareFoot, other.CostPerSquareFoot)) {
            return false;
        }
        if (!java.util.Objects.equals(this.LaborCostPerSquareFoot, other.LaborCostPerSquareFoot)) {
            return false;
        }
        if (!java.util.Objects.equals(this.MaterialCost, other.MaterialCost)) {
            return false;
        }
        if (!java.util.Objects.equals(this.LaborCost, other.LaborCost)) {
            return false;
        }
        if (!java.util.Objects.equals(this.Tax, other.Tax)) {
            return false;
        }
        if (!java.util.Objects.equals(this.Total, other.Total)) {
            return false;
        }
        return true;
    }

}
