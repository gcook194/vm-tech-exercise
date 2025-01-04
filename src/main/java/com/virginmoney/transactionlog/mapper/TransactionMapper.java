package com.virginmoney.transactionlog.mapper;

import com.virginmoney.transactionlog.dto.*;
import com.virginmoney.transactionlog.enumerator.TransactionType;
import com.virginmoney.transactionlog.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TransactionMapper {

    @Mapping(source = "type", target = "transactionType")
    public abstract TransactionDTO transactionToTransactionDTO(Transaction transaction);

    public abstract List<TransactionDTO> transactionListToTransactionDTOList(List<Transaction> transactions);

    public abstract List<TotalOutgoingDTO> totalOutgoingListToTotalOutgoingDTOList(List<TotalOutgoing> outgoingPerCategory);

    public abstract GetTransactionTotalOutgoingDTO transactionTotalOutgoingToDTO(TransactionTotalOutgoing outgoing);

    public abstract GetAverageMonthlySpendDTO averageMonthlySpendToDTO(AverageMonthlySpend averageSpend);

    public abstract GetAnnualSpendDTO annualSpendToDTO(AnnualSpend annualSpend);

    protected String map(TransactionType transactionType) {
        return transactionType != null ? transactionType.name : null;
    }

    public GetTransactionListDTO transactionListToWrapperDTO(List<Transaction> transactions) {
        final List<TransactionDTO> transactionDTOList = transactionListToTransactionDTOList(transactions);

        return new GetTransactionListDTO(transactionDTOList);
    }

    public TotalOutgoingListDTO totalOutgoingListToWrapperDTO(List<TotalOutgoing> totalOutgoinglist) {
        final List<TotalOutgoingDTO> totalOutgoing = totalOutgoingListToTotalOutgoingDTOList(totalOutgoinglist);

        return new TotalOutgoingListDTO(totalOutgoing);
    }

}
