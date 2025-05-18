package com.enefit.backend.integration.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EleringElectricityPriceResponse {
    private Double centsPerKwh;
    private Double centsPerKwhWithVat;
    private Double eurPerMwh;
    private Double eurPerMwhWithVat;
    private String fromDateTime;
    private String toDateTime;
}
