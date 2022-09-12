package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.CustomerRepository;
import com.ninjaone.backendinterviewproject.exception.CustomerAlreadyExistsException;
import com.ninjaone.backendinterviewproject.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    static final Long CUSTOMER_ID = 1l;
    static final String CUSTOMER_NAME = "Test Customer";
    @InjectMocks
    private CustomerService customerService;
    @Mock
    private CustomerRepository customerRepository;
    private Customer customerEntity;

    @BeforeEach
    void setUp() {
        customerEntity = new Customer(CUSTOMER_ID, CUSTOMER_NAME);
    }

    @Test
    void addCustomer() throws CustomerAlreadyExistsException {
        when(customerRepository.findByName(CUSTOMER_NAME)).thenReturn(Optional.empty());
        when(customerRepository.save(customerEntity)).thenReturn(customerEntity);
        assertEquals(customerEntity, customerService.addCustomer(customerEntity));
    }

    @Test
    void addCustomerAlreadyExists() throws CustomerAlreadyExistsException {
        when(customerRepository.findByName(CUSTOMER_NAME)).thenReturn(Optional.of(customerEntity));
        assertThrows(CustomerAlreadyExistsException.class,() -> customerService.addCustomer(customerEntity));
    }

    @Test
    void getAllCustomers() {
        when(customerRepository.findAll()).thenReturn(Stream.of(customerEntity, customerEntity).collect(Collectors.toList()));
        assertEquals(((List<Customer>)customerService.getAllCustomers()).size(), 2);
    }

    @Test
    void getCustomerById() {
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customerEntity));
        assertEquals(customerEntity, customerService.getCustomerById(CUSTOMER_ID));
    }

    @Test
    void getCustomerByIdNotFound() {
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,() -> customerService.getCustomerById(CUSTOMER_ID));
    }

    @Test
    void getMonthlyCost() {
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customerEntity));
        assertEquals(customerEntity.getCost(), customerService.getMonthlyCost(CUSTOMER_ID));
    }

    @Test
    void getMonthlyCost2() {
        when(customerRepository.getMonthlyCostByCustomerId(CUSTOMER_ID)).thenReturn(new BigDecimal(1));
        assertEquals(new BigDecimal(1), customerService.getMonthlyCost2(CUSTOMER_ID));
    }
}