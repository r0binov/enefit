package com.enefit.backend.controller;

import com.enefit.backend.dto.YearlyConsumptionWithPricesDto;
import com.enefit.backend.service.ConsumptionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/consumption")
public class ConsumptionController {

    private final ConsumptionService consumptionService;

    public ConsumptionController(ConsumptionService consumptionService) {
        this.consumptionService = consumptionService;
    }

    @GetMapping("/with-prices")
    public List<YearlyConsumptionWithPricesDto> getConsumptionsWithPrices() {
        return consumptionService.getConsumptionsWithPricesForCurrentCustomer();
    }
}