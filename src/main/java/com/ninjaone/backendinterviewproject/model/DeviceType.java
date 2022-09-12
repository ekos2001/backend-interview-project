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
public class DeviceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String name;
    private BigDecimal cost;

    public DeviceType() {
    }

    public DeviceType(Long id) {
        this.id = id;
    }

    public DeviceType(Long id, String type, String name, BigDecimal cost) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.cost = cost;
    }
}
