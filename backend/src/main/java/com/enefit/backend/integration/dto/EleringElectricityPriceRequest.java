package com.enefit.backend.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EleringElectricityPriceRequest {
    private String startDateTime;
    private String endDateTime;
}
