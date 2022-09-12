package com.ninjaone.backendinterviewproject.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@EqualsAndHashCode
@ToString
public class ServiceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String name;
    private BigDecimal cost;

    public ServiceType() {
    }

    public ServiceType(Long id) {
        this(id, null, null, null);
    }

    public ServiceType(Long id, String type, String name, BigDecimal cost) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.cost = cost;
    }
}
