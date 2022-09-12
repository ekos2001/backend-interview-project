package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.CustomerRepository;
import com.ninjaone.backendinterviewproject.exception.CustomerAlreadyExistsException;
import com.ninjaone.backendinterviewproject.model.Customer;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;

@Service
@Log4j2
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer addCustomer(Customer customer) throws CustomerAlreadyExistsException {
        log.info("Creating customer {}", customer);
        if (customerRepository.findByName(customer.getName()).isPresent()) {
            throw new CustomerAlreadyExistsException();
        }
        return customerRepository.save(customer);
    }

    public Iterable<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Customer with id %s not found", customerId)));
    }

    public BigDecimal getMonthlyCost(Long customerId) {
        Customer customer = getCustomerById(customerId);
        return customer.getCost();
    }

    public BigDecimal getMonthlyCost2(Long customerId) {
        log.info("Calculating  monthly cost second approach for customer {} ", customerId);
        long startTime = System.currentTimeMillis();
        BigDecimal cost = customerRepository.getMonthlyCostByCustomerId(customerId);
        long endTime = System.currentTimeMillis();
        log.info("Cost for customer {} is {}, calculated in {} ms", customerId, cost, endTime-startTime);
        return cost;
    }
}
