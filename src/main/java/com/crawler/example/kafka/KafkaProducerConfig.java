package com.crawler.example.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * create by zhouzhenyang on 2018/10/9
 */
@Configuration
public class KafkaProducerConfig {

    @Autowired
    Environment env;

    @Bean
    public Properties kafkaProducerProps() {
        Properties props = new Properties();
        Map<String, Object> envMap = getAllEnvProps();
        for (String key : envMap.keySet()) {
            if (key.startsWith("producer")) {
                Object value = envMap.get(key);
                props.put(key.replaceFirst("producer.", ""), value);
            }
        }
        return props;
    }

    private Map<String, Object> getAllEnvProps() {
        Map<String, Object> map = new HashMap<>();
        for (PropertySource<?> propertySource : ((AbstractEnvironment) env).getPropertySources()) {
            String fileName = propertySource.getName();
            if (propertySource instanceof MapPropertySource
                    && fileName.contains("kafka")) {
                map.putAll(((MapPropertySource) propertySource).getSource());
            }
        }
        return map;
    }


}
