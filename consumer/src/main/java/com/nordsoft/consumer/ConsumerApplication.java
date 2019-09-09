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

    public static final String COM_NORDSOFT_STREAMS = "com.nordsoft.streams.";
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
        Set<String> keys = template.keys(COM_NORDSOFT_STREAMS + "*");
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
        template.opsForHash().put(COM_NORDSOFT_STREAMS + instanceId, "producer", msg);
        template.opsForHash().put(COM_NORDSOFT_STREAMS + instanceId, "timestamp", LocalDateTime.now().toString());
        template.expire(COM_NORDSOFT_STREAMS + instanceId, 30, TimeUnit.MINUTES);
        log.info("consumer:{},message:{}", instanceId, msg);
    }
}
