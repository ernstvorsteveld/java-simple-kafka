package com.sternitc.simplekafka.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface UserListener {

    void consume(ConsumerRecord<String, String> userConsumerRecord);
}
