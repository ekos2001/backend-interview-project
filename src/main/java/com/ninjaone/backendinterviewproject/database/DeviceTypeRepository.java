package com.ninjaone.backendinterviewproject.database;

import com.ninjaone.backendinterviewproject.model.DeviceType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeviceTypeRepository extends CrudRepository<DeviceType, Long> {
}
