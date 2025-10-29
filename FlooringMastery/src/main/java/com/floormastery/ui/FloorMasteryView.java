package com.floormastery.ui;

import com.floormastery.model.Order;
import com.floormastery.model.Product;
import com.floormastery.model.Tax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class FloorMasteryView {

    @Autowired
    private UserIO io;

    public FloorMasteryView(UserIO io) {
        this.io = io;
    }

    public int printMenuAndGetSelection() {
        io.print("<<Flooring Program>>");
        io.print("1. Display Orders");
        io.print("2. Add an Order");
        io.print("3. Edit an Order");
        io.print("4. Remove an Order");
        io.print("5. Export All Data");
        io.print("6. Quit");

        return io.readInt("Please select from the above choices.", 1, 6);
    }

    public String getProductInput(List<Product> productList) {
        io.print("=== Select a Product ===");
        for (int i = 0; i < productList.size(); i++) {
            System.out.println(i+1 + ". " + productList.get(i).getProductType());
        }
        int choice = io.readInt("Please select from the above choices.", 1, productList.size());

        return productList.get(choice-1).getProductType();
    }

    public Order getAddOrderInput(List<Tax> taxList, List<Product> productList) {
        Order order = new Order();
        String customerName = io.readString("Please enter customer name");
        String state = io.readString("Please enter a state as abbreviation");
        String product = getProductInput(productList);
        BigDecimal area = BigDecimal.valueOf(io.readDouble("Please enter an area."));
        BigDecimal costPerSquareFoot = BigDecimal.valueOf(io.readDouble("Please enter cost per square foot."));
        BigDecimal laborCostPerSquareFoot = BigDecimal.valueOf(io.readDouble("Please enter labor cost per square foot."));

        BigDecimal taxRate = taxList.stream()
                    .filter(t -> t.getStateAbr().equals(state))
                    .map(Tax::getTaxRate).findFirst()
                    .orElse(BigDecimal.ZERO);

        System.out.println(taxRate);

        order.setCustomerName(customerName);
        order.setState(state);
        order.setTaxRate(taxRate);
        order.setProductType(product);
        order.setArea(area);
        order.setCostPerSquareFoot(costPerSquareFoot);
        order.setLaborCostPerSquareFoot(laborCostPerSquareFoot);

        return order;
    }

    public Order getEditOrderInput(List<Tax> taxList, List<Product> productList, Order order) {
        String newCustomerName = io.readString("Enter new name or press enter to skip.");
        String state = io.readString("Enter new state or press enter to skip.");
        String product = io.readString("Enter new product type or press enter to skip.");
        BigDecimal area = BigDecimal.valueOf(io.readDouble("Please enter an area or press enter to skip"));

        if (!newCustomerName.isBlank()) {
            order.setCustomerName(newCustomerName);
        }

        if (!state.isBlank()) {
            order.setState(state);

            BigDecimal taxRate = taxList.stream()
                    .filter(t -> t.getStateAbr().equals(state))
                    .map(Tax::getTaxRate).findFirst()
                    .orElse(BigDecimal.ZERO);

            order.setTax(taxRate);
        }

        if (!product.isBlank()) {
            order.setProductType(product);
        }

        if (!area.equals(order.getArea())) {
            order.setArea(area);
        }

        return order;

    }

    public boolean getShouldSaveData() {
        String choice = io.readString("Do you want to save data?");
        return choice.equalsIgnoreCase("Yes");
    }

    public void getEditSummary(Order order) {
        io.print("=== Summary ===");
        io.print(order.toString());
    }

    public int getOrderNumber() {
        return io.readInt("Enter orderNumber.");
    }

    public LocalDate getDateInput() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        return io.readDate("Please enter a date (format: MM-dd-yyyy)", formatter);
    }

    public void displayOrdersList(List<Order> orderList) {
        for (Order order : orderList) {
            io.print(order.toString());
        }
        io.readString("Please hit enter to continue.");
    }

    //Banners
    public void displayAllOrdersBanner() {
        io.print("=== Display All Order ===");
    }

    public void displayAddOrderBanner() {
        io.print("=== Add Order ===");
    }

    public void displayAddSucessBanner() {
        io.print("Order successfully created. Please hit enter to continue.");
    }

    public void displayEditOrderBanner() {
        io.print("=== Edit Order ===");
    }

    public void displayEditSucessBanner() {
        io.print("Order successfully modified. Please hit enter to continue.");
    }

    public void displayEditNotComplete() {
        io.print("Data will not be saved.");
    }

    public void displayNoOrderFoundBanner() {
        io.print("No order found.");
    }

    public void displayRemoveOrderBanner() {
        io.print("=== Remove Order ===");
    }

    public void displayRemoveSucessBanner() {
        io.print("Order successfully removed. Please hit enter to continue.");
    }

    public void displayExitBanner() {
        io.print("Good Bye!!!");
    }

    public void displayUnknownCommandBanner() {
        io.print("Unknown Command!!!");
    }

    public void displayErrorMessage(String errorMsg) {
        io.print("=== ERROR ===");
        io.print(errorMsg);
    }
}
