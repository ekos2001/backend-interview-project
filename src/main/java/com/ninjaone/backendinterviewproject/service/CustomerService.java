package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.CustomerRepository;
import com.ninjaone.backendinterviewproject.exception.CustomerAlreadyExistsException;
import com.ninjaone.backendinterviewproject.model.Customer;
import com.ninjaone.backendinterviewproject.model.Device;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            String msg = String.format("Customer with id %s not found", customerId);
            throw new EntityNotFoundException(msg);
        }
        return customer.get();
    }

    public BigDecimal getMonthlyCost(Long customerId) {
        log.info("Calculating  monthly cost for customer {} ", customerId);
        Customer customer = getCustomerById(customerId);
        BigDecimal cost = new BigDecimal(0);
        List<Device> devices = customer.getDevices();
        log.info("Customer {} has {} devices", customerId, devices.size());
        for (Device device : devices) {
            cost = cost.add(device.getDeviceType().getCost());
            List<com.ninjaone.backendinterviewproject.model.Service> services = device.getServices();
            log.info("Customer {} has device {} with {} services", customerId, device.getName(), services.size());
            for (com.ninjaone.backendinterviewproject.model.Service service : services) {
                cost = cost.add(service.getType().getCost());
            }
        }
        log.info("Cost for customer {} is {}", customerId, cost);
        return cost;
    }
}
