package com.ninjaone.backendinterviewproject.model;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true, nullable = false)
    private String name;
    @OneToMany(targetEntity=Device.class, mappedBy="customer",cascade= CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Device> devices = new ArrayList<>();

    public Customer() {}

    public Customer(Long id) {
        this(id, null);
    }

    public Customer(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
