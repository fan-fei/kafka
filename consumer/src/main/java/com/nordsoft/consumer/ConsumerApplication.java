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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        Map<String, Map<Object, Object>> dataMap = Maps.newHashMap();
        Set<String> keys = template.keys("com.nordsoft.streams.*");
        for (String key : keys) {
            Map<Object, Object> hash = template.opsForHash().entries(key);
            dataMap.put(key, hash);
        }
        return dataMap;
    }

    @StreamListener(Sink.INPUT)
    public void receive(Object message) {
        String instanceId = registration.getInstanceId();
        String msg = message.toString();
        template.opsForHash().put("com.nordsoft.streams." + instanceId, "producer", msg);
        template.opsForHash().put("com.nordsoft.streams." + instanceId, "timestamp", LocalDateTime.now().toString());
        template.expire("com.nordsoft.streams." + instanceId, 30, TimeUnit.MINUTES);
        log.info("consumer:{},message:{}", instanceId, msg);
    }
}
