package com.floormastery.service;

import com.floormastery.dao.exceptions.NoSuchOrderException;
import com.floormastery.dao.exceptions.PersistenceException;
import com.floormastery.dao.interfaces.OrderDao;
import com.floormastery.dao.interfaces.ProductDao;
import com.floormastery.dao.interfaces.TaxDao;
import com.floormastery.model.Order;
import com.floormastery.service.exceptions.FloorMasteryDataValidationException;
import com.floormastery.service.exceptions.FloorMasteryDuplicateOrderNumberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class FloorMasteryServiceImpl implements FloorMasteryService {
    private OrderDao orderDao;
    private TaxDao taxDao;
    private ProductDao productDao;

    @Autowired
    public FloorMasteryServiceImpl(OrderDao orderDao, TaxDao taxDao, ProductDao productDao) {
        this.orderDao = orderDao;
        this.taxDao = taxDao;
        this.productDao = productDao;
    }

    @Override
    public List<Order> displayOrders(LocalDate date) throws NoSuchOrderException {
        return orderDao.getOrdersForDate(date);
    }

    @Override
    public Order addOrder(LocalDate date, Order order) throws NoSuchOrderException, PersistenceException, FloorMasteryDataValidationException {
        //check if its already there
        Order orderFromDao = null;
        
        try {
             orderFromDao = orderDao.getOrder(date, order.getOrderNumber());
        } catch (PersistenceException ignored) {}
        
        
        if (orderFromDao != null) {

            throw new FloorMasteryDuplicateOrderNumberException(
                    "ERROR: Could not create order. Order Number "
                            + order.getOrderNumber()
                            + " already exists."
            );
        }

        //validate
        validateOrderData(date, order);

        //calculate fields
        calculateFields(order);

        //add
        return orderDao.addOrder(date,order);
    }

    @Override
    public Order editOrder(LocalDate date, int orderNumber, Order newOrder) throws PersistenceException, FloorMasteryDataValidationException, NoSuchOrderException {
        validateOrderData(date, newOrder);

        //recalculate
        calculateFields(newOrder);

        return orderDao.editOrder(date, orderNumber, newOrder);
    }

    @Override
    public Order removeOrder(LocalDate date, int orderNumber) throws NoSuchOrderException, PersistenceException {
        return orderDao.removeOrder(date, orderNumber);
    }

    @Override
    public List<Order> getOrdersForDate(LocalDate date) throws NoSuchOrderException {
        return orderDao.getOrdersForDate(date);
    }

    private void validateOrderData(LocalDate orderDate, Order order) throws FloorMasteryDataValidationException, PersistenceException {
        //Make sure Date is in the future
        if (orderDate.isBefore(LocalDate.now().plusDays(1))) {
            throw new FloorMasteryDataValidationException("ERROR: Order date must be in the future.");
        }

        //Customer name validation
        if (order.getCustomerName() == null || order.getCustomerName().trim().isEmpty()) {
            throw new FloorMasteryDataValidationException("ERROR: Customer name may not be blank.");
        }
        if (!order.getCustomerName().matches("[a-zA-Z0-9., ]+")) {
            throw new FloorMasteryDataValidationException("ERROR: Customer name contains invalid characters.");
        }

        //State validation – must exist in taxes list
        boolean stateExists = taxDao.getAllTaxes().stream()
                .anyMatch(t -> t.getStateAbr().equalsIgnoreCase(order.getState()));
        if (!stateExists) {
            throw new FloorMasteryDataValidationException(
                    "ERROR: Cannot sell in the state '" + order.getState() + "'.");
        }

        //Product validation – must exist in products list
        boolean productExists = productDao.getAllProducts().stream()
                .anyMatch(p -> p.getProductType().equalsIgnoreCase(order.getProductType()));
        if (!productExists) {
            throw new FloorMasteryDataValidationException(
                    "ERROR: Product type '" + order.getProductType() + "' does not exist.");
        }

        // Area validation – positive decimal, min 100 sq ft
        if (order.getArea() == null || order.getArea().compareTo(new BigDecimal("100")) < 0) {
            throw new FloorMasteryDataValidationException("ERROR: Area must be at least 100 sq ft.");
        }

    }

    private void calculateFields(Order order) {
        // MaterialCost = (Area * CostPerSquareFoot)
        BigDecimal materialCost = order.getArea().multiply(order.getCostPerSquareFoot());

        // LaborCost = (Area * LaborCostPerSquareFoot)
        BigDecimal laborCost = order.getArea().multiply(order.getLaborCostPerSquareFoot());

        // Tax = (MaterialCost + LaborCost) * (TaxRate / 100)
        BigDecimal subtotal = materialCost.add(laborCost);
        BigDecimal tax = subtotal.multiply(order.getTaxRate().divide(new BigDecimal("100")));

        // Total = (MaterialCost + LaborCost + Tax)
        BigDecimal total = subtotal.add(tax);

        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(tax);
        order.setTotal(total);
    }
}
