package com.company.domain;

/**
 * Model of a sale message. No null validations should be added according to validation mechanism.
 */
public class Message {
    private MessageType type;
    private double value;
    private String product;
    private int quantity = 1;
    private SaleOperation operation;

    public Message(){}

    public Message(String product, double value){
        type = MessageType.SALE;
        this.product = product;
        this.value = value;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public SaleOperation getOperation() {
        return operation;
    }

    public void setOperation(SaleOperation operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return String.valueOf(operation) +
                " " +
                value +
                " " +
                product;
    }
}
