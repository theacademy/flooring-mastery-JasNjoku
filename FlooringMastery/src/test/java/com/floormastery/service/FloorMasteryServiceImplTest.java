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
        order.setMaterialCost(order.getArea().multiply(order.getCostPerSquareFoot())); // 871.50
        order.setLaborCost(order.getArea().multiply(order.getLaborCostPerSquareFoot())); // 1033.35
        order.setTax(order.getMaterialCost().add(order.getLaborCost())
                .multiply(order.getTaxRate()).divide(new BigDecimal("100"))); // 476.21
        order.setTotal(order.getMaterialCost().add(order.getLaborCost()).add(order.getTax())); // 2381.06
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

}