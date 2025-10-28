package com.floormastery.controller;

import com.floormastery.model.Order;
import com.floormastery.service.FloorMasteryService;
import com.floormastery.ui.FloorMasteryView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class FloorMasteryController {
    private FloorMasteryView view;
    private FloorMasteryService service;

    @Autowired
    public FloorMasteryController(FloorMasteryView view, FloorMasteryService service) {
        this.view = view;
        this.service = service;
    }

    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;
        try {
            while (keepGoing) {

                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1:
                        displayOrders();
                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                    case 5:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();

                }
            }
            exitMessage();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void displayOrders() {
        view.displayAllOrdersBanner();
        LocalDate date = view.getDateInput();
        List<Order> orderList = service.displayOrders(date);
        view.displayOrdersList(orderList);
    }

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }

    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    private void exitMessage() {
        view.displayExitBanner();
    }
}
