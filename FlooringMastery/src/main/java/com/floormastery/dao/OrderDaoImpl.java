package com.floormastery.dao;

import com.floormastery.dao.interfaces.OrderDao;
import com.floormastery.model.Order;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class OrderDaoImpl implements OrderDao {
    public static final String ORDER_FOLDER = "fileData/Orders";
    public static final String DELIMITER = ",";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("_MMddyyy");
    private Map<LocalDate, Map<Integer, Order>> orders = new HashMap<>();

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
        loadRoster(date);
        Map<Integer, Order> ordersForDate = orders.get(date);
        return new ArrayList<>(ordersForDate.values());
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
                            new FileReader(ORDER_FOLDER+"/Orders"+localDate.format(FORMATTER)+".txt")
                    )
            );
        } catch (FileNotFoundException e) {
            //THROW CUSTOM ERROR
            System.out.println(e);
        }

        String currentLine;
        Map<Integer, Order> ordersMap = new HashMap<>();

        // Skip the header
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            Order currentOrder = unmarshallOrder(currentLine);
            
            ordersMap.put(currentOrder.getOrderNumber(), currentOrder);
        }

        orders.put(localDate, ordersMap);
        scanner.close();
    }
}
