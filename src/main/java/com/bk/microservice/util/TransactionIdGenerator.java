package com.bk.microservice.util;

import java.util.UUID;

public class TransactionIdGenerator {
    public static String ensureTransactionId(String transactionId) {
        return (transactionId == null || transactionId.isEmpty()) ? UUID.randomUUID().toString() : transactionId;
    }
}
