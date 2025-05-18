package com.enefit.backend.integration;

import com.enefit.backend.integration.dto.EleringElectricityPriceRequest;
import com.enefit.backend.integration.dto.EleringElectricityPriceResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EleringIntegration {

    private final RestTemplate restTemplate;

    public EleringIntegration(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<EleringElectricityPriceResponse> getEnergyPrice(EleringElectricityPriceRequest request) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_DATE_TIME;
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

            // Handle startDateTime
            OffsetDateTime start;
            if (request.getStartDateTime().endsWith("Z") || request.getStartDateTime().matches(".*[+-]\\d\\d:\\d\\d$")) {
                start = OffsetDateTime.parse(request.getStartDateTime(), inputFormatter);
            } else {
                start = LocalDateTime.parse(request.getStartDateTime(), inputFormatter).atOffset(java.time.ZoneOffset.UTC);
            }
            String formattedStart = start.format(outputFormatter);

            // Handle endDateTime
            OffsetDateTime end;
            if (request.getEndDateTime().endsWith("Z") || request.getEndDateTime().matches(".*[+-]\\d\\d:\\d\\d$")) {
                end = OffsetDateTime.parse(request.getEndDateTime(), inputFormatter);
            } else {
                end = LocalDateTime.parse(request.getEndDateTime(), inputFormatter).atOffset(java.time.ZoneOffset.UTC);
            }
            String formattedEnd = end.format(outputFormatter);

            URI uri = UriComponentsBuilder
                    .newInstance()
                    .scheme("https")
                    .host("estfeed.elering.ee")
                    .path("/api/public/v1/energy-price/electricity")
                    .queryParam("startDateTime", formattedStart)
                    .queryParam("endDateTime", formattedEnd)
                    .build()
                    .toUri();

            EleringElectricityPriceResponse[] response = restTemplate.getForObject(uri, EleringElectricityPriceResponse[].class);
            return response != null ? List.of(response) : List.of();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
