package com.enefit.backend.repository;

import com.enefit.backend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findCustomerByUserName(String userName);
}
