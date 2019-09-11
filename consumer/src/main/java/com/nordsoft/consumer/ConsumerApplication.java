package com.nordsoft.consumer;

import cn.hutool.core.util.IdUtil;
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
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
@RestController
@EnableBinding(Sink.class)
public class ConsumerApplication {

    public static final String COM_NORDSOFT_STREAMS_LOG = "com.nordsoft.streams.log.";
    public static final String COM_NORDSOFT_STREAMS_INCR = "com.nordsoft.streams.incr";
    @Autowired
    private StringRedisTemplate template;
    @Autowired
    private Registration registration;

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @GetMapping(value = "/consumer/hello")
    public Map get() {
        Map<String, Map<Object, Object>> dataMap = Maps.newTreeMap();
        Set<String> keys = template.keys(COM_NORDSOFT_STREAMS_LOG + "*");
        for (String key : keys) {
            Map<Object, Object> hash = template.opsForHash().entries(key);
            dataMap.put(key, hash);
        }
        return dataMap;
    }

    @StreamListener(Sink.INPUT)
    public void receive(Message<?> message) {
        String instanceId = registration.getInstanceId();
        String msg = message.getPayload().toString();
        String key = COM_NORDSOFT_STREAMS_LOG + IdUtil.getSnowflake(Long.valueOf(registration.getHost().replaceAll("\\.", "")) % 32, registration.getPort() % 32).nextId();
        template.opsForHash().put(key, "consumer", instanceId);
        template.opsForHash().put(key, "timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        template.opsForHash().put(key, "msg", msg);
        template.expire(key, 10, TimeUnit.MINUTES);
        log.info("consumer:{},message:{}", instanceId, msg);
    }
}
