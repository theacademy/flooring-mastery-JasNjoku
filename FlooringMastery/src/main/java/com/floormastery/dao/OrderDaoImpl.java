package com.floormastery.dao;

import com.floormastery.dao.interfaces.OrderDao;
import com.floormastery.model.Order;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class OrderDaoImpl implements OrderDao {
    public static final String ORDER_FOLDER = "fileData/Orders";
    public static final String DELIMITER = ",";
    private Map<LocalDate, Map<Integer, Order>> orders;

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

    private Order unmarshallOrder(String orderAsText) {
        String[] orderTokens = orderAsText.split(DELIMITER);

        Order orderFromFile = new Order();

        orderFromFile.setOrderNumber(Integer.parseInt(orderTokens[0]));
        orderFromFile.setCustomerName(orderTokens[1]);
        orderFromFile.setState(orderTokens[2]);
        orderFromFile.setTaxRate(new BigDecimal(orderTokens[3]));
        orderFromFile.setProductType(orderTokens[4]);
        orderFromFile.setArea(new BigDecimal(orderTokens[5]));
        orderFromFile.setCostPerSquareFoot(new BigDecimal(orderTokens[6]));
        orderFromFile.setLaborCostPerSquareFoot(new BigDecimal(orderTokens[7]));
        orderFromFile.setMaterialCost(new BigDecimal(orderTokens[8]));
        orderFromFile.setLaborCost(new BigDecimal(orderTokens[9]));
        orderFromFile.setTax(new BigDecimal(orderTokens[10]));
        orderFromFile.setTotal(new BigDecimal(orderTokens[11]));

        return orderFromFile;
    }

    private void loadRoster(LocalDate localDate) {
        Scanner scanner = null;

        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(ORDER_FOLDER+"/"+localDate.toString())
                    )
            );
        } catch (FileNotFoundException e) {
            //THROW CUSTOM ERROR
        }

        String currentLine;
        Order currentOrder;

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentOrder = unmarshallOrder(currentLine);
            
            //orders.put(localDate, currentOrder);
        }
        
        scanner.close();
    }
}
