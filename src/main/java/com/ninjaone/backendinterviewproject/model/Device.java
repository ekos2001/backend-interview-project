package com.ninjaone.backendinterviewproject.model;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"name", "device_type_id", "customer_id"})})
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne()
    @JoinColumn(name="device_type_id", referencedColumnName = "id", nullable = false)
    private DeviceType deviceType;

    @OneToMany(targetEntity=Service.class, mappedBy="device", cascade= CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Service> services = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name="customer_id", referencedColumnName = "id", updatable = false, nullable = false)
    private Customer customer;

    public Device() {
    }

    public Device(String name, DeviceType deviceType) {
        this(null, name, deviceType);
    }

    public Device(Long id, String name, DeviceType deviceType) {
        this.id = id;
        this.name = name;
        this.deviceType = deviceType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
