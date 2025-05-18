package com.enefit.backend.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConsumptionWithPricesDto {
    private String month;
    private String totalPriceWithVat;
    private String totalPriceWithoutVat;
    private BigDecimal consumptionInKw;
    private BigDecimal baselinePricePerMwh;
    private BigDecimal baselinePriceMwhWithVat;
}

