package com.enefit.backend.repository;

import com.enefit.backend.entity.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {
    List<Consumption> findByMeteringPointIdIn(List<Long> ids);
}
