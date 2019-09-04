package com.nordsoft.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableDiscoveryClient
@SpringBootApplication
@RestController
public class ProducerApplication {

    @Autowired
    private Registration registration;

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

    @RequestMapping(value = "/producer/hello")
    public String get() {
        return registration.getInstanceId();
    }

}

