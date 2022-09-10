package com.ninjaone.backendinterviewproject.controller;

import com.ninjaone.backendinterviewproject.controller.dto.DeviceDTO;
import com.ninjaone.backendinterviewproject.exception.DeviceAlreadyExistsException;
import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.service.DeviceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/customers")
public class DeviceController {
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/{customerId}/devices")
    public List<DeviceDTO> getAllDevicesByCustomer(@PathVariable Long customerId) {
        List<Device> devices = deviceService.getAllDevicesByCustomer(customerId);
        return devices.stream().map(DeviceDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/devices/{deviceId}")
    public DeviceDTO getDeviceById(@PathVariable Long deviceId) {
        return new DeviceDTO(deviceService.getDeviceById(deviceId));
    }

    @PostMapping("{customerId}/devices")
    @ResponseStatus(HttpStatus.CREATED)
    public DeviceDTO createDevice(@PathVariable Long customerId, @RequestBody @Valid DeviceDTO deviceDTO) throws DeviceAlreadyExistsException {
        Device device = new Device(deviceDTO.getName(), deviceDTO.getType());
        return new DeviceDTO(deviceService.createDevice(customerId, device));
    }

    @PutMapping("/devices/{deviceId}")
    public DeviceDTO updateDevice(@PathVariable Long deviceId, @RequestBody @Valid DeviceDTO deviceDTO) {
        Device device = new Device(deviceDTO.getName(), deviceDTO.getType());
        return new DeviceDTO(deviceService.updateDevice(deviceId, device));
    }

    @DeleteMapping("/devices/{deviceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDevice(@PathVariable Long deviceId) {
        deviceService.deleteDevice(deviceId);
    }
}
