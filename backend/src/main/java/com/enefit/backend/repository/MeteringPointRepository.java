package com.enefit.backend.repository;

import com.enefit.backend.entity.MeteringPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeteringPointRepository extends JpaRepository<MeteringPoint, Long> {
    List<MeteringPoint> findAllByCustomerId(Long customerId);
}
