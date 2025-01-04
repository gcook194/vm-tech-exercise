package com.virginmoney.transactionlog.dto;

import java.util.List;

public record TotalOutgoingListDTO(List<TotalOutgoingDTO> totalOutgoingPerCategory) {
}
