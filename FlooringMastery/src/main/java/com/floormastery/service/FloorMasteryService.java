package com.floormastery.service;

import com.floormastery.dao.exceptions.NoSuchOrderException;
import com.floormastery.model.Order;

import java.time.LocalDate;
import java.util.List;

public interface FloorMasteryService {
    List<Order> displayOrders(LocalDate date) throws NoSuchOrderException;
    Order addOrder(LocalDate date, Order order);
}
