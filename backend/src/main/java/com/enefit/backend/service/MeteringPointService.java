package com.enefit.backend.service;

import com.enefit.backend.dto.MeteringPointResponse;

import java.util.List;

public interface MeteringPointService {
    List<MeteringPointResponse> getCustomerMeteringPoints();
}
