package com.floormastery.dao.interfaces;

import com.floormastery.dao.exceptions.PersistenceException;
import com.floormastery.model.Product;

import java.util.List;

public interface ProductDao {
    List<Product> getAllProducts() throws PersistenceException;
}
