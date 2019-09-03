package com.nordsoft.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@EnableDiscoveryClient
@SpringBootApplication
public class ProducerApplication {


    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

    @GetMapping(value = "/producer/hello/{code}")
    public String updateHallProperty(@PathVariable("code") String code) {
        return "HELLO!";
    }
}

