package com.virginmoney.transactionlog.service;

import com.virginmoney.transactionlog.dto.*;
import com.virginmoney.transactionlog.enumerator.TransactionType;
import com.virginmoney.transactionlog.mapper.TransactionMapper;
import com.virginmoney.transactionlog.model.*;
import com.virginmoney.transactionlog.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    public static final String CATEGORY = "Groceries";

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService serviceUnderTest;

    @Test
    void getTransactions_FetchesFromDatabaseAndMapsToDTO() {
        final List<Transaction> transactionsFromDatabase = getTestTransactionData();
        final GetTransactionListDTO dto = getGetTransactionListDTO(transactionsFromDatabase);

        when(transactionRepository.findAll())
                .thenReturn(transactionsFromDatabase);

        when(transactionMapper.transactionListToWrapperDTO(transactionsFromDatabase))
                .thenReturn(dto);

        final GetTransactionListDTO methodResponse = serviceUnderTest.getTransactions();

        assertThat(methodResponse).isEqualTo(dto);
    }

    @Test
    void getTransactionsByCategory_FetchesFromDatabaseAndMapsToDTO() {

        final List<Transaction> transactionsFromDatabase = getTestTransactionData().stream()
                .filter(t -> t.getCategory().equals(CATEGORY))
                .toList();

        final GetTransactionListDTO dto = getGetTransactionListDTO(transactionsFromDatabase);


        when(transactionRepository.findByCategoryIgnoreCaseOrderByDateDesc(CATEGORY))
                .thenReturn(transactionsFromDatabase);

        when(transactionMapper.transactionListToWrapperDTO(transactionsFromDatabase))
                .thenReturn(dto);


        final GetTransactionListDTO methodResponse = serviceUnderTest.getTransactionsByCategory(CATEGORY);


        assertThat(methodResponse).isEqualTo(dto);
    }

    @Test
    void getTotalOutgoingByTransactionCategory_FetchesFromDatabaseAndMapsToDTO() {

        final TransactionTotalOutgoing outgoingForCategory = new TransactionTotalOutgoing() {
            @Override
            public String getCategory() {
                return CATEGORY;
            }

            @Override
            public Float getTotalOutgoing() {
                return 100F;
            }
        };

        final GetTransactionTotalOutgoingDTO dto = new GetTransactionTotalOutgoingDTO(
                outgoingForCategory.getCategory(),
                outgoingForCategory.getTotalOutgoing()
        );


        when(transactionRepository.findTotalOutgoingByTransactionCategory(CATEGORY))
                .thenReturn(outgoingForCategory);
        when(transactionMapper.transactionTotalOutgoingToDTO(outgoingForCategory))
                .thenReturn(dto);



        final GetTransactionTotalOutgoingDTO methodResponse = serviceUnderTest.getTotalOutgoingByTransactionCategory(CATEGORY);


        verify(transactionRepository).findTotalOutgoingByTransactionCategory(CATEGORY);
        assertThat(methodResponse).isEqualTo(dto);
    }

    @Test
    void getAverageMonthlySpendByCategory_FetchesFromDatabaseAndMapsToDTO() {

        final AverageMonthlySpend averageMonthlySpend = new AverageMonthlySpend() {
            @Override
            public String getCategory() {
                return CATEGORY;
            }

            @Override
            public Float getAverageMonthlySpend() {
                return 100F;
            }
        };

        final GetAverageMonthlySpendDTO dto = new GetAverageMonthlySpendDTO(
                averageMonthlySpend.getCategory(),
                averageMonthlySpend.getAverageMonthlySpend()
        );


        when(transactionRepository.getAverageMonthlySpendByCategory(CATEGORY))
                .thenReturn(averageMonthlySpend);
        when(transactionMapper.averageMonthlySpendToDTO(averageMonthlySpend))
                .thenReturn(dto);



        final GetAverageMonthlySpendDTO methodResponse = serviceUnderTest.getAverageMonthlySpendByCategory(CATEGORY);


        verify(transactionRepository).getAverageMonthlySpendByCategory(CATEGORY);
        assertThat(methodResponse).isEqualTo(dto);
    }

    @Test
    void getHighestSpendByCategoryAndYear_FetchesFromDatabaseAndMapsToDTO() {

        final int year = 2020;
        final AnnualSpend highestSpendForCategory = new AnnualSpend() {
            @Override
            public String getCategory() {
                return CATEGORY;
            }

            @Override
            public Float getAnnualSpend() {
                return 100F;
            }
        };

        final GetAnnualSpendDTO dto = new GetAnnualSpendDTO(
                highestSpendForCategory.getCategory(),
                highestSpendForCategory.getAnnualSpend()
        );


        when(transactionRepository.findMaxAmountByCategoryAndYear(CATEGORY, year))
                .thenReturn(highestSpendForCategory);
        when(transactionMapper.annualSpendToDTO(highestSpendForCategory))
                .thenReturn(dto);



        final GetAnnualSpendDTO methodResponse = serviceUnderTest.getHighestSpendByCategoryAndYear(CATEGORY, year);


        verify(transactionRepository).findMaxAmountByCategoryAndYear(CATEGORY, year);
        assertThat(methodResponse).isEqualTo(dto);
    }

    @Test
    void getLowestSpendByCategoryAndYear_FetchesFromDatabaseAndMapsToDTO() {

        final int year = 2020;
        final AnnualSpend lowestSpendForCategory = new AnnualSpend() {
            @Override
            public String getCategory() {
                return CATEGORY;
            }

            @Override
            public Float getAnnualSpend() {
                return 100F;
            }
        };

        final GetAnnualSpendDTO dto = new GetAnnualSpendDTO(
                lowestSpendForCategory.getCategory(),
                lowestSpendForCategory.getAnnualSpend()
        );


        when(transactionRepository.findMinAmountByCategoryAndYear(CATEGORY, year))
                .thenReturn(lowestSpendForCategory);
        when(transactionMapper.annualSpendToDTO(lowestSpendForCategory))
                .thenReturn(dto);



        final GetAnnualSpendDTO methodResponse = serviceUnderTest.getLowestSpendByCategoryAndYear(CATEGORY, year);


        verify(transactionRepository).findMinAmountByCategoryAndYear(CATEGORY, year);
        assertThat(methodResponse).isEqualTo(dto);
    }

    @Test
    void getTotalOutgoingPerCategory_FetchesFromDatabaseAndMapsToDTO() {

        final TotalOutgoing spendForCategory = new TotalOutgoing() {
            @Override
            public String getCategory() {
                return CATEGORY;
            }

            @Override
            public Float getTotal() {
                return 123F;
            }
        };

        final TotalOutgoingDTO spendForCategoryDTO = new TotalOutgoingDTO(
                spendForCategory.getCategory(),
                spendForCategory.getTotal()
        );

        final TotalOutgoingListDTO dto = new TotalOutgoingListDTO(List.of(spendForCategoryDTO));


        when(transactionRepository.getTotalSpendPerCategory())
                .thenReturn(List.of(spendForCategory));
        when(transactionMapper.totalOutgoingListToWrapperDTO(List.of(spendForCategory)))
                .thenReturn(dto);



        final TotalOutgoingListDTO methodResponse = serviceUnderTest.getTotalOutgoingPerCategory();


        verify(transactionRepository).getTotalSpendPerCategory();
        assertThat(methodResponse).isEqualTo(dto);
    }

    private static List<Transaction> getTestTransactionData() {
        final Transaction transaction1 = Transaction.builder()
                .id(100L)
                .category("Groceries")
                .type(TransactionType.CARD)
                .amount(10F)
                .date(LocalDate.now())
                .vendor("Marks & Spencer")
                .build();

        final Transaction transaction2 = Transaction.builder()
                .id(101L)
                .category("Mortgage")
                .type(TransactionType.DIRECT_DEBIT)
                .amount(1000F)
                .date(LocalDate.now())
                .vendor("Virgin Money")
                .build();

        final Transaction transaction3 = Transaction.builder()
                .id(102L)
                .category("Internet")
                .type(TransactionType.DIRECT_DEBIT)
                .amount(25F)
                .date(LocalDate.now())
                .vendor("PureGym")
                .build();

        return List.of(transaction1, transaction2, transaction3);
    }

    private static GetTransactionListDTO getGetTransactionListDTO(List<Transaction> transactions) {
        final List<TransactionDTO> transactionDTOList = transactions.stream()
                .map(t -> new TransactionDTO(
                        t.getDate(),
                        t.getVendor(),
                        t.getType().name,
                        t.getAmount(),
                        t.getCategory()
                ))
                .toList();

        return new GetTransactionListDTO(transactionDTOList);
    }
}