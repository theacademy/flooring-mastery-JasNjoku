package com.floormastery.ui;

import com.floormastery.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        io.print("4. Export All Data");
        io.print("5. Quit");

        return io.readInt("Please select from the above choices.", 1, 5);
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
