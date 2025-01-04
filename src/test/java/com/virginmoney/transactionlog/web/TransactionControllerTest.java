package com.virginmoney.transactionlog.web;

import com.virginmoney.transactionlog.dto.*;
import com.virginmoney.transactionlog.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    public static final String CATEGORY = "Groceries";
    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController controllerUnderTest;

    @Test
    void getTransactions_ReturnsExpectedResults() {

        final List<TransactionDTO> transactions = List.of(
                new TransactionDTO(LocalDate.now(), "Marks & Spencer", "card", 150.0F, "Groceries"),
                new TransactionDTO(LocalDate.now().minusMonths(1), "Marks & Spencer", "card", 18.41F, "Groceries"),
                new TransactionDTO(LocalDate.now().minusMonths(2), "Marks & Spencer", "card", 198.54F, "Groceries")
        );

        final GetTransactionListDTO dto = new GetTransactionListDTO(transactions);

        when(transactionService.getTransactions())
                .thenReturn(dto);

        final ResponseEntity<GetTransactionListDTO> response = controllerUnderTest.getTransactions();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(dto);
    }

    @Test
    void getTotalOutgoingPerCategory_ReturnsExpectedResults() {

        final List<TotalOutgoingDTO> totalOutgoingList = List.of(
                new TotalOutgoingDTO("Groceries", 367.95F),
                new TotalOutgoingDTO("Utilities", 123.45F),
                new TotalOutgoingDTO("Entertainment", 75.50F)
        );

        final TotalOutgoingListDTO dto = new TotalOutgoingListDTO(totalOutgoingList);

        when(transactionService.getTotalOutgoingPerCategory())
                .thenReturn(dto);

        final ResponseEntity<TotalOutgoingListDTO> response = controllerUnderTest.getTotalOutgoingPerCategory();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(dto);
    }

    @Test
    void getTransactionsByCategory_ReturnsExpectedResults() {

        final List<TransactionDTO> transactions = List.of(
                new TransactionDTO(LocalDate.now(), "Marks & Spencer", "card", 150.0F, "Groceries"),
                new TransactionDTO(LocalDate.now().minusMonths(1), "Marks & Spencer", "card", 18.41F, "Groceries")
        );

        final GetTransactionListDTO dto = new GetTransactionListDTO(transactions);

        when(transactionService.getTransactionsByCategory(CATEGORY))
                .thenReturn(dto);

        final ResponseEntity<GetTransactionListDTO> response = controllerUnderTest.getTransactionsByCategory(CATEGORY);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(dto);

        // this check is implicit in the test data but might as well make it explicit
        response.getBody().transactions()
                .forEach(transaction -> assertThat(transaction.category()).isEqualTo(CATEGORY));
    }

    @Test
    void getTotalOutgoingByTransactionCategory_ReturnsExpectedResults() {

        final GetTransactionTotalOutgoingDTO dto = new GetTransactionTotalOutgoingDTO(CATEGORY, 192.31F);

        when(transactionService.getTotalOutgoingByTransactionCategory(CATEGORY))
                .thenReturn(dto);

        final ResponseEntity<GetTransactionTotalOutgoingDTO> response = controllerUnderTest.getTotalOutgoingByTransactionCategory(CATEGORY);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(dto);
    }

    @Test
    void getAverageMonthlySpendByTransactionCategory_ReturnsExpectedResults() {

        final GetAverageMonthlySpendDTO dto = new GetAverageMonthlySpendDTO(CATEGORY, 136F);

        when(transactionService.getAverageMonthlySpendByCategory(CATEGORY))
                .thenReturn(dto);

        final ResponseEntity<GetAverageMonthlySpendDTO> response = controllerUnderTest.getAverageMonthlySpendByTransactionCategory(CATEGORY);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(dto);
    }

    @Test
    void getHighestSpendByCategoryAndYear_ReturnsExpectedResults() {

        final int year = 2024;
        final GetAnnualSpendDTO dto = new GetAnnualSpendDTO(CATEGORY, 411.36F);

        when(transactionService.getHighestSpendByCategoryAndYear(CATEGORY, year))
                .thenReturn(dto);

        final ResponseEntity<GetAnnualSpendDTO> response = controllerUnderTest.getHighestSpendByCategoryAndYear(CATEGORY, year);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(dto);
    }

    @Test
    void getLowestSpendByCategoryAndYear_ReturnsExpectedResults() {

        final int year = 2024;
        final GetAnnualSpendDTO dto = new GetAnnualSpendDTO(CATEGORY, 19.54F);

        when(transactionService.getLowestSpendByCategoryAndYear(CATEGORY, year))
                .thenReturn(dto);

        final ResponseEntity<GetAnnualSpendDTO> response = controllerUnderTest.getLowestSpendByCategoryAndYear(CATEGORY, year);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(dto);
    }
}