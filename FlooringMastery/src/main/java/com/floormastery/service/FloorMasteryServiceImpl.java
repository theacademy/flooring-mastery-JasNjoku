package com.floormastery.service;

import com.floormastery.dao.exceptions.NoSuchOrderException;
import com.floormastery.dao.interfaces.OrderDao;
import com.floormastery.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class FloorMasteryServiceImpl implements FloorMasteryService {
    private OrderDao orderDao;

    @Autowired
    public FloorMasteryServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public List<Order> displayOrders(LocalDate date) throws NoSuchOrderException {
        return orderDao.getOrdersForDate(date);
    }

    @Override
    public Order addOrder(LocalDate date, Order order) {
        //check if its already there
        //validate
        //add
        return null;
    }


    private void validateOrderData(Order order) {

    }

}
