package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.CustomerRepository;
import com.ninjaone.backendinterviewproject.database.ServiceRepository;
import com.ninjaone.backendinterviewproject.database.ServiceTypeRepository;
import com.ninjaone.backendinterviewproject.exception.GenericApiException;
import com.ninjaone.backendinterviewproject.exception.ServiceAlreadyExistsException;
import com.ninjaone.backendinterviewproject.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceServiceTest {
    static final Long SERVICE_ID = 1L;
    static final Long DEVICE_ID = 1L;
    @InjectMocks
    private ServiceService serviceService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ServiceRepository serviceRepository;
    @Mock
    private ServiceTypeRepository serviceTypeRepository;
    @Mock
    private DeviceService deviceService;
    @Mock
    private EntityManager entityManager;
    private Service service;
    private Device device;
    private Customer customer;

    @BeforeEach
    void setUp() {
        service = new Service(SERVICE_ID, new ServiceType(1L, "BACKUP", "Backup", new BigDecimal(1)));
        device = new Device(DEVICE_ID, "DeviceName", new DeviceType(1L, "MAC", "Mac", new BigDecimal(1)));
        service.setDevice(device);
        customer = new Customer(1L);
        device.setCustomer(customer);
    }

    @Test
    void getAllServicesByDevice() {
        when(serviceRepository.findByDeviceIdOrderById(DEVICE_ID)).thenReturn(Stream.of(service, service).collect(Collectors.toList()));
        assertEquals(((List<Service>)serviceService.getAllServicesByDevice(DEVICE_ID)).size(), 2);
    }

    @Test
    void createService() throws ServiceAlreadyExistsException {
        when(serviceRepository.findByDeviceIdAndType(DEVICE_ID, service.getType())).thenReturn(Optional.empty());
        when(deviceService.getDeviceById(DEVICE_ID)).thenReturn(device);
        when(serviceTypeRepository.findById(service.getType().getId())).thenReturn(Optional.of(service.getType()));
        when(serviceRepository.save(service)).thenReturn(service);
        assertEquals(service, serviceService.createService(DEVICE_ID, service));
    }

    @Test
    void createServiceNoType() throws ServiceAlreadyExistsException {
        Service serviceNoType = new Service(SERVICE_ID, null);
        when(serviceRepository.findByDeviceIdAndType(DEVICE_ID, serviceNoType.getType())).thenReturn(Optional.empty());
        when(deviceService.getDeviceById(DEVICE_ID)).thenReturn(device);
        assertThrows(GenericApiException.class,() -> serviceService.createService(DEVICE_ID, serviceNoType));
    }

    @Test
    void createServiceAlreadyExists() throws ServiceAlreadyExistsException {
        when(serviceRepository.findByDeviceIdAndType(DEVICE_ID, service.getType())).thenReturn(Optional.of(service));
        assertThrows(ServiceAlreadyExistsException.class,() -> serviceService.createService(DEVICE_ID, service));
    }

    @Test
    void deleteService() {
        when(serviceRepository.findById(SERVICE_ID)).thenReturn(Optional.of(service));
        doNothing().when(serviceRepository).deleteById(SERVICE_ID);
        serviceService.deleteService(SERVICE_ID);
        verify(serviceRepository, times(1)).deleteById(SERVICE_ID);
    }

    @Test
    void deleteServiceNotFound() {
        when(serviceRepository.findById(SERVICE_ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> serviceService.deleteService(SERVICE_ID));
    }
}