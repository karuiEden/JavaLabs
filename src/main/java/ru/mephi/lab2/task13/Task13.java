package ru.mephi.lab2.task13;

import com.opencsv.CSVReaderHeaderAware;

import java.io.FileReader;
import java.util.Map;

public class Task13 {
    static void main(String[] args) throws Exception{
        Map<String, String> values = new CSVReaderHeaderAware(new FileReader("house_prices.csv")).readMap();
        System.out.println(values);


    }
}

/**
 * C:\Users\karui\.jdks\openjdk-25.0.2\bin\javac.exe -cp ".;opencsv-5.12.0.jar" Task13.java
 *
 * C:\Users\karui\.jdks\openjdk-25.0.2\bin\java.exe -classpath ".;.\ru\mephi\lab2\task13\opencsv-5.12.0.jar" ru.mephi.lab2.task13.Task13
 */