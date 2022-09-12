package com.ninjaone.backendinterviewproject.controller;

import com.ninjaone.backendinterviewproject.controller.dto.CostDTO;
import com.ninjaone.backendinterviewproject.controller.dto.CustomerDTO;
import com.ninjaone.backendinterviewproject.exception.CustomerAlreadyExistsException;
import com.ninjaone.backendinterviewproject.model.Customer;
import com.ninjaone.backendinterviewproject.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO createCustomer(@RequestBody @Valid CustomerDTO customer) throws CustomerAlreadyExistsException {
        Customer customerToCreate = new Customer(customer.getName());
        return new CustomerDTO(customerService.addCustomer(customerToCreate));
    }

    @GetMapping
    public Iterable<CustomerDTO> getAllCustomers() {
        List<CustomerDTO> customers = new ArrayList<>();
        customerService.getAllCustomers().forEach(customer -> customers.add(new CustomerDTO(customer)));
        return customers;
    }

    @GetMapping("/{customerId}/cost")
    public CostDTO getMonthlyCost(@PathVariable Long customerId) {
        return new CostDTO(customerService.getMonthlyCost(customerId));
    }

    @GetMapping("/{customerId}/cost2")
    public CostDTO getMonthlyCost2(@PathVariable Long customerId) {
        return new CostDTO(customerService.getMonthlyCost2(customerId));
    }
}
