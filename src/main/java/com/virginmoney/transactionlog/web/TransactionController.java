package com.virginmoney.transactionlog.web;

import com.virginmoney.transactionlog.dto.*;
import com.virginmoney.transactionlog.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("transactions")
@RestController
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<GetTransactionListDTO> getTransactions() {
        return ResponseEntity.ok(transactionService.getTransactions());
    }

    @GetMapping("total-outgoing")
    public ResponseEntity<TotalOutgoingListDTO> getTotalOutgoingPerCategory() {
        return ResponseEntity.ok(transactionService.getTotalOutgoingPerCategory());
    }

    @GetMapping("{category}")
    public ResponseEntity<GetTransactionListDTO> getTransactionsByCategory(@PathVariable final String category) {
        return ResponseEntity.ok(transactionService.getTransactionsByCategory(category));
    }

    @GetMapping("{category}/total-outgoing")
    public ResponseEntity<GetTransactionTotalOutgoingDTO> getTotalOutgoingByTransactionCategory(@PathVariable final String category) {
        return ResponseEntity.ok(transactionService.getTotalOutgoingByTransactionCategory(category));
    }

    @GetMapping("{category}/average-monthly")
    public ResponseEntity<GetAverageMonthlySpendDTO> getAverageMonthlySpendByTransactionCategory(@PathVariable final String category) {
        return ResponseEntity.ok(transactionService.getAverageMonthlySpendByCategory(category));
    }

    @GetMapping("{category}/highest-spend/{year}")
    public ResponseEntity<GetAnnualSpendDTO> getHighestSpendByCategoryAndYear(@PathVariable final String category, @PathVariable final int year) {
        return ResponseEntity.ok(transactionService.getHighestSpendByCategoryAndYear(category, year));
    }

    @GetMapping("{category}/lowest-spend/{year}")
    public ResponseEntity<GetAnnualSpendDTO> getLowestSpendByCategoryAndYear(@PathVariable final String category, @PathVariable final int year) {
        return ResponseEntity.ok(transactionService.getLowestSpendByCategoryAndYear(category, year));
    }
}
