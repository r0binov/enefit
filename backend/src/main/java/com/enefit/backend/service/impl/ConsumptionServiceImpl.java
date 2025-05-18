package com.enefit.backend.service.impl;

import com.enefit.backend.dto.ConsumptionWithPricesDto;
import com.enefit.backend.dto.YearlyConsumptionWithPricesDto;
import com.enefit.backend.entity.Consumption;
import com.enefit.backend.entity.Customer;
import com.enefit.backend.entity.MeteringPoint;
import com.enefit.backend.integration.EleringIntegration;
import com.enefit.backend.integration.dto.EleringElectricityPriceRequest;
import com.enefit.backend.integration.dto.EleringElectricityPriceResponse;
import com.enefit.backend.repository.ConsumptionRepository;
import com.enefit.backend.repository.CustomerRepository;
import com.enefit.backend.service.ConsumptionService;
import com.enefit.backend.util.UserSessionUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConsumptionServiceImpl implements ConsumptionService {

    private final EleringIntegration eleringIntegration;
    private final ConsumptionRepository consumptionRepository;
    private final CustomerRepository customerRepository;
    private final UserSessionUtil userSessionUtil;

    public ConsumptionServiceImpl(
            ConsumptionRepository consumptionRepository,
            EleringIntegration eleringIntegration,
            CustomerRepository customerRepository,
            UserSessionUtil userSessionUtil
    ) {
        this.consumptionRepository = consumptionRepository;
        this.eleringIntegration = eleringIntegration;
        this.customerRepository = customerRepository;
        this.userSessionUtil = userSessionUtil;
    }

    @Override
    public List<YearlyConsumptionWithPricesDto> getConsumptionsWithPricesForCurrentCustomer() {
        Long customerId = userSessionUtil.getCurrentUserId();
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        List<Long> meteringPointIds = customer.getMeteringPoints().stream()
                .map(MeteringPoint::getId)
                .collect(Collectors.toList());
        return getConsumptionsWithPrices(meteringPointIds);
    }

    public List<YearlyConsumptionWithPricesDto> getConsumptionsWithPrices(List<Long> meteringPointIds) {
        List<Consumption> consumptions = consumptionRepository.findByMeteringPointIdIn(meteringPointIds);
        Map<YearMonth, List<Consumption>> byMonth = groupConsumptionsByMonth(consumptions);

        List<Map.Entry<YearMonth, List<Consumption>>> sortedByMonth = byMonth.entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getKey().getMonthValue()))
                .toList();

        Map<Integer, List<ConsumptionWithPricesDto>> byYear = new TreeMap<>();

        for (Map.Entry<YearMonth, List<Consumption>> entry : sortedByMonth) {
            YearMonth yearMonth = entry.getKey();
            List<Consumption> monthConsumptions = entry.getValue();

            BigDecimal totalConsumption = calculateTotalConsumption(monthConsumptions);
            EleringPriceData priceData = fetchEleringPrices(yearMonth, totalConsumption);

            ConsumptionWithPricesDto dto = compileConsumptionWithPricesDto(yearMonth, totalConsumption, priceData);

            byYear.computeIfAbsent(yearMonth.getYear(), y -> new ArrayList<>()).add(dto);
        }

        List<YearlyConsumptionWithPricesDto> result = new ArrayList<>();
        for (Map.Entry<Integer, List<ConsumptionWithPricesDto>> entry : byYear.entrySet()) {
            result.add(new YearlyConsumptionWithPricesDto(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    private Map<YearMonth, List<Consumption>> groupConsumptionsByMonth(List<Consumption> consumptions) {
        return consumptions.stream()
                .collect(Collectors.groupingBy(c -> YearMonth.from(c.getConsumptionTime())));
    }

    private BigDecimal calculateTotalConsumption(List<Consumption> monthConsumptions) {
        return monthConsumptions.stream()
                .map(c -> BigDecimal.valueOf(c.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private EleringPriceData fetchEleringPrices(YearMonth yearMonth, BigDecimal totalConsumption) {
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime end = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        EleringElectricityPriceRequest priceRequest = new EleringElectricityPriceRequest();
        priceRequest.setStartDateTime(start.toString());
        priceRequest.setEndDateTime(end.toString());
        List<EleringElectricityPriceResponse> priceResponse = eleringIntegration.getEnergyPrice(priceRequest);

        BigDecimal avgWithVat = BigDecimal.ZERO;
        BigDecimal avgWithoutVat = BigDecimal.ZERO;
        BigDecimal avgEurPerMwh = BigDecimal.ZERO;
        BigDecimal avgEurPerMwhWithVat = BigDecimal.ZERO;

        if (priceResponse != null && !priceResponse.isEmpty()) {
            avgWithVat = priceResponse.stream()
                    .map(d -> BigDecimal.valueOf(d.getEurPerMwhWithVat()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(priceResponse.size()), RoundingMode.HALF_UP);

            avgWithoutVat = priceResponse.stream()
                    .map(d -> BigDecimal.valueOf(d.getEurPerMwh()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(priceResponse.size()), RoundingMode.HALF_UP);

            avgEurPerMwh = priceResponse.stream()
                    .map(d -> BigDecimal.valueOf(d.getEurPerMwh()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(priceResponse.size()), 2, RoundingMode.HALF_UP);

            avgEurPerMwhWithVat = priceResponse.stream()
                    .map(d -> BigDecimal.valueOf(d.getEurPerMwhWithVat()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(priceResponse.size()), 2, RoundingMode.HALF_UP);
        }

        BigDecimal consumptionMWh = totalConsumption.divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP);

        BigDecimal totalPriceWithVat = consumptionMWh.multiply(avgWithVat);
        BigDecimal totalPriceWithoutVat = consumptionMWh.multiply(avgWithoutVat);

        return new EleringPriceData(totalPriceWithVat, totalPriceWithoutVat, avgEurPerMwh, avgEurPerMwhWithVat);
    }

    private record EleringPriceData(
            BigDecimal totalPriceWithVat,
            BigDecimal totalPriceWithoutVat,
            BigDecimal avgEurPerMwh,
            BigDecimal avgEurPerMwhWithVat
    ) {
    }

    private ConsumptionWithPricesDto compileConsumptionWithPricesDto(YearMonth yearMonth, BigDecimal totalConsumption, EleringPriceData priceData) {
        String monthName = yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        return new ConsumptionWithPricesDto(
                monthName,
                priceData.totalPriceWithVat.setScale(2, RoundingMode.HALF_UP).toString(),
                priceData.totalPriceWithoutVat.setScale(2, RoundingMode.HALF_UP).toString(),
                totalConsumption,
                priceData.avgEurPerMwh.setScale(2, RoundingMode.HALF_UP),
                priceData.avgEurPerMwhWithVat.setScale(2, RoundingMode.HALF_UP)
        );
    }
}