package com.floormastery.service;

import com.floormastery.dao.OrderDaoImpl;
import com.floormastery.dao.ProductDaoImpl;
import com.floormastery.dao.TaxDaoFileImpl;
import com.floormastery.dao.exceptions.NoSuchOrderException;
import com.floormastery.dao.exceptions.PersistenceException;
import com.floormastery.dao.interfaces.OrderDao;
import com.floormastery.dao.interfaces.ProductDao;
import com.floormastery.dao.interfaces.TaxDao;
import com.floormastery.model.Order;
import com.floormastery.model.Product;
import com.floormastery.service.exceptions.FloorMasteryDataValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FloorMasteryServiceImplTest {
    OrderDao orderDao;
    FloorMasteryService service;

    @BeforeEach
    public void setUp() throws Exception {
        String testFolder = "fileData/TestFiles/Orders";
        File folder = new File(testFolder);
        if (!folder.exists()) folder.mkdirs();

        // Delete all files in the folder
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    file.delete();
                }
            }
        }

        orderDao = new OrderDaoImpl(testFolder);
        service = new FloorMasteryServiceImpl(orderDao, new TaxDaoFileImpl("fileData/TestFiles/Data/Taxes.txt"), new ProductDaoImpl("fileData/TestFiles/Data/Products.txt"));
    }

    private Order createValidOrder() {
        Order order = new Order();
        order.setCustomerName("Ada Lovelace");
        order.setState("CA");
        order.setTaxRate(new BigDecimal("25.00"));
        order.setProductType("Tile");
        order.setArea(new BigDecimal("249.00"));
        order.setCostPerSquareFoot(new BigDecimal("3.50"));
        order.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        return order;
    }

    @Test
    void testAddOrder_Success() throws NoSuchOrderException, PersistenceException, FloorMasteryDataValidationException {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        Order order = createValidOrder();

        Order added = service.addOrder(futureDate, order);

        List<Order> orderList = service.getOrdersForDate(futureDate);

        assertNotNull(added);
        assertEquals(1, orderList.size());
        assertEquals("Ada Lovelace", orderList.get(0).getCustomerName());
    }

    @Test
    void testAddOrder_InvalidCustomerName() {
        LocalDate futureDate = LocalDate.now().plusDays(1);

        Order order = createValidOrder();
        order.setCustomerName("!!!"); // invalid

        FloorMasteryDataValidationException ex = assertThrows(
                FloorMasteryDataValidationException.class,
                () -> service.addOrder(futureDate, order)
        );

        assertTrue(ex.getMessage().contains("Customer name contains invalid characters"));
    }

    @Test
    void testAddOrder_InvalidProductType() throws Exception {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        Order invalidOrder = createValidOrder();
        invalidOrder.setProductType("Marble"); // not in mocked product list

        FloorMasteryDataValidationException ex = assertThrows(
                FloorMasteryDataValidationException.class,
                () -> service.addOrder(futureDate, invalidOrder)
        );

        assertTrue(ex.getMessage().contains("Product type 'Marble' does not exist."));
    }

    @Test
    void testAddOrder_InvalidState() {
        LocalDate futureDate = LocalDate.now().plusDays(1);

        Order order = createValidOrder();
        order.setState("ZZ"); // invalid

        FloorMasteryDataValidationException ex = assertThrows(
                FloorMasteryDataValidationException.class,
                () -> service.addOrder(futureDate, order)
        );

        assertTrue(ex.getMessage().contains("Cannot sell in the state"));
    }

    @Test
    void testAddOrder_InvalidArea() {
        LocalDate futureDate = LocalDate.now().plusDays(1);

        Order order = createValidOrder();
        order.setArea(new BigDecimal("50")); // too small
        order.setMaterialCost(order.getArea().multiply(order.getCostPerSquareFoot()));
        order.setLaborCost(order.getArea().multiply(order.getLaborCostPerSquareFoot()));
        order.setTax(order.getMaterialCost().add(order.getLaborCost())
                .multiply(order.getTaxRate()).divide(new BigDecimal("100")));
        order.setTotal(order.getMaterialCost().add(order.getLaborCost()).add(order.getTax()));

        FloorMasteryDataValidationException ex = assertThrows(
                FloorMasteryDataValidationException.class,
                () -> service.addOrder(futureDate, order)
        );

        assertTrue(ex.getMessage().contains("Area must be at least 100"));
    }

    @Test
    void testEditOrder_Success() throws Exception {
        LocalDate futureDate = LocalDate.now().plusDays(2);

        // Create initial order
        Order originalOrder = createValidOrder();

        // Add the original order
        Order addedOrder = service.addOrder(futureDate, originalOrder);
        int orderNumber = addedOrder.getOrderNumber();

        Order editedOrder = new Order();
        editedOrder.setOrderNumber(orderNumber);
        editedOrder.setCustomerName("Acme Corp");
        editedOrder.setState("TX");
        editedOrder.setTaxRate(new BigDecimal("4.45"));
        editedOrder.setProductType("Tile");
        editedOrder.setArea(new BigDecimal("250"));
        editedOrder.setCostPerSquareFoot(new BigDecimal("3.50"));
        editedOrder.setLaborCostPerSquareFoot(new BigDecimal("4.15"));;

        // Perform edit
        Order updatedOrder = service.editOrder(futureDate, orderNumber, editedOrder);

        // Verify changes
        assertNotNull(updatedOrder);
        assertEquals("Acme Corp", updatedOrder.getCustomerName());
        assertEquals(new BigDecimal("250"), updatedOrder.getArea());
    }


    @Test
    void testRemoveOrder_Success() throws Exception {
        LocalDate date = LocalDate.now().plusDays(1);
        Order order = createValidOrder();
        service.addOrder(date, order);
        List<Order> beforeRemove = service.getOrdersForDate(date);
        assertEquals(1, beforeRemove.size());

        Order removed = service.removeOrder(date, order.getOrderNumber());

        assertNotNull(removed);
        assertEquals("Ada Lovelace", removed.getCustomerName());

        List<Order> afterRemove = orderDao.getOrdersForDate(date);
        assertEquals(0, afterRemove.size());
    }

    @Test
    void testRemoveOrder_NoSuchOrderException() throws Exception {
        LocalDate date = LocalDate.now().plusDays(1);
        int orderNumber = 99;

        assertThrows(NoSuchOrderException.class, () -> {
            service.removeOrder(date, orderNumber);
        });
    }

}