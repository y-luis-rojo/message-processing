package com.company.services;

import com.company.domain.Message;

/**
 * Interface to be used by external applications
 */
public interface MessageProcessor {

    /**
     * Process message
     * @param message Message
     * @return true if the message was successfully processed, false otherwise
     */
    boolean process(Message message);
}
