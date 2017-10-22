package com.company.services;

import com.company.domain.SaleOperation;

import java.util.List;
import java.util.Set;

/**
 * Repository interface for sales.
 */
public interface SalesRepository {

    /**
     * Save a new sale.
     * @param product Product.
     * @param price Price.
     */
    void save(String product, double price);

    /**
     * Get sales of specified product.
     * @param product Product.
     * @return List of sales.
     */
    List<Double> getSales(String product);

    /**
     * Get saled products.
     * @return Set of products.
     */
    Set<String> getProducts();

    /**
     * Adjust product sales.
     * @param product Product.
     * @param operation Operation.
     * @param value Value.
     */
    void update(String product, SaleOperation operation, double value);

    /**
     * Get number of sales of specified product.
     * @param product Product.
     * @return Number of sales
     */
    int getNumberOfSales(String product);

    /**
     * Get total of sales of specified product.
     * @param product Product.
     * @return Total of sales.
     */
    double getSalesTotalPrice(String product);
}
