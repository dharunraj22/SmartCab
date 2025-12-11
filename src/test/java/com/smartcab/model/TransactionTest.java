package com.smartcab.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void testValidTransactionCreationAndGetters() {
        String txId = "tx-123";
        String customerId = "C001";
        TransactionType type = TransactionType.CHARGE;
        double amount = 25.50;
        Instant ts = Instant.parse("2025-01-01T10:00:00Z");

        Transaction tx = new Transaction(txId, customerId, type, amount, ts);

        assertEquals(txId, tx.getTransactionId());
        assertEquals(customerId, tx.getCustomerId());
        assertEquals(type, tx.getType());
        assertEquals(amount, tx.getAmount(), 0.0001);
        assertEquals(ts, tx.getTimestamp());
        String s = tx.toString();
        assertTrue(s.contains(txId));
        assertTrue(s.contains(customerId));
        assertTrue(s.contains(type.name()));
    }

    @Test
    void testConstructorRejectsNullTransactionId() {
        String customerId = "C001";
        TransactionType type = TransactionType.TOPUP;
        double amount = 10.0;
        Instant ts = Instant.now();

        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(null, customerId, type, amount, ts));
    }

    @Test
    void testConstructorRejectsNullCustomerId() {
        String txId = "tx-001";
        TransactionType type = TransactionType.TOPUP;
        double amount = 10.0;
        Instant ts = Instant.now();

        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(txId, null, type, amount, ts));
    }

    @Test
    void testConstructorRejectsNonPositiveAmount() {
        String txId = "tx-004";
        String customerId = "C004";
        TransactionType type = TransactionType.CHARGE;
        Instant ts = Instant.now();

        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(txId, customerId, type, 0.0, ts));

        assertThrows(IllegalArgumentException.class, () ->
                new Transaction(txId, customerId, type, -5.0, ts));
    }

    @Test
    void testEqualsAndHashCodeByTransactionId() {
        String txId = "tx-eq-1";
        String customerA = "C-A";
        String customerB = "C-B";
        Instant tsA = Instant.parse("2025-01-01T00:00:00Z");
        Instant tsB = Instant.parse("2025-01-02T00:00:00Z");

        Transaction t1 = new Transaction(txId, customerA, TransactionType.CHARGE, 10.0, tsA);
        // same transaction id but different other fields
        Transaction t2 = new Transaction(txId, customerB, TransactionType.REFUND, 5.0, tsB);

        assertEquals(t1, t2, "Transactions with same id must be equal");
        assertEquals(t1.hashCode(), t2.hashCode(), "hashCode must match for equal transactions");

        Transaction t3 = new Transaction("tx-eq-2", customerA, TransactionType.CHARGE, 10.0, tsA);
        assertNotEquals(t1, t3);
    }

    @Test
    void testEqualsSymmetryAndNullHandling() {
        String txId = "tx-sym-1";
        Instant ts = Instant.now();
        Transaction t1 = new Transaction(txId, "C1", TransactionType.TOPUP, 20.0, ts);
        Transaction t2 = new Transaction(txId, "C1", TransactionType.TOPUP, 20.0, ts);

        assertTrue(t1.equals(t2));
        assertTrue(t2.equals(t1));

        assertFalse(t1.equals(null));
        assertFalse(t1.equals("some string"));
    }
}
