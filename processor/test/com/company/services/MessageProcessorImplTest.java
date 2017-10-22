package com.company.services;

import com.company.domain.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tester class for a Message Processor implementation. This tests builds Message instances but also deserialize it from files.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessageProcessorImplTest {

    private static MessageProcessorImpl mp;
    private static Random random;

    //Some products to use for testing
    private static String[] products = new String[]{"apple", "orange", "banana", "strawberry", "grape"};

    @BeforeClass
    public static void setUp() {
        mp = new MessageProcessorImpl(new SalesRepositoryImpl());

        //Save some products
        random = new Random();

        for (int i = 1; i <= 25; i++) {
            String product = products[random.nextInt(products.length)];

            Message message = new Message(product, random.nextInt(100));

            mp.process(message);
        }
    }

    @Test
    public void zzIsFilled() {

        for (int i = 1; i <= 25; i++) {
            String product = products[random.nextInt(products.length)];

            Message message = new Message(product, random.nextInt(100));

            mp.process(message);
        }

        assertEquals(50, mp.getMessageCounter());

        String product = products[random.nextInt(products.length)];

        Message message = new Message(product, random.nextDouble());

        boolean result = mp.process(message);

        assertEquals(false, result);
        assertEquals(50, mp.getMessageCounter());
    }

    @Test
    public void sale() throws IOException {
        Message message = getMessage("messages/sale1.json");

        sale(message);
    }

    @Test
    public void saleWithOccurrences() throws IOException {
        Message message = getMessage("messages/sale2.json");

        sale(message);
    }

    @Test
    public void zAdd() throws IOException {
        Message message = getMessage("messages/add.json");

        adjust(message);
    }

    @Test
    public void zSubtract() throws IOException {
        Message message = getMessage("messages/subtract.json");

        adjust(message);
    }

    @Test
    public void zMultiply() throws IOException {
        Message message = getMessage("messages/multiply.json");

        adjust(message);
    }

    private void adjust(Message message) {
        int numberOfSaleBefore = mp.getNumberOfSales(message.getProduct());
        double salesTotal = mp.getSalesTotalPrice(message.getProduct());

        double diff;

        switch (message.getOperation()) {
            case add:
                diff = salesTotal + (numberOfSaleBefore * message.getValue());
                break;
            case subtract:
                diff = salesTotal - (numberOfSaleBefore * message.getValue());
                break;
            case multiply:
                diff = mp.getSales(message.getProduct()).stream().mapToDouble(s -> s * message.getValue()).sum();
                break;
            default:
                throw new IllegalArgumentException("Unsupported operation.");
        }

        boolean result = mp.process(message);

        assertEquals(true, result);
        assertEquals(numberOfSaleBefore, mp.getNumberOfSales(message.getProduct()));
        assertEquals(diff, mp.getSalesTotalPrice(message.getProduct()));
    }

    private void sale(Message message) {
        int numberOfApplesSaleBefore = mp.getNumberOfSales(message.getProduct());
        double salesTotal = mp.getSalesTotalPrice(message.getProduct());

        boolean result = mp.process(message);

        assertEquals(true, result);
        assertEquals(numberOfApplesSaleBefore + message.getQuantity(), mp.getNumberOfSales(message.getProduct()));
        assertEquals(salesTotal + (message.getValue() * message.getQuantity()), mp.getSalesTotalPrice(message.getProduct()));
    }

    private Message getMessage(String file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(file), Message.class);
    }
}