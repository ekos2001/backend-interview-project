package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.CustomerRepository;
import com.ninjaone.backendinterviewproject.database.ServiceRepository;
import com.ninjaone.backendinterviewproject.database.ServiceTypeRepository;
import com.ninjaone.backendinterviewproject.exception.GenericApiException;
import com.ninjaone.backendinterviewproject.exception.ServiceAlreadyExistsException;
import com.ninjaone.backendinterviewproject.model.Customer;
import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.Service;
import com.ninjaone.backendinterviewproject.model.ServiceType;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@org.springframework.stereotype.Service
@Log4j2
public class ServiceService {
    private final ServiceRepository serviceRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final DeviceService deviceService;
    private final CustomerRepository customerRepository;
    private final EntityManager entityManager;

    public ServiceService(ServiceRepository serviceRepository, ServiceTypeRepository serviceTypeRepository, DeviceService deviceService, CustomerRepository customerRepository, EntityManager entityManager) {
        this.serviceRepository = serviceRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.deviceService = deviceService;
        this.customerRepository = customerRepository;
        this.entityManager = entityManager;
    }

    public List<Service> getAllServicesByDevice(Long deviceId) {
        return serviceRepository.findByDeviceIdOrderById(deviceId);
    }

    @Transactional
    public Service createService(Long deviceId, Service service) throws ServiceAlreadyExistsException {
        log.info("Creating service {} for device {}", service, deviceId);
        if (serviceRepository.findByDeviceIdAndType(deviceId, service.getType()).isPresent()) {
            throw new ServiceAlreadyExistsException();
        }
        Device device = deviceService.getDeviceById(deviceId);
        service.setDevice(new Device(device.getId(), null, null) );
        service.setType(getTypeForNewService(service));

        Customer customer = device.getCustomer();
        customer.setCost(customer.getCost().add(service.getType().getCost()));

        service = serviceRepository.save(service);
        customerRepository.save(customer);
        entityManager.flush();
        entityManager.refresh(service);
        return service;
    }

    private ServiceType getTypeForNewService(Service service) {
        if (service.getType() != null && service.getType().getId() != null) {
            return serviceTypeRepository.findById(service.getType().getId())
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Service type %s is not valid", service.getType().getId())));
        } else {
            throw new GenericApiException("Service type is required");
        }
    }

    public void deleteService(Long serviceId) {
        log.info("Deleting service {}", serviceId);
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Service with id %s not found", serviceId)));
        Customer customer = service.getDevice().getCustomer();
        customer.setCost(customer.getCost().subtract(service.getType().getCost()));
        serviceRepository.deleteById(serviceId);
        customerRepository.save(customer);
    }
}
