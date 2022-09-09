package com.ninjaone.backendinterviewproject.controller.dto;

import com.ninjaone.backendinterviewproject.model.Service;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ServiceDTO {
    private Long id;
    private Long typeId;
    private String type;
    private String name;
    private BigDecimal cost;
    private Long deviceId;

    public ServiceDTO() {
    }

    public ServiceDTO(Service service) {
        this.id = service.getId();
        this.typeId = service.getType().getId();
        this.type = service.getType().getType();
        this.name = service.getType().getName();
        this.cost = service.getType().getCost();
        this.deviceId = service.getDevice().getId();
    }
}
