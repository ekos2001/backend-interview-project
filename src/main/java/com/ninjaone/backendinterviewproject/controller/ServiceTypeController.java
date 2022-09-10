package com.ninjaone.backendinterviewproject.controller;

import com.ninjaone.backendinterviewproject.model.ServiceType;
import com.ninjaone.backendinterviewproject.service.ServiceTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/settings/service-types")
public class ServiceTypeController {
    private final ServiceTypeService serviceTypeService;

    public ServiceTypeController(ServiceTypeService serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
    }

    @GetMapping
    public Iterable<ServiceType> getAllServiceTypes() {
        return serviceTypeService.getAllServiceTypes();
    }
}
