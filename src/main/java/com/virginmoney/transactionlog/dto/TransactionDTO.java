package com.virginmoney.transactionlog.dto;

import java.time.LocalDate;

public record TransactionDTO(LocalDate date, String vendor, String transactionType, Float amount, String category) {
}
