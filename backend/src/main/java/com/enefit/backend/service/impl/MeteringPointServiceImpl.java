package com.enefit.backend.service.impl;

import com.enefit.backend.dto.MeteringPointResponse;
import com.enefit.backend.entity.MeteringPoint;
import com.enefit.backend.repository.MeteringPointRepository;
import com.enefit.backend.service.MeteringPointService;
import com.enefit.backend.util.UserSessionUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeteringPointServiceImpl implements MeteringPointService {

    private final MeteringPointRepository meteringPointRepository;
    private final UserSessionUtil userSessionUtil;

    public MeteringPointServiceImpl(MeteringPointRepository meteringPointRepository, UserSessionUtil userSessionUtil) {
        this.meteringPointRepository = meteringPointRepository;
        this.userSessionUtil = userSessionUtil;
    }

    @Override
    public List<MeteringPointResponse> getCustomerMeteringPoints() {
        Long customerId = userSessionUtil.getCurrentUserId();
        List<MeteringPoint> points = meteringPointRepository.findAllByCustomerId(customerId);
        return points.stream()
                .map(mp -> new MeteringPointResponse(mp.getId(), mp.getAddress()))
                .collect(Collectors.toList());
    }
}
