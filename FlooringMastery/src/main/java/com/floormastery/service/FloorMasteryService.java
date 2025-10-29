package com.floormastery.service;

import com.floormastery.dao.exceptions.NoSuchOrderException;
import com.floormastery.dao.exceptions.PersistenceException;
import com.floormastery.model.Order;
import com.floormastery.model.Product;
import com.floormastery.model.Tax;
import com.floormastery.service.exceptions.FloorMasteryDataValidationException;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.List;

public interface FloorMasteryService {
    List<Order> displayOrders(LocalDate date) throws NoSuchOrderException;
    Order addOrder(LocalDate date, Order order) throws NoSuchOrderException, PersistenceException, FloorMasteryDataValidationException;
    Order editOrder(LocalDate date, int orderNumber, Order newOrder) throws PersistenceException, NoSuchOrderException, FloorMasteryDataValidationException;
    Order removeOrder(LocalDate date, int orderNumber) throws NoSuchOrderException, PersistenceException;
    Order getOrder(LocalDate date, int orderNumber) throws NoSuchOrderException, PersistenceException;
    List<Order> getOrdersForDate(LocalDate date) throws NoSuchOrderException;
    List<Tax> getTaxes() throws PersistenceException;
    List<Product> getProducts() throws PersistenceException;
}
