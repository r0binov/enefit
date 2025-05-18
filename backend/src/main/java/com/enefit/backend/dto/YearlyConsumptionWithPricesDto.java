package com.enefit.backend.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class YearlyConsumptionWithPricesDto {
    private int year;
    private List<ConsumptionWithPricesDto> values;
}
