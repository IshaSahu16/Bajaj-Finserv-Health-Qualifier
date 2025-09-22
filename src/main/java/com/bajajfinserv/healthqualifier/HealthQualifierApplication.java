package com.bajajfinserv.healthqualifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bajajfinserv.healthqualifier.service.QualifierService;

@SpringBootApplication
public class HealthQualifierApplication implements CommandLineRunner {

    @Autowired
    private QualifierService qualifierService;

    public static void main(String[] args) {
        SpringApplication.run(HealthQualifierApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // This method runs after the Spring Boot application has started
        // No controller/endpoint should trigger the flow - as per requirements
        System.out.println("Starting Bajaj Finserv Health Qualifier Assessment...");
        qualifierService.executeQualifierFlow();
    }
}

