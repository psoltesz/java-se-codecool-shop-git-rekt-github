package com.codecool.shop.dbconnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class DBPassword {

    private static final Logger logger = LoggerFactory.getLogger(DBConnection.class);

    public static ArrayList<String> readFile() {
        String filename = "db_config.txt";
        ArrayList<String> records = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;

            while ((line = reader.readLine()) != null) {
                records.add(line);
            }
            reader.close();
            return records;

        } catch (Exception e) {
            logger.error("Exception occurred trying to read '{}'.", filename);
            e.printStackTrace();
            return null;
        }
    }
}

