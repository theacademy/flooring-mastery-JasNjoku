package com.floormastery.dao;

import com.floormastery.dao.interfaces.OrderDao;
import com.floormastery.model.Order;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class OrderDaoImpl implements OrderDao {
    public static final String ORDER_FILE = "fileData/orders.txt";
    @Override
    public int getNextOrderNumber() {
        return 0;
    }

    @Override
    public Order addOrder(Order order) {
        return null;
    }

    @Override
    public Order getOrder(Order order) {
        return null;
    }

    @Override
    public Order editOrder(Order order) {
        return null;
    }

    @Override
    public Order removeOrder() {
        return null;
    }

    @Override
    public List<Order> getOrdersForDate(LocalDate date) {
        return List.of();
    }

    @Override
    public Map<LocalDate, Map<Integer, Order>> getAllOrders() {
        return Map.of();
    }
}
