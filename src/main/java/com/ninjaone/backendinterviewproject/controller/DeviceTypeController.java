package com.ninjaone.backendinterviewproject.controller;

import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.DeviceType;
import com.ninjaone.backendinterviewproject.service.DeviceTypeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/settings/device-types")
public class DeviceTypeController {
    private final DeviceTypeService deviceTypeService;

    public DeviceTypeController(DeviceTypeService deviceTypeService) {
        this.deviceTypeService = deviceTypeService;
    }

    @GetMapping
    public Iterable<DeviceType> getAllDeviceTypes() {
        return deviceTypeService.getAllDeviceTypes();
    }
}
