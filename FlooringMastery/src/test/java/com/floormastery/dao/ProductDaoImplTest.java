package com.floormastery.dao;

import com.floormastery.dao.exceptions.PersistenceException;
import com.floormastery.dao.interfaces.ProductDao;
import com.floormastery.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductDaoImplTest {
    ProductDao testDao;

    @BeforeEach
    void setUp() {
        String testFile = "fileData/TestFiles/Data/Products.txt";
        testDao = new ProductDaoImpl(testFile);
    }

    @Test
    void getAllProducts() throws PersistenceException {
        List<Product> productList = testDao.getAllProducts();
        assertEquals(4, productList.size(), "Should return first product type.");
    }
}