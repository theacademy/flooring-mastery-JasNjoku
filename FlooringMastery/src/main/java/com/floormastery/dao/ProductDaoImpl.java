package com.floormastery.dao;

import com.floormastery.dao.exceptions.PersistenceException;
import com.floormastery.dao.interfaces.ProductDao;
import com.floormastery.model.Product;
import com.floormastery.model.Tax;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

@Component
public class ProductDaoImpl implements ProductDao {
    private Map<String, Product> products = new HashMap<>();
    public final String PRODUCT_FILE;
    public static final String DELIMITER = ",";

    public ProductDaoImpl() {
        PRODUCT_FILE = "fileData/Data/Taxes.txt";
    }

    public ProductDaoImpl(String productFile) {
        PRODUCT_FILE = productFile;
    }

    @Override
    public List<Product> getAllProducts() throws PersistenceException {
        loadProducts();
        return new ArrayList<>(products.values());
    }

    private Product unmarshallProduct(String productAsText) {
        String[] productTokens = productAsText.split(DELIMITER);
        Product productFromFile = new Product();

        productFromFile.setProductType(productTokens[0]);
        productFromFile.setCostPerSquareFoot(new BigDecimal(productTokens[1]));
        productFromFile.setLaborCostPerSquareFoot(new BigDecimal(productTokens[2]));

        return productFromFile;
    }

    private void loadProducts() throws PersistenceException {
        Scanner scanner = null;

        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(PRODUCT_FILE)
                    )
            );
        } catch (FileNotFoundException e) {
            throw new PersistenceException("Could not load product data into memory.", e);
        }

        String currentLine;

        // Skip the header
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            Product currentProduct = unmarshallProduct(currentLine);
            products.put(currentProduct.getProductType(), currentProduct);
        }

        scanner.close();
    }
}
