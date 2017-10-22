package com.company.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enum to help with message types
 */
public enum MessageType {
    SALE(1), SALEWOCCURRENCE(2), SALEWADJUSTMENT(3);

    private int key;

    MessageType(int key) {
        this.key = key;
    }

    @JsonCreator
    public static MessageType fromInt(int key) {
        Optional<MessageType> messageType = Arrays.stream(MessageType.values()).filter(mt -> mt.getKey() == key).findFirst();
        return messageType.orElse(null);
    }

    @JsonValue
    public int getKey() {
        return key;
    }
}
