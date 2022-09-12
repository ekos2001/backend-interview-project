package com.ninjaone.backendinterviewproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.BackendInterviewProjectApplication;
import com.ninjaone.backendinterviewproject.model.ServiceType;
import com.ninjaone.backendinterviewproject.security.JwtTokenService;
import com.ninjaone.backendinterviewproject.service.ServiceTypeService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BackendInterviewProjectApplication.class, JwtTokenService.class, UserDetailsSecurityService.class})
@WebMvcTest(ServiceTypeController.class)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
class ServiceTypeControllerTest {
    final static String BASE_URL = "/api/v1/settings/service-types";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ServiceTypeService serviceTypeService;

    private ServiceType serviceType;
    @BeforeEach
    void setUp() {
        serviceType = new ServiceType(1L, "BACKUP", "Backup", new BigDecimal(1));
    }

    @Test
    @WithMockUser(value = "spring")
    void getAllServiceTypes() throws Exception {
        List<ServiceType> result = Stream.of(serviceType).collect(Collectors.toList());
        when(serviceTypeService.getAllServiceTypes()).thenReturn(result);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(result)));
    }
}