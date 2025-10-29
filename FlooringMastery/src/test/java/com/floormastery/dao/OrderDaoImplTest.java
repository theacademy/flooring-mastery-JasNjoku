package com.floormastery.dao;

import com.floormastery.dao.interfaces.OrderDao;
import com.floormastery.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class OrderDaoImplTest {
    OrderDao testDao;

    public OrderDaoImplTest() {}

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

        testDao = new OrderDaoImpl(testFolder);
    }

    @Test
    public void testAddOrderAndRetrieve() throws Exception {
        // Arrange
        LocalDate testDate = LocalDate.of(2025, 10, 29);
        Order order = new Order();

        order.setCustomerName("Ada Lovelace");
        order.setState("CA");
        order.setTaxRate(new BigDecimal("25.00"));
        order.setProductType("Tile");
        order.setArea(new BigDecimal("249.00"));
        order.setCostPerSquareFoot(new BigDecimal("3.50"));
        order.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        order.setMaterialCost(new BigDecimal("871.50"));
        order.setLaborCost(new BigDecimal("1033.35"));
        order.setTax(new BigDecimal("476.21"));
        order.setTotal(new BigDecimal("2381.06"));

        // Act
        testDao.addOrder(testDate, order);

        // Retrieve the same order
        Order retrieved = testDao.getOrder(testDate, 1);

        // Assert
        assertNotNull(retrieved, "Retrieved order should not be null.");
        assertEquals(order.getCustomerName(), retrieved.getCustomerName(), "Customer name should match.");
        assertEquals(order.getProductType(), retrieved.getProductType(), "Product type should match.");
        assertEquals(order.getTotal(), retrieved.getTotal(), "Total should match.");
    }

    @Test
    public void testEditOrder() throws Exception {
        // Arrange
        LocalDate testDate = LocalDate.of(2025, 10, 29);
        Order order = new Order();
        order.setCustomerName("Ada Lovelace");
        order.setState("CA");
        order.setTaxRate(new BigDecimal("25.00"));
        order.setProductType("Tile");
        order.setArea(new BigDecimal("249.00"));
        order.setCostPerSquareFoot(new BigDecimal("3.50"));
        order.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        order.setMaterialCost(new BigDecimal("871.50"));
        order.setLaborCost(new BigDecimal("1033.35"));
        order.setTax(new BigDecimal("476.21"));
        order.setTotal(new BigDecimal("2381.06"));
        testDao.addOrder(testDate, order);

        // Act
        order.setCustomerName("Alan Turing"); // modify customer
        Order edited = testDao.editOrder(testDate, 1, order);

        // Assert
        assertEquals("Alan Turing", edited.getCustomerName(), "Customer name should be updated.");
        Order retrieved = testDao.getOrder(testDate, 1);
        assertEquals("Alan Turing", retrieved.getCustomerName(), "Edited order should be retrievable with new name.");
    }

    @Test
    public void testRemoveOrder() throws Exception {
        // Arrange
        LocalDate testDate = LocalDate.of(2025, 10, 29);
        Order order = new Order();
        order.setCustomerName("Ada Lovelace");
        order.setState("CA");
        order.setTaxRate(new BigDecimal("25.00"));
        order.setProductType("Tile");
        order.setArea(new BigDecimal("249.00"));
        order.setCostPerSquareFoot(new BigDecimal("3.50"));
        order.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        order.setMaterialCost(new BigDecimal("871.50"));
        order.setLaborCost(new BigDecimal("1033.35"));
        order.setTax(new BigDecimal("476.21"));
        order.setTotal(new BigDecimal("2381.06"));
        testDao.addOrder(testDate, order);

        // Act
        Order removed = testDao.removeOrder(testDate, 1);

        // Assert
        assertEquals("Ada Lovelace", removed.getCustomerName(), "Removed order should match.");
    }

    @Test
    public void testGetOrdersForDate() throws Exception {
        LocalDate testDate = LocalDate.of(2025, 10, 29);
        Order order = new Order();
        order.setCustomerName("Ada Lovelace");
        order.setState("CA");
        order.setTaxRate(new BigDecimal("25.00"));
        order.setProductType("Tile");
        order.setArea(new BigDecimal("249.00"));
        order.setCostPerSquareFoot(new BigDecimal("3.50"));
        order.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        order.setMaterialCost(new BigDecimal("871.50"));
        order.setLaborCost(new BigDecimal("1033.35"));
        order.setTax(new BigDecimal("476.21"));
        order.setTotal(new BigDecimal("2381.06"));
        testDao.addOrder(testDate, order);

        List<Order> orders = testDao.getOrdersForDate(testDate);
        assertEquals(1, orders.size(), "Should return two orders for the date.");
        assertTrue(orders.stream().anyMatch(o -> o.getCustomerName().equals("Ada Lovelace")));
    }

}