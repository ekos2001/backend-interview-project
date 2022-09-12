package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.CustomerRepository;
import com.ninjaone.backendinterviewproject.database.DeviceRepository;
import com.ninjaone.backendinterviewproject.database.DeviceTypeRepository;
import com.ninjaone.backendinterviewproject.exception.DeviceAlreadyExistsException;
import com.ninjaone.backendinterviewproject.exception.GenericApiException;
import com.ninjaone.backendinterviewproject.model.Customer;
import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.DeviceType;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@Log4j2
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final DeviceTypeRepository deviceTypeRepository;
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final EntityManager entityManager;

    public DeviceService(DeviceRepository deviceRepository, DeviceTypeRepository deviceTypeRepository, CustomerService customerService, CustomerRepository customerRepository, EntityManager entityManager) {
        this.deviceRepository = deviceRepository;
        this.deviceTypeRepository = deviceTypeRepository;
        this.customerService = customerService;
        this.customerRepository = customerRepository;
        this.entityManager = entityManager;
    }

    public List<Device> getAllDevicesByCustomer(Long customerId) {
        return deviceRepository.findByCustomerIdOrderById(customerId);
    }

    public Device getDeviceById(Long deviceId) {
        return deviceRepository.findById(deviceId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Device with id %s not found", deviceId)));
    }

    @Transactional
    public Device createDevice(Long customerId, Device device) throws DeviceAlreadyExistsException {
        log.info("Creating device {} for customer {}", device, customerId);
        if (deviceRepository.findByCustomerIdAndNameAndDeviceType(customerId, device.getName(), device.getDeviceType()).isPresent()) {
            throw new DeviceAlreadyExistsException();
        }
        Customer customer = customerService.getCustomerById(customerId);
        device.setCustomer(new Customer(customer.getId()));
        device.setDeviceType(getTypeForNewDevice(device));
        customer.setCost(customer.getCost().add(device.getDeviceType().getCost()));
        device = deviceRepository.save(device);
        customerRepository.save(customer);
        entityManager.flush();
        entityManager.refresh(device);
        return device;
    }

    private DeviceType getTypeForNewDevice(Device device) {
        if (device.getDeviceType() != null && device.getDeviceType().getId() != null) {
            return deviceTypeRepository.findById(device.getDeviceType().getId())
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Device type %s is not valid", device.getDeviceType().getId())));
        } else {
            throw new GenericApiException("Device type is required");
        }
    }

    @Transactional
    public Device updateDevice(Long deviceId, Device newDevice) {
        log.info("Updating device {} with {}", deviceId, newDevice);
        Device currentDevice = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Device with id %s not found", deviceId)));
        if (newDevice.getName() != null && !"".equals(newDevice.getName())) {
            currentDevice.setName(newDevice.getName());
        }
        updateDeviceType(currentDevice, newDevice);
        currentDevice = deviceRepository.save(currentDevice);
        entityManager.flush();
        entityManager.refresh(currentDevice);
        return currentDevice;
    }

    private void updateDeviceType(Device currentDevice, Device newDevice) {
        if (newDevice.getDeviceType() != null) {
            Long newDeviceTypeId = newDevice.getDeviceType().getId();
            if (newDeviceTypeId != null &&
                !newDeviceTypeId.equals(currentDevice.getDeviceType().getId())) {
                DeviceType newDeviceType = deviceTypeRepository.findById(newDeviceTypeId)
                        .orElseThrow(() -> new EntityNotFoundException(String.format("Device type with id %s not found", newDeviceTypeId)));
                updateCustomerCostBeforeDeviceUpdate(currentDevice, newDeviceType);
                currentDevice.setDeviceType(newDeviceType);
            }
        }
    }

    private void updateCustomerCostBeforeDeviceUpdate(Device currentDevice, DeviceType newDeviceType) {
        if (!newDeviceType.getCost().equals(currentDevice.getDeviceType().getCost())) {
            BigDecimal currentCost = currentDevice.getCustomer().getCost();
            BigDecimal newCost = currentCost.subtract(currentDevice.getDeviceType().getCost());
            newCost = newCost.add(newDeviceType.getCost());
            currentDevice.getCustomer().setCost(newCost);
        }
    }

    public void deleteDevice(Long deviceId) {
        log.info("Deleting device {}", deviceId);
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Device with id %s not found", deviceId)));
        Customer customer = device.getCustomer();
        updateCustomerCostBeforeDeviceDelete(device, customer);
        deviceRepository.deleteById(deviceId);
        customerRepository.save(customer);
    }

    private void updateCustomerCostBeforeDeviceDelete(Device device, Customer customer) {
        BigDecimal costToSubtract = device.getDeviceType().getCost();
        for (com.ninjaone.backendinterviewproject.model.Service service : device.getServices()) {
            costToSubtract = costToSubtract.add(service.getType().getCost());
        }
        customer.setCost(customer.getCost().subtract(costToSubtract));
    }
}
