package com.floormastery.dao.interfaces;

import com.floormastery.dao.exceptions.NoSuchOrderException;
import com.floormastery.dao.exceptions.PersistenceException;
import com.floormastery.model.Order;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderDao {
    int getNextOrderNumber();
    Order addOrder(LocalDate date, Order order) throws PersistenceException, NoSuchOrderException;
    Order getOrder(LocalDate date, int orderNumber) throws PersistenceException, NoSuchOrderException;
    Order editOrder(LocalDate date, int orderNumber, Order newOrder) throws PersistenceException, NoSuchOrderException;
    Order removeOrder(LocalDate date, int orderNumber) throws PersistenceException, NoSuchOrderException;
    List<Order> getOrdersForDate(LocalDate date) throws NoSuchOrderException;
    Map<LocalDate, Map<Integer, Order>> getAllOrders() throws PersistenceException;

}
