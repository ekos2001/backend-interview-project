package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.ServiceRepository;
import com.ninjaone.backendinterviewproject.exception.ServiceAlreadyExistsException;
import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.Service;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@org.springframework.stereotype.Service
@Log4j2
public class ServiceService {
    private final ServiceRepository serviceRepository;
    private final DeviceService deviceService;

    public ServiceService(ServiceRepository serviceRepository, DeviceService deviceService) {
        this.serviceRepository = serviceRepository;
        this.deviceService = deviceService;
    }

    public List<Service> getAllServicesByDevice(Long deviceId) {
        return serviceRepository.findByDeviceIdOrderById(deviceId);
    }

    public Service createService(Long deviceId, Service service) throws ServiceAlreadyExistsException {
        log.info("Creating service {} for device {}", service, deviceId);
        if (serviceRepository.findByDeviceIdAndType(deviceId, service.getType()).isPresent()) {
            throw new ServiceAlreadyExistsException();
        }
        Device device = deviceService.getDeviceById(deviceId);
        service.setDevice(new Device(device.getId(), null, null) );
        return serviceRepository.save(service);
    }

    public void deleteDevice(Long serviceId) {
        log.info("Deleting service {}", serviceId);
        try {
            serviceRepository.deleteById(serviceId);
        } catch(EmptyResultDataAccessException ex) {
            String msg = String.format("Service with id %s not found", serviceId);
            throw new EntityNotFoundException(msg);
        }
    }
}
