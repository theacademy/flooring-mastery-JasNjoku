package com.floormastery.service;

import com.floormastery.dao.exceptions.NoSuchOrderException;
import com.floormastery.dao.exceptions.PersistenceException;
import com.floormastery.model.Order;
import com.floormastery.service.exceptions.FloorMasteryDataValidationException;

import java.time.LocalDate;
import java.util.List;

public interface FloorMasteryService {
    List<Order> displayOrders(LocalDate date) throws NoSuchOrderException;
    Order addOrder(LocalDate date, Order order) throws NoSuchOrderException, PersistenceException, FloorMasteryDataValidationException;
    List<Order> getOrdersForDate(LocalDate date) throws NoSuchOrderException;
}
