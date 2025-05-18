package com.enefit.backend.controller;

import com.enefit.backend.dto.MeteringPointResponse;
import com.enefit.backend.service.MeteringPointService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/metering-points")
public class MeteringPointController {

    private final MeteringPointService meteringPointService;

    public MeteringPointController(MeteringPointService meteringPointService) {
        this.meteringPointService = meteringPointService;
    }

    @GetMapping
    public List<MeteringPointResponse> getCustomerMeteringPoints() {
        return meteringPointService.getCustomerMeteringPoints();
    }
}