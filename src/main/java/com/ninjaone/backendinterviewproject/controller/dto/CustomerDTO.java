package com.ninjaone.backendinterviewproject.controller.dto;

import com.ninjaone.backendinterviewproject.model.Customer;
import lombok.Getter;

@Getter
public class CustomerDTO {
    private Long id;
    private String name;

    public CustomerDTO() {
    }

    public CustomerDTO(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
    }
}
