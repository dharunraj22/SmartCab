package com.smartcab.model;

import java.time.Instant;

public class Transaction {
    final String transactionId;
    final String customerId;
    final TransactionType type;
    final double amount;
    final Instant timestamp;

    public Transaction(String transactionId, String customerId, TransactionType type, double amount, Instant timestamp) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", type=" + type +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Transaction other = (Transaction) obj;
        return this.transactionId.equals(other.transactionId);
    }

    @Override
    public int hashCode() {
        return transactionId.hashCode();    
    }
}
