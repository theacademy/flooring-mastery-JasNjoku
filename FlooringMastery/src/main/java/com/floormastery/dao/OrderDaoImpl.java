package com.floormastery.dao;

import com.floormastery.dao.exceptions.NoSuchOrderException;
import com.floormastery.dao.exceptions.PersistenceException;
import com.floormastery.dao.interfaces.OrderDao;
import com.floormastery.model.Order;
import org.springframework.stereotype.Component;

import java.io.*;
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
    public Order addOrder(LocalDate date, Order order) throws PersistenceException, NoSuchOrderException {
        //If there is no order create new file
        try {
            loadOrders(date);
        } catch (PersistenceException e) {
            orders.put(date, new HashMap<>());
        }

        Order newOrder = orders.get(date).put(getNextOrderNumber(), order);
        writeOrdersToFile();
        return newOrder;
    }

    @Override
    public Order getOrder(LocalDate date, int orderNumber) throws PersistenceException, NoSuchOrderException {
        try {
            loadOrders(date);
        } catch (PersistenceException e) {
            throw new PersistenceException("File: Order"+date.format(FORMATTER)+" does not exist.", e);
        }

        Order order = orders.get(date).get(orderNumber);

        if (order == null) {
            throw new NoSuchOrderException("Order does not exist.");
        }

        return order;
    }

    @Override
    public Order editOrder(LocalDate date, int orderNumber, Order newOrder) throws PersistenceException, NoSuchOrderException {
        try {
            loadOrders(date);
        } catch (PersistenceException e) {
            throw new PersistenceException("File: Order"+date.format(FORMATTER)+" does not exist.", e);
        }

        Order order = orders.get(date).replace(orderNumber, newOrder);
        writeOrdersToFile();
        return order;
    }

    @Override
    public Order removeOrder(LocalDate date, int orderNumber) throws PersistenceException, NoSuchOrderException {
        try {
            loadOrders(date);
        } catch (PersistenceException e) {
            throw new PersistenceException("File: Order"+date.format(FORMATTER)+" does not exist.", e);
        }

        Order order = orders.get(date).remove(orderNumber);
        writeOrdersToFile();
        return order;
    }

    @Override
    public List<Order> getOrdersForDate(LocalDate date) throws NoSuchOrderException {
        try {
            loadOrders(date);
        } catch (PersistenceException e) {
            throw new NoSuchOrderException("Order"+date.format(FORMATTER)+" does not exist.", e);
        }

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

    private String marshallOrder(Order order) {
        String orderAsText = order.getOrderNumber() + DELIMITER
                + order.getCustomerName() + DELIMITER
                + order.getState() + DELIMITER
                + order.getTaxRate() + DELIMITER
                + order.getProductType() + DELIMITER
                + order.getArea() + DELIMITER
                + order.getCostPerSquareFoot() + DELIMITER
                + order.getLaborCostPerSquareFoot() + DELIMITER
                + order.getMaterialCost() + DELIMITER
                + order.getLaborCost() + DELIMITER
                + order.getTax() + DELIMITER
                + order.getTotal();

        return orderAsText;
    }

    private void loadOrders(LocalDate localDate) throws PersistenceException {
        Scanner scanner = null;

        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(ORDER_FOLDER+"/Orders"+localDate.format(FORMATTER)+".txt")
                    )
            );
        } catch (FileNotFoundException e) {
            throw new PersistenceException("Could not load order data into memory.", e);
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

    private void writeOrdersToFile() throws PersistenceException, NoSuchOrderException {
        PrintWriter out;

        for (LocalDate date : orders.keySet()) {
            try {
                out = new PrintWriter(new FileWriter(ORDER_FOLDER+"/Orders"+date.format(FORMATTER)+".txt"));
            } catch (IOException e) {
                throw new PersistenceException("Could not save data.", e);
            }

            String orderAsText;
            List<Order> orderList = this.getOrdersForDate(date);

            for (Order currentOrder: orderList) {
                orderAsText = marshallOrder(currentOrder);
                out.println(orderAsText);
                out.flush();
            }

            out.close();
        }
    }
}
