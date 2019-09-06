package com.nordsoft.consumer;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
@RestController
@EnableBinding(Sink.class)
public class ConsumerApplication {

    @Autowired
    private StringRedisTemplate template;
    @Autowired
    private Registration registration;


    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @GetMapping(value = "/consumer/hello")
    public Map get() {
        Map<String, String> dataMap = Maps.newHashMap();
        Set<String> keys = template.keys("com.nordsoft.streams.*");
        for (String key : keys) {
            dataMap.put(key, template.opsForValue().get(key));
        }
        return dataMap;
    }

    @StreamListener(Sink.INPUT)
    public void receive(Message<String> message) {
        String instanceId = registration.getInstanceId();
        String msg = message.getPayload();
        template.opsForValue().set("com.nordsoft.streams." + instanceId, msg, 10, TimeUnit.SECONDS);
        log.info("consumer:{},message:{}", instanceId, msg);
    }
}
