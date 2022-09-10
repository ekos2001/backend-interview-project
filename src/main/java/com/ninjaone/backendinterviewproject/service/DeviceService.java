package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.DeviceRepository;
import com.ninjaone.backendinterviewproject.exception.DeviceAlreadyExistsException;
import com.ninjaone.backendinterviewproject.model.Customer;
import com.ninjaone.backendinterviewproject.model.Device;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final CustomerService customerService;

    public DeviceService(DeviceRepository deviceRepository, CustomerService customerService) {
        this.deviceRepository = deviceRepository;
        this.customerService = customerService;
    }

    public List<Device> getAllDevicesByCustomer(Long customerId) {
        return deviceRepository.findByCustomerIdOrderById(customerId);
    }

    public Device getDeviceById(Long deviceId) {
        Optional<Device> device = deviceRepository.findById(deviceId);
        if (device.isEmpty()) {
            String msg = String.format("Device with id %s not found", deviceId);
            throw new EntityNotFoundException(msg);
        }
        return device.get();
    }

    public Device createDevice(Long customerId, Device device) throws DeviceAlreadyExistsException {
        log.info("Creating device {} for customer {}", device, customerId);
        if (deviceRepository.findByCustomerIdAndNameAndDeviceType(customerId, device.getName(), device.getDeviceType()).isPresent()) {
            throw new DeviceAlreadyExistsException();
        }
        Customer customer = customerService.getCustomerById(customerId);
        device.setCustomer(new Customer(customer.getId()));
        return deviceRepository.save(device);
    }

    public Device updateDevice(Long deviceId, Device device) {
        log.info("Updating device {} with {}", deviceId, device);
        Device deviceToUpdate = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Device with id %s not found", deviceId)));
        deviceToUpdate.setName(device.getName());
        deviceToUpdate.setDeviceType(device.getDeviceType());
        return deviceRepository.save(deviceToUpdate);
    }

    public void deleteDevice(Long deviceId) {
        log.info("Deleting device {}", deviceId);
        try {
            deviceRepository.deleteById(deviceId);
        } catch(EmptyResultDataAccessException ex) {
            String msg = String.format("Device with id %s not found", deviceId);
            throw new EntityNotFoundException(msg);
        }
    }
}
