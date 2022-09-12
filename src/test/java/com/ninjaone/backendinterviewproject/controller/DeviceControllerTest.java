package com.ninjaone.backendinterviewproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.BackendInterviewProjectApplication;
import com.ninjaone.backendinterviewproject.controller.dto.DeviceDTO;
import com.ninjaone.backendinterviewproject.model.Customer;
import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.DeviceType;
import com.ninjaone.backendinterviewproject.security.JwtTokenService;
import com.ninjaone.backendinterviewproject.service.DeviceService;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BackendInterviewProjectApplication.class, JwtTokenService.class, UserDetailsSecurityService.class})
@WebMvcTest(DeviceController.class)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
public class DeviceControllerTest {
    final static Long CUSTOMER_ID = 1L;
    final static Long DEVICE_ID = 1L;
    final static String NAME = "DeviceName";
    final static String BASE_URL = "/api/v1/customers";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private DeviceService deviceService;

    private Device device;
    private DeviceDTO deviceDTO;

    @BeforeEach
    void setup(){
        device = new Device(DEVICE_ID, NAME, new DeviceType(1L));
        Customer customer = new Customer(1L, "Test Customer");
        device.setCustomer(customer);
        deviceDTO = new DeviceDTO(device);
    }

    @Test
    @WithMockUser(value = "spring")
    void getAllDevicesByCustomer() throws Exception {
        List<Device> serviceResult = Stream.of(device).collect(Collectors.toList());
        List<DeviceDTO> expectedResult = serviceResult.stream().map(DeviceDTO::new).collect(Collectors.toList());

        when(deviceService.getAllDevicesByCustomer(CUSTOMER_ID)).thenReturn(serviceResult);


        mockMvc.perform(get(BASE_URL +"/" + CUSTOMER_ID + "/devices"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(expectedResult)));
    }

    @Test
    @WithMockUser(value = "spring")
    void getDeviceById() throws Exception {
        when(deviceService.getDeviceById(DEVICE_ID)).thenReturn(device);

        mockMvc.perform(get(BASE_URL + "/devices/" + DEVICE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(deviceDTO)));
    }

    @Test
    @WithMockUser(value = "spring")
    void createDevice() throws Exception {
        when(deviceService.createDevice(eq(CUSTOMER_ID), any())).thenReturn(device);

        String sampleEntityString = objectMapper.writeValueAsString(deviceDTO);
        mockMvc.perform(post(BASE_URL +"/" + CUSTOMER_ID + "/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sampleEntityString))
                .andExpect(status().isCreated())
                .andExpect(content().string(sampleEntityString));
    }

    @Test
    @WithMockUser(value = "spring")
    void updateDevice() throws Exception {
        when(deviceService.updateDevice(eq(DEVICE_ID), any())).thenReturn(device);

        String sampleEntityString = objectMapper.writeValueAsString(deviceDTO);
        mockMvc.perform(put(BASE_URL + "/devices/" + DEVICE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sampleEntityString))
                .andExpect(status().isOk())
                .andExpect(content().string(sampleEntityString));
    }

    @Test
    @WithMockUser(value = "spring")
    void deleteDevice() throws Exception {
        doNothing().when(deviceService).deleteDevice(DEVICE_ID);

        mockMvc.perform(delete(BASE_URL + "/devices/" + DEVICE_ID))
                .andExpect(status().isNoContent());
    }
}
