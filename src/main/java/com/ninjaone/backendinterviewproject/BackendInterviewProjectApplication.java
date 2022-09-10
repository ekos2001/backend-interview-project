package com.ninjaone.backendinterviewproject;

import com.ninjaone.backendinterviewproject.database.CustomerRepository;
import com.ninjaone.backendinterviewproject.database.DeviceRepository;
import com.ninjaone.backendinterviewproject.database.ServiceRepository;
import com.ninjaone.backendinterviewproject.model.*;
import com.ninjaone.backendinterviewproject.service.CustomerService;
import com.ninjaone.backendinterviewproject.service.DeviceService;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
@Log4j2
public class BackendInterviewProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendInterviewProjectApplication.class, args);
	}

    //@Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository, DeviceRepository deviceRepository, ServiceRepository serviceRepository) {
        return args -> {
            int customers = 1000;
            int devices = 1000;
            List<Customer> cutomersToCreate = new ArrayList<>();
            Long customerId = 100L;
            Long deviceId = 100L;
            Long serviceId = 100L;
            for (int i = 0; i < customers; i++) {
                Customer customer = new Customer(/*customerId++, */null,"customer " +i);

                Random random = new Random();

                for (int j = 0; j < devices; j++) {
                    DeviceType deviceType = new DeviceType(random.longs(1L, 3L).findFirst().getAsLong());
                    Device device = new Device(/*deviceId++,*/"Device " + j, deviceType);
                    device.setCustomer(customer);
                    customer.getDevices().add(device);

                    for (int k = 1; k <= 5; k++) {
                        ServiceType serviceType = new ServiceType((long) k);
                        Service service = new Service(/*serviceId++,*/ serviceType);
                        service.setDevice(device);
                        device.getServices().add(service);
                    }
                }


                cutomersToCreate.add(customer);

            }
            log.info("Saving test customers...");
            customerRepository.saveAll(cutomersToCreate);
            log.info("Saving test customers...Done");
        };
    }
}
