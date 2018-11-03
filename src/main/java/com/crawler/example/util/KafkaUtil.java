package com.crawler.example.util;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Properties;

/**
 * create by zhouzhenyang on 2018/10/9
 */
@Component
public class KafkaUtil {

    @Autowired
    Properties kafkaProducerProps;

    private final Logger logger = LoggerFactory.getLogger(KafkaUtil.class);

    public KafkaProducer<String, String> getProducer() {
        return new KafkaProducer<>(kafkaProducerProps);
    }

    public void sendMessage(String topic, String message) {
        KafkaProducer<String, String> producer = getProducer();
        //注：发送失败时metadata为null
        producer.send(new ProducerRecord<>(topic, message), new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {

                if (exception != null) {
                    logger.error("kafka send message error:", exception);
                }
            }
        });
        producer.close();
    }
}
