package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.DeviceTypeRepository;
import com.ninjaone.backendinterviewproject.model.DeviceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceTypeServiceTest {
    @InjectMocks
    private DeviceTypeService deviceTypeService;
    @Mock
    private DeviceTypeRepository deviceTypeRepository;
    private DeviceType deviceTypeEntity;

    @BeforeEach
    void setUp() {
        deviceTypeEntity = new DeviceType(1L, "MAC", "Mac", new BigDecimal(1));
    }

    @Test
    void getAllDeviceTypes() {
        when(deviceTypeRepository.findAll()).thenReturn(Stream.of(deviceTypeEntity, deviceTypeEntity, deviceTypeEntity).collect(Collectors.toList()));
        assertEquals(((List<DeviceType>)deviceTypeService.getAllDeviceTypes()).size(), 3);
    }
}