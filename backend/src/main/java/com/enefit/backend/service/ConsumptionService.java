package com.enefit.backend.service;

import com.enefit.backend.dto.YearlyConsumptionWithPricesDto;

import java.util.List;

public interface ConsumptionService {
    List<YearlyConsumptionWithPricesDto> getConsumptionsWithPricesForCurrentCustomer();
}
