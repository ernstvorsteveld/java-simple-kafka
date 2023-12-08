package com.sternitc.simplekafka.listener;

import com.sternitc.simplekafka.TopicProvider;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;

@AllArgsConstructor
public class UserListenerKafka implements UserListener {

    private UserHandler userHandler;

    private TopicProvider topicProvider;

    @KafkaListener(topics = "#{topicProvider.get(\"users\")}", topicPartitions = {
            @TopicPartition(topic = "#{topicProvider.get(\"users\")}",
                    partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0"))
    })
    @Override
    public void consume(ConsumerRecord<String, String> userConsumerRecord) {
        userHandler.handle(
                userConsumerRecord.headers().headers("correlationId").toString(),
                userConsumerRecord.key(),
                userConsumerRecord.value());
    }

}
