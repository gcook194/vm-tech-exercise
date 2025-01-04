package com.virginmoney.transactionlog.enumerator;

public enum TransactionType {
    CARD("card"),
    DIRECT_DEBIT("direct debit"),
    INTERNET("internet");

    public final String name;
    TransactionType(String name) {
        this.name = name;
    }
}
