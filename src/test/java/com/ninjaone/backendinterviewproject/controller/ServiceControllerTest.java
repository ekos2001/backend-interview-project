package com.ninjaone.backendinterviewproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.BackendInterviewProjectApplication;
import com.ninjaone.backendinterviewproject.controller.dto.DeviceDTO;
import com.ninjaone.backendinterviewproject.controller.dto.ServiceDTO;
import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.DeviceType;
import com.ninjaone.backendinterviewproject.model.Service;
import com.ninjaone.backendinterviewproject.model.ServiceType;
import com.ninjaone.backendinterviewproject.security.JwtTokenService;
import com.ninjaone.backendinterviewproject.service.DeviceService;
import com.ninjaone.backendinterviewproject.service.ServiceService;
import com.ninjaone.backendinterviewproject.service.UserDetailsSecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BackendInterviewProjectApplication.class, JwtTokenService.class, UserDetailsSecurityService.class})
@WebMvcTest(ServiceController.class)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
class ServiceControllerTest {
    final static Long DEVICE_ID = 1l;
    final static Long SERVICE_ID = 1l;
    final static String BASE_URL = "/api/v1/customers/devices";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ServiceService serviceService;

    private Service service;
    private ServiceDTO serviceDTO;
    @BeforeEach
    void setUp() {
        service = new Service(SERVICE_ID, new ServiceType(1L, "BACKUP", "Backup", new BigDecimal(1)));
        Device device = new Device(DEVICE_ID, "DeviceName", new DeviceType(1L, "MAC", "Mac", new BigDecimal(1)));
        service.setDevice(device);
        serviceDTO = new ServiceDTO(service);
    }

    @Test
    @WithMockUser(value = "spring")
    void getAllServicesByDevice() throws Exception {
        List<Service> serviceResult = Stream.of(service).collect(Collectors.toList());
        List<ServiceDTO> expectedResult = serviceResult.stream().map(ServiceDTO::new).collect(Collectors.toList());

        when(serviceService.getAllServicesByDevice(DEVICE_ID)).thenReturn(serviceResult);

        mockMvc.perform(get(BASE_URL +"/" + DEVICE_ID + "/services"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(expectedResult)));
    }

    @Test
    @WithMockUser(value = "spring")
    void createService() throws Exception {
        when(serviceService.createService(eq(DEVICE_ID), any())).thenReturn(service);

        String sampleEntityString = objectMapper.writeValueAsString(serviceDTO);
        mockMvc.perform(post(BASE_URL +"/" + DEVICE_ID + "/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sampleEntityString))
                .andExpect(status().isCreated())
                .andExpect(content().string(sampleEntityString));
    }

    @Test
    @WithMockUser(value = "spring")
    void deleteService() throws Exception {
        doNothing().when(serviceService).deleteService(SERVICE_ID);

        mockMvc.perform(delete(BASE_URL + "/services/" + SERVICE_ID))
                .andExpect(status().isNoContent());
    }
}