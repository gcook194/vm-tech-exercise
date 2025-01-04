package com.virginmoney.transactionlog.service;


import com.virginmoney.transactionlog.dto.*;
import com.virginmoney.transactionlog.mapper.TransactionMapper;
import com.virginmoney.transactionlog.model.*;
import com.virginmoney.transactionlog.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;

    public GetTransactionListDTO getTransactions() {
        final List<Transaction> transactions = transactionRepository.findAll();

        return transactionMapper.transactionListToWrapperDTO(transactions);
    }

    public GetTransactionListDTO getTransactionsByCategory(final String category) {
        final List<Transaction> transactions = transactionRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);

        return transactionMapper.transactionListToWrapperDTO(transactions);
    }

    public GetTransactionTotalOutgoingDTO getTotalOutgoingByTransactionCategory(final String category) {
        final TransactionTotalOutgoing outgoing =  transactionRepository.findTotalOutgoingByTransactionCategory(category);

        return transactionMapper.transactionTotalOutgoingToDTO(outgoing);
    }

    public GetAverageMonthlySpendDTO getAverageMonthlySpendByCategory(final String category) {
        final AverageMonthlySpend averageMonthlySpend =  transactionRepository.getAverageMonthlySpendByCategory(category);

        return transactionMapper.averageMonthlySpendToDTO(averageMonthlySpend);
    }

    public GetAnnualSpendDTO getHighestSpendByCategoryAndYear(String category, int year) {
        final AnnualSpend annualSpend = transactionRepository.findMaxAmountByCategoryAndYear(category, year);

        return transactionMapper.annualSpendToDTO(annualSpend);
    }

    public GetAnnualSpendDTO getLowestSpendByCategoryAndYear(String category, int year) {
        final AnnualSpend annualSpend = transactionRepository.findMinAmountByCategoryAndYear(category, year);

        return transactionMapper.annualSpendToDTO(annualSpend);
    }

    public TotalOutgoingListDTO getTotalOutgoingPerCategory() {
        final List<TotalOutgoing> totalOutgoingPerCategory =  transactionRepository.getTotalSpendPerCategory();

        return transactionMapper.totalOutgoingListToWrapperDTO(totalOutgoingPerCategory);
    }
}
