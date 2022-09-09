package com.ninjaone.backendinterviewproject;

import com.ninjaone.backendinterviewproject.database.DeviceRepository;
import com.ninjaone.backendinterviewproject.model.Customer;
import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.DeviceType;
import com.ninjaone.backendinterviewproject.service.DeviceService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendInterviewProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendInterviewProjectApplication.class, args);
	}

    @Bean
    CommandLineRunner commandLineRunner(DeviceService deviceService, DeviceRepository deviceRepository) {
        return args -> {
            DeviceType deviceType = new DeviceType(1L);
            Device device = new Device("Device 1", deviceType);
            Customer customer = new Customer(1L);
            device.setCustomer(customer);
            deviceRepository.save(device);
        };
    }
}
