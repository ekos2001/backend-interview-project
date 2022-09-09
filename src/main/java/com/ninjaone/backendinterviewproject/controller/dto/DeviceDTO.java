package com.ninjaone.backendinterviewproject.controller.dto;

import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.DeviceType;
import lombok.Getter;

@Getter
public class DeviceDTO {
    private Long id;
    private String name;
    private DeviceType type;
    private Long customerId;

    public DeviceDTO() {
    }

    public DeviceDTO(Device device) {
        this.id = device.getId();
        this.name = device.getName();
        this.type = device.getDeviceType();
        this.customerId = device.getCustomer().getId();
    }
}
