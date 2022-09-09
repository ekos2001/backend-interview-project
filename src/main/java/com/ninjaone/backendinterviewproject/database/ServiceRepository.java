package com.ninjaone.backendinterviewproject.database;

import com.ninjaone.backendinterviewproject.model.Service;
import com.ninjaone.backendinterviewproject.model.ServiceType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends CrudRepository<Service, Long> {
    List<Service> findByDeviceIdOrderById(Long deviceId);

    Optional<Service> findByDeviceIdAndType(Long deviceId, ServiceType type);
}
