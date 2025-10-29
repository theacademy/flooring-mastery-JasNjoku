package com.floormastery.controller;

import com.floormastery.dao.exceptions.NoSuchOrderException;
import com.floormastery.dao.exceptions.PersistenceException;
import com.floormastery.model.Order;
import com.floormastery.service.FloorMasteryService;
import com.floormastery.service.exceptions.FloorMasteryDataValidationException;
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
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:

                        break;
                    case 6:
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
        boolean hasErrors = false;

        do {
            LocalDate date = view.getDateInput();

            try {
                List<Order> orderList = service.displayOrders(date);
                view.displayOrdersList(orderList);
                hasErrors = false;
            } catch (NoSuchOrderException e) {
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);
    }

    private void addOrder() throws PersistenceException, NoSuchOrderException, FloorMasteryDataValidationException {
        view.displayAddOrderBanner();
        Order newOrder = view.getAddOrderInput(service.getTaxes(), service.getProducts());
        LocalDate date = view.getDateInput();
        service.addOrder(date, newOrder);
        view.displayAddSucessBanner();
    }

    private void editOrder() throws NoSuchOrderException, PersistenceException, FloorMasteryDataValidationException {
        view.displayEditOrderBanner();
        LocalDate date = view.getDateInput();
        int orderNumber = view.getOrderNumber();
        Order order = service.getOrder(date, orderNumber);

        if (order == null) {
            view.displayNoOrderFoundBanner();
            return;
        }

        Order modifiedOrder = view.getEditOrderInput(service.getTaxes(), service.getProducts(), order);
        view.getEditSummary(modifiedOrder);

        boolean shouldReplace = view.getShouldSaveData();

        if (shouldReplace) {
            service.editOrder(date, orderNumber, modifiedOrder);
            view.displayEditSucessBanner();
        }

        view.displayEditNotComplete();
    }

    private void removeOrder() {
        view.displayRemoveOrderBanner();
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
