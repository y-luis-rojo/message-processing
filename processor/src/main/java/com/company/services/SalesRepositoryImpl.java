package com.company.services;

import com.company.domain.SaleOperation;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Memory implementation of a sale repository. Uses a Map to store records.
 */
public class SalesRepositoryImpl implements SalesRepository {

    private Map<String, List<Double>> salesRecord;

    SalesRepositoryImpl(){
        salesRecord = new HashMap<>();
    }

    @Override
    public void save(String product, double price) {
        if (!salesRecord.containsKey(product))
            salesRecord.put(product, new ArrayList<>());

        salesRecord.get(product).add(price);
    }

    @Override
    public List<Double> getSales(String product) {
        return salesRecord.get(product);
    }

    @Override
    public Set<String> getProducts() {
        return salesRecord.keySet();
    }

    @Override
    public void update(String product, SaleOperation operation, double value) {

        if (!salesRecord.containsKey(product))
            return;

        List<Double> mapped;

        switch (operation){
            case add:
                mapped = getSales(product).stream().map(i -> i + value).collect(Collectors.toList());
                break;
            case subtract:
                mapped = getSales(product).stream().map(i -> i - value).collect(Collectors.toList());
                break;
            case multiply:
                mapped = getSales(product).stream().map(i -> i * value).collect(Collectors.toList());
                break;
            default:
                throw new IllegalArgumentException("Unsupported operation.");
        }

        salesRecord.put(product, mapped);
    }

    @Override
    public int getNumberOfSales(String product) {

        if (salesRecord.containsKey(product))
            return salesRecord.get(product).size();
        return 0;
    }

    @Override
    public double getSalesTotalPrice(String product) {
        if (salesRecord.containsKey(product))
            return salesRecord.get(product).stream().mapToDouble(i -> i).sum();

        return 0;
    }
}
