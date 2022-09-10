package com.ninjaone.backendinterviewproject.controller;

import com.ninjaone.backendinterviewproject.controller.dto.ServiceDTO;
import com.ninjaone.backendinterviewproject.exception.ServiceAlreadyExistsException;
import com.ninjaone.backendinterviewproject.model.Service;
import com.ninjaone.backendinterviewproject.model.ServiceType;
import com.ninjaone.backendinterviewproject.service.ServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/customers/devices")
public class ServiceController {
    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping("/{deviceId}/services")
    public List<ServiceDTO> getAllServicesByDevice(@PathVariable Long deviceId) {
        var services = serviceService.getAllServicesByDevice(deviceId);
        return services.stream().map(ServiceDTO::new).collect(Collectors.toList());
    }

    @PostMapping("/{deviceId}/services")
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceDTO createService(@PathVariable Long deviceId, @RequestBody @Valid ServiceDTO serviceDTO) throws ServiceAlreadyExistsException {
        ServiceType serviceType = new ServiceType(serviceDTO.getTypeId());
        Service service = new Service(serviceType);
        return new ServiceDTO(serviceService.createService(deviceId, service));
    }

    @DeleteMapping("/services/{serviceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteService(@PathVariable Long serviceId) {
        serviceService.deleteDevice(serviceId);
    }
}
