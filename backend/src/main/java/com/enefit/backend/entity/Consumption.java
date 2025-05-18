package com.enefit.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "consumption")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Consumption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 0L;
    private Double amount;
    private String amountUnit;
    private LocalDateTime consumptionTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metering_point_id")
    private MeteringPoint meteringPoint;
}
