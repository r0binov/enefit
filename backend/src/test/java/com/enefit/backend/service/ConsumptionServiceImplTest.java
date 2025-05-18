package com.enefit.backend.service;// File: backend/src/test/java/com/enefit/backend/service/impl/ConsumptionServiceImplTest.java

import com.enefit.backend.dto.YearlyConsumptionWithPricesDto;
import com.enefit.backend.entity.Customer;
import com.enefit.backend.entity.MeteringPoint;
import com.enefit.backend.integration.EleringIntegration;
import com.enefit.backend.repository.ConsumptionRepository;
import com.enefit.backend.repository.CustomerRepository;
import com.enefit.backend.service.impl.ConsumptionServiceImpl;
import com.enefit.backend.util.UserSessionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConsumptionServiceImplTest {

    private ConsumptionServiceImpl service;
    private ConsumptionRepository consumptionRepository;
    private CustomerRepository customerRepository;
    private UserSessionUtil userSessionUtil;

    @BeforeEach
    void setUp() {
        consumptionRepository = mock(ConsumptionRepository.class);
        EleringIntegration eleringIntegration = mock(EleringIntegration.class);
        customerRepository = mock(CustomerRepository.class);
        userSessionUtil = mock(UserSessionUtil.class);
        service = new ConsumptionServiceImpl(consumptionRepository, eleringIntegration, customerRepository, userSessionUtil);
    }

    @Test
    void returnsConsumptionForCurrentCustomer() {
        Long userId = 1L;
        MeteringPoint mp = new MeteringPoint();
        mp.setId(10L);
        Customer customer = new Customer();
        customer.setId(userId);
        customer.setMeteringPoints(List.of(mp));

        when(userSessionUtil.getCurrentUserId()).thenReturn(userId);
        when(customerRepository.findById(userId)).thenReturn(Optional.of(customer));
        when(consumptionRepository.findByMeteringPointIdIn(List.of(10L))).thenReturn(Collections.emptyList());

        List<YearlyConsumptionWithPricesDto> result = service.getConsumptionsWithPricesForCurrentCustomer();
        assertNotNull(result);
    }

    @Test
    void returnsEmptyListWhenNoMeteringPoints() {
        Long userId = 2L;
        Customer customer = new Customer();
        customer.setId(userId);
        customer.setMeteringPoints(Collections.emptyList());

        when(userSessionUtil.getCurrentUserId()).thenReturn(userId);
        when(customerRepository.findById(userId)).thenReturn(Optional.of(customer));

        List<YearlyConsumptionWithPricesDto> result = service.getConsumptionsWithPricesForCurrentCustomer();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void throwsWhenCustomerNotFound() {
        Long userId = 3L;
        when(userSessionUtil.getCurrentUserId()).thenReturn(userId);
        when(customerRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.getConsumptionsWithPricesForCurrentCustomer());
    }
}