package com.ninjaone.backendinterviewproject.service;

import com.ninjaone.backendinterviewproject.database.ServiceTypeRepository;
import com.ninjaone.backendinterviewproject.model.ServiceType;
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
class ServiceTypeServiceTest {
    @InjectMocks
    private ServiceTypeService serviceTypeService;
    @Mock
    private ServiceTypeRepository serviceTypeRepository;
    private ServiceType serviceTypeEntity;

    @BeforeEach
    void setUp() {
        serviceTypeEntity = new ServiceType(1L, "BACKUP", "Backup", new BigDecimal(1));
    }

    @Test
    void getAllServiceTypes() {
        when(serviceTypeRepository.findAll()).thenReturn(Stream.of(serviceTypeEntity).collect(Collectors.toList()));
        assertEquals(((List<ServiceType>)serviceTypeService.getAllServiceTypes()).size(), 1);
    }
}