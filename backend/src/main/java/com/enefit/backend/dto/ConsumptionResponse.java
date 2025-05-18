package com.enefit.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConsumptionResponse {
    private Long meteringPointId;
    private Double amount;
    private String amountUnit;
    private LocalDateTime consumptionTime;
}
