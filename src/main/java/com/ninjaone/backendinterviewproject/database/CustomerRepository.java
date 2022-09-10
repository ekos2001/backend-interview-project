package com.ninjaone.backendinterviewproject.database;

import com.ninjaone.backendinterviewproject.model.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    Optional<Customer> findByName(String name);

    @Query(name = "getMonthlyCostByCustomerId")
    BigDecimal getMonthlyCostByCustomerId(@Param("customerId") Long customerId);
}
