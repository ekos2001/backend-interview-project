package com.ninjaone.backendinterviewproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.BackendInterviewProjectApplication;
import com.ninjaone.backendinterviewproject.controller.dto.CostDTO;
import com.ninjaone.backendinterviewproject.controller.dto.CustomerDTO;
import com.ninjaone.backendinterviewproject.controller.dto.DeviceDTO;
import com.ninjaone.backendinterviewproject.exception.CustomerAlreadyExistsException;
import com.ninjaone.backendinterviewproject.model.Customer;
import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.security.JwtTokenService;
import com.ninjaone.backendinterviewproject.service.CustomerService;
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
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BackendInterviewProjectApplication.class, JwtTokenService.class, UserDetailsSecurityService.class})
@WebMvcTest(CustomerController.class)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
class CustomerControllerTest {
    final static String BASE_URL = "/api/v1/customers";
    final static Long CUSTOMER_ID = 1L;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private CustomerService customerService;
    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customer = new Customer(CUSTOMER_ID, "Test Customer");
        customerDTO = new CustomerDTO(customer);
    }

    @Test
    @WithMockUser(value = "spring")
    void createCustomer() throws Exception {
        when(customerService.addCustomer(any())).thenReturn(customer);

        String sampleEntityString = objectMapper.writeValueAsString(customerDTO);
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sampleEntityString))
                .andExpect(status().isCreated())
                .andExpect(content().string(sampleEntityString));
    }

    @Test
    @WithMockUser(value = "spring")
    void getAllCustomers() throws Exception {
        List<Customer> serviceResult = Stream.of(customer).collect(Collectors.toList());
        List<CustomerDTO> expectedResult = serviceResult.stream().map(CustomerDTO::new).collect(Collectors.toList());

        when(customerService.getAllCustomers()).thenReturn(serviceResult);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(expectedResult)));
    }

    @Test
    @WithMockUser(value = "spring")
    void getMonthlyCost() throws Exception {
        when(customerService.getMonthlyCost(any())).thenReturn(new BigDecimal(1));
        String sampleEntityString = objectMapper.writeValueAsString(new CostDTO(new BigDecimal(1)));
        mockMvc.perform(get(BASE_URL + "/" + CUSTOMER_ID + "/cost"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(sampleEntityString));
    }

    @Test
    @WithMockUser(value = "spring")
    void getMonthlyCost2() throws Exception {
        when(customerService.getMonthlyCost2(any())).thenReturn(new BigDecimal(1));
        String sampleEntityString = objectMapper.writeValueAsString(new CostDTO(new BigDecimal(1)));
        mockMvc.perform(get(BASE_URL + "/" + CUSTOMER_ID + "/cost2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(sampleEntityString));
    }
}