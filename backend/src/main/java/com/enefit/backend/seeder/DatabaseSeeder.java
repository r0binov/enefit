package com.enefit.backend.seeder;

import com.enefit.backend.entity.Consumption;
import com.enefit.backend.entity.Customer;
import com.enefit.backend.entity.MeteringPoint;
import com.enefit.backend.repository.ConsumptionRepository;
import com.enefit.backend.repository.CustomerRepository;
import com.enefit.backend.repository.MeteringPointRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final MeteringPointRepository meteringPointRepository;
    private final ConsumptionRepository consumptionRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(CustomerRepository customerRepository,
                          MeteringPointRepository meteringPointRepository,
                          ConsumptionRepository consumptionRepository,
                          PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.meteringPointRepository = meteringPointRepository;
        this.consumptionRepository = consumptionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Customer customer1 = new Customer(1L,
                "John",
                "Doe",
                "john_doe",
                passwordEncoder.encode("password"),
                null
        );
        Customer customer2 = new Customer(2L,
                "Jane",
                "Smith",
                "jane_smith",
                passwordEncoder.encode("password"),
                null
        );
        customerRepository.saveAll(List.of(customer1, customer2));

        MeteringPoint mp1 = new MeteringPoint(null, "123 Main St", customer1, null);
        MeteringPoint mp2 = new MeteringPoint(null, "456 Elm St", customer2, null);
        MeteringPoint mp3 = new MeteringPoint(null, "789 Oak St", customer2, null);
        meteringPointRepository.saveAll(List.of(mp1, mp2, mp3));

        List<MeteringPoint> meteringPoints = new ArrayList<>();
        meteringPoints.add(mp1);
        meteringPoints.add(mp2);
        meteringPoints.add(mp3);

        Random random = new Random();
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        for (MeteringPoint meteringPoint : meteringPoints) {
            LocalDate date = startDate;
            while (!date.isAfter(endDate)) {
                Consumption consumption = new Consumption();
                double roundedAmount = BigDecimal.valueOf(random.nextDouble() * 100)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
                consumption.setAmount(roundedAmount);
                consumption.setAmountUnit("kW");
                consumption.setConsumptionTime(date.atStartOfDay());
                consumption.setMeteringPoint(meteringPoint);
                consumptionRepository.save(consumption);
                date = date.plusDays(1);
            }
        }
    }
}

