package com.floormastery.dao.interfaces;

import com.floormastery.model.Order;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderDao {
    int getNextOrderNumber();
    Order addOrder(Order order);
    Order getOrder(Order order);
    Order editOrder(Order order);
    Order removeOrder();
    List<Order> getOrdersForDate(LocalDate date);
    Map<LocalDate, Map<Integer, Order>> getAllOrders();

}
