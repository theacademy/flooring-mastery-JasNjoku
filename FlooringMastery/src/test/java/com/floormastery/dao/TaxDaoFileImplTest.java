package com.floormastery.dao;

import com.floormastery.dao.exceptions.PersistenceException;
import com.floormastery.dao.interfaces.TaxDao;
import com.floormastery.model.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaxDaoFileImplTest {
    TaxDao testDao;

    @BeforeEach
    void setUp() {
        String testFile = "fileData/TestFiles/Data/Taxes.txt";
        testDao = new TaxDaoFileImpl(testFile);
    }

    @Test
    void getAllTaxes() throws PersistenceException {
        List<Tax> taxList = testDao.getAllTaxes();
        assertEquals("Texas", taxList.get(0).getState(), "Should return first state.");
    }
}