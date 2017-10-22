package com.company.services;

import com.company.domain.Message;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Implementation of the proposed interface
 */
public class MessageProcessorImpl implements MessageProcessor {

    private int report;
    private int pause;
    private int messageCounter;
    private Set<Message> adjustments;
    private SalesRepository salesRepository;


    MessageProcessorImpl(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
        setConfig();
        adjustments = new HashSet<>();
    }

    /**
     * Reads the configuration parameters (number of messages to report and to pause) from a file config.txt.
     */
    private void setConfig() {

        try (Scanner s = new Scanner(new File("config.txt"))) {
            s.next();
            report = s.nextInt();
            s.next();
            pause = s.nextInt();
        } catch (IOException ex) {
            throw new RuntimeException("Cannot load config properties", ex);
        }
    }

    @Override
    public boolean process(Message message) {

        if (getMessageCounter() >= pause) {
            System.out.println("Reached maximum capacity. No more messages accepted for processing.");
            return false;
        }

        switch (message.getType()) {
            case SALE:
            case SALEWOCCURRENCE:
                handleSale(message);
                break;
            case SALEWADJUSTMENT:
                handleAdjustSale(message);
                adjustments.add(message);
                break;
            default:
                throw new IllegalArgumentException("Unknown message type");
        }
        messageCounter += 1;

        if (getMessageCounter() % report == 0) {
            System.out.println("Report #" + messageCounter / report + " ********************************");
            for (String product : salesRepository.getProducts()) {
                System.out.println(String.format("Product: %s. Number of sales: %d. Total value: %g", product, getNumberOfSales(product), getSalesTotalPrice(product)));
            }
            System.out.println("******************************************");
        }

        if (getMessageCounter() == pause) {
            System.out.println("Pausing application... no more messages will be accepted...");
            System.out.println("Adjustments report ***********************");
            for (Message adjustment : adjustments)
                System.out.println(adjustment);
            System.out.println("******************************************");
        }

        return true;
    }

    /**
     * Handle any sale message (type 1 and 2).
     *
     * @param message Message
     */
    private void handleSale(Message message) {
        for (int i = 1; i <= message.getQuantity(); i++) {
            salesRepository.save(message.getProduct(), message.getValue());
        }
    }

    /**
     * Handle adjust message (type 3).
     *
     * @param message Message
     */
    private void handleAdjustSale(Message message) {
        //Since specifications does not sates clear if the message contains a sale to be stored, and if so,
        // before or after adjustment, I assumed this message is only an adjust operation (stores nothing).
        salesRepository.update(message.getProduct(), message.getOperation(), message.getValue());
    }

    /**
     * Get number of sales of specified product.
     *
     * @param product Product
     * @return Number of sales.
     */
    int getNumberOfSales(String product) {
        return salesRepository.getNumberOfSales(product);
    }

    /**
     * Get total of sales of specified product.
     *
     * @param product Product
     * @return Total saled.
     */
    double getSalesTotalPrice(String product) {
        return salesRepository.getSalesTotalPrice(product);
    }

    /**
     * Get sales of specified product.
     *
     * @param product Product
     * @return List of sales.
     */
    List<Double> getSales(String product) {
        return salesRepository.getSales(product);
    }

    /**
     * Get number of processed messages.
     *
     * @return Number of processed messages.
     */
    int getMessageCounter() {
        return messageCounter;
    }
}
