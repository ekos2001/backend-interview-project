package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.CustomerRepository;
import com.ninjaone.backendinterviewproject.database.DeviceRepository;
import com.ninjaone.backendinterviewproject.database.DeviceTypeRepository;
import com.ninjaone.backendinterviewproject.exception.DeviceAlreadyExistsException;
import com.ninjaone.backendinterviewproject.exception.GenericApiException;
import com.ninjaone.backendinterviewproject.model.Customer;
import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.DeviceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceTest {
    static final Long CUSTOMER_ID = 1L;
    static final Long DEVICE_ID = 1L;

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private DeviceRepository deviceRepository;
    @Mock
    private DeviceTypeRepository deviceTypeRepository;
    @Mock
    private CustomerService customerService;
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private DeviceService deviceService;

    private Device deviceEntity;
    private Customer customerEntity;

    @BeforeEach
    void setup(){
        deviceEntity = new Device(DEVICE_ID, "DeviceName", new DeviceType(1L, "MAC", "Mac", new BigDecimal(1)));
        customerEntity = new Customer(CUSTOMER_ID);
        deviceEntity.setCustomer(customerEntity);
    }

    @Test
    void getAllDevicesByCustomer() {
        when(deviceRepository.findByCustomerIdOrderById(CUSTOMER_ID)).thenReturn(Stream.of(deviceEntity, deviceEntity).collect(Collectors.toList()));
        assertEquals(deviceService.getAllDevicesByCustomer(CUSTOMER_ID).size(), 2);
    }

    @Test
    void getDeviceById() {
        when(deviceRepository.findById(DEVICE_ID)).thenReturn(Optional.of(deviceEntity));
        Device actualEntity = deviceService.getDeviceById(DEVICE_ID);
        assert actualEntity != null;
        assertEquals(deviceEntity, actualEntity);
    }

    @Test
    void getDeviceByIdNotFound() {
        when(deviceRepository.findById(DEVICE_ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> deviceService.getDeviceById(DEVICE_ID));
    }

    @Test
    void createDevice() throws DeviceAlreadyExistsException {
        when(deviceRepository.findByCustomerIdAndNameAndDeviceType(CUSTOMER_ID, deviceEntity.getName(), deviceEntity.getDeviceType()))
                .thenReturn(Optional.empty());
        when(customerService.getCustomerById(CUSTOMER_ID)).thenReturn(customerEntity);
        when(deviceTypeRepository.findById(deviceEntity.getDeviceType().getId())).thenReturn(Optional.of(deviceEntity.getDeviceType()));
        when(deviceRepository.save(deviceEntity)).thenReturn(deviceEntity);
        assertEquals(deviceEntity, deviceService.createDevice(CUSTOMER_ID, deviceEntity));
    }

    @Test
    void createDeviceWrongType() throws DeviceAlreadyExistsException {
        Device deviceNoType = new Device(1L, "Device Name", null);
        when(deviceRepository.findByCustomerIdAndNameAndDeviceType(CUSTOMER_ID, deviceNoType.getName(), deviceNoType.getDeviceType()))
                .thenReturn(Optional.empty());
        when(customerService.getCustomerById(CUSTOMER_ID)).thenReturn(customerEntity);
        assertThrows(GenericApiException.class, () -> deviceService.createDevice(CUSTOMER_ID, deviceNoType));
     }

    @Test
    void createDeviceAlreadyExists() throws DeviceAlreadyExistsException {
        when(deviceRepository.findByCustomerIdAndNameAndDeviceType(CUSTOMER_ID, deviceEntity.getName(), deviceEntity.getDeviceType()))
                .thenReturn(Optional.of(deviceEntity));
        assertThrows(DeviceAlreadyExistsException.class, () -> deviceService.createDevice(CUSTOMER_ID, deviceEntity));
    }

    @Test
    void updateDevice() {
        when(deviceRepository.findById(DEVICE_ID)).thenReturn(Optional.of(deviceEntity));
        when(deviceRepository.save(deviceEntity)).thenReturn(deviceEntity);
        assertEquals(deviceEntity, deviceService.updateDevice(DEVICE_ID, deviceEntity));
    }

    @Test
    void updateDeviceNotFound() {
        when(deviceRepository.findById(DEVICE_ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> deviceService.updateDevice(DEVICE_ID, deviceEntity));
    }

    @Test
    void deleteDevice() {
        when(deviceRepository.findById(DEVICE_ID)).thenReturn(Optional.of(deviceEntity));
        doNothing().when(deviceRepository).deleteById(DEVICE_ID);
        deviceService.deleteDevice(DEVICE_ID);
        verify(deviceRepository, times(1)).deleteById(DEVICE_ID);
    }

    @Test
    void deleteDeviceNotFound() {
        when(deviceRepository.findById(DEVICE_ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> deviceService.deleteDevice(DEVICE_ID));
    }
}