package com.virginmoney.transactionlog.dto;

import java.util.List;

public record GetTransactionListDTO(List<TransactionDTO> transactions) {
}
