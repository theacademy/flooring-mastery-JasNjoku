package com.floormastery.dao.interfaces;

import com.floormastery.dao.exceptions.PersistenceException;
import com.floormastery.model.Tax;

import java.util.List;

public interface TaxDao {
    List<Tax> getAllTaxes() throws PersistenceException;
}
