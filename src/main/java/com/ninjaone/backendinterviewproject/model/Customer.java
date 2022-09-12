package com.ninjaone.backendinterviewproject.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@EqualsAndHashCode
@NamedQuery(name = "getMonthlyCostByCustomerId",
        query = "select " +
                "coalesce((select sum(dt.cost) from Device d join DeviceType dt on d.deviceType.id = dt.id " +
                "where d.customer.id = :customerId), 0) + " +
                "coalesce((select sum(st.cost) from Device d join Service s on s.device.id = d.id join ServiceType st on s.type.id=st.id " +
                "where d.customer.id=:customerId), 0) from Customer c where c.id = :customerId"
)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true, nullable = false)
    private String name;
    private BigDecimal cost = BigDecimal.ZERO;
    @OneToMany(targetEntity=Device.class, mappedBy="customer",cascade= CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Device> devices = new ArrayList<>();

    public Customer() {}

    public Customer(Long id) {
        this(id, null);
    }

    public Customer(String name) {
        this(null, name);
    }

    public Customer(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
