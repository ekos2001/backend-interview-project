package com.ninjaone.backendinterviewproject.database;

import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.DeviceType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends CrudRepository<Device, Long> {
    List<Device> findByCustomerIdOrderById(Long customerId);

    Optional<Device> findByCustomerIdAndNameAndDeviceType(Long customerId, String name, DeviceType deviceType);
}
