package com.virginmoney.transactionlog.mapper;

import com.virginmoney.transactionlog.dto.GetTransactionListDTO;
import com.virginmoney.transactionlog.dto.TotalOutgoingDTO;
import com.virginmoney.transactionlog.dto.TotalOutgoingListDTO;
import com.virginmoney.transactionlog.dto.TransactionDTO;
import com.virginmoney.transactionlog.enumerator.TransactionType;
import com.virginmoney.transactionlog.model.TotalOutgoing;
import com.virginmoney.transactionlog.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TransactionMapperTest {

    // Although I am instantiating the impl I will only test the non-mapstruct generated methods
    private TransactionMapper mapperUnderTest;

    @BeforeEach
    public void setup() {
        mapperUnderTest = new TransactionMapperImpl();
    }

    @Test
    void transactionListToWrapperDTO_MapsCorrectly() {
        final Transaction transaction = Transaction.builder()
                .id(100L)
                .vendor("Virgin Money")
                .date(LocalDate.now())
                .amount(200F)
                .type(TransactionType.DIRECT_DEBIT)
                .category("Mortgage")
                .build();

        final List<Transaction> transactions = List.of(transaction);


        final GetTransactionListDTO mapperResponse = mapperUnderTest.transactionListToWrapperDTO(transactions);


        assertThat(mapperResponse.transactions().size()).isEqualTo(1);

        final TransactionDTO mappedTransaction = mapperResponse.transactions().get(0);

        assertThat(mappedTransaction.vendor()).isEqualTo(transaction.getVendor());
        assertThat(mappedTransaction.date()).isEqualTo(transaction.getDate());
        assertThat(mappedTransaction.amount()).isEqualTo(transaction.getAmount());
        assertThat(mappedTransaction.transactionType()).isEqualTo(transaction.getType().name);
        assertThat(mappedTransaction.category()).isEqualTo(transaction.getCategory());
    }

    @Test
    void totalOutgoingDTOListToWrapperDTO_MapsCorrectly() {

        final TotalOutgoing totalOutgoingForGroceries = new TotalOutgoing() {
            @Override
            public String getCategory() {
                return "Groceries";
            }

            @Override
            public Float getTotal() {
                return 100F;
            }
        };

        final TotalOutgoing totalOutgoingForMortgage = new TotalOutgoing() {
            @Override
            public String getCategory() {
                return "Mortgage";
            }

            @Override
            public Float getTotal() {
                return 8000F;
            }
        };

        final List<TotalOutgoing> totalOutgoings = List.of(totalOutgoingForGroceries, totalOutgoingForMortgage);


        final TotalOutgoingListDTO mapperResponse = mapperUnderTest.totalOutgoingListToWrapperDTO(totalOutgoings);


        assertThat(mapperResponse.totalOutgoingPerCategory().size())
                .isEqualTo(2);

        final TotalOutgoingDTO groceriesCategoryDTO = mapperResponse.totalOutgoingPerCategory().get(0);

        assertThat(groceriesCategoryDTO.category()).isEqualTo(totalOutgoingForGroceries.getCategory());
        assertThat(groceriesCategoryDTO.total()).isEqualTo(totalOutgoingForGroceries.getTotal());

        final TotalOutgoingDTO mortgageCategoryDTO = mapperResponse.totalOutgoingPerCategory().get(1);

        assertThat(mortgageCategoryDTO.category()).isEqualTo(totalOutgoingForMortgage.getCategory());
        assertThat(mortgageCategoryDTO.total()).isEqualTo(totalOutgoingForMortgage.getTotal());
    }
}