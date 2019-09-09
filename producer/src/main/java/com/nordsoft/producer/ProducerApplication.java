package com.nordsoft.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
@RestController
@EnableBinding(Source.class)
public class ProducerApplication {

    @Autowired
    private Registration registration;

    @Autowired
    private Source source;


    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

    @GetMapping(value = "/producer/hello")
    public Boolean get() {
        String instanceId = registration.getInstanceId();
        log.info("producer:{},message:{}", instanceId, instanceId);
        return source.output().send(MessageBuilder.withPayload(instanceId).build());
    }

}

