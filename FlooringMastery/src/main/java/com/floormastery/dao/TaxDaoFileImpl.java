package com.floormastery.dao;

import com.floormastery.dao.exceptions.PersistenceException;
import com.floormastery.dao.interfaces.TaxDao;
import com.floormastery.model.Tax;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

@Component
public class TaxDaoFileImpl implements TaxDao {
    private Map<String, Tax> taxes = new HashMap<>();
    public final String TAX_FILE;
    public static final String DELIMITER = ",";

    public TaxDaoFileImpl() {
        TAX_FILE = "fileData/Orders/Data/Taxes.txt";
    }

    public TaxDaoFileImpl(String taxFile) {
        TAX_FILE = taxFile;
    }

    @Override
    public List<Tax> getAllTaxes() throws PersistenceException {
        loadTaxes();
        return new ArrayList<>(taxes.values());
    }

    private Tax unmarshallTax(String taxAsText) {
        String[] taxTokens = taxAsText.split(DELIMITER);
        Tax taxFromFile = new Tax();

        taxFromFile.setStateAbr(taxTokens[0]);
        taxFromFile.setState(taxTokens[1]);
        taxFromFile.setTaxRate(new BigDecimal(taxTokens[2]));

        return taxFromFile;
    }

    private void loadTaxes() throws PersistenceException {
        Scanner scanner = null;

        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(TAX_FILE)
                    )
            );
        } catch (FileNotFoundException e) {
            throw new PersistenceException("Could not load tax data into memory.", e);
        }

        String currentLine;

        // Skip the header
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            Tax currentTax = unmarshallTax(currentLine);
            taxes.put(currentTax.getStateAbr(), currentTax);
        }

        scanner.close();
    }
}
