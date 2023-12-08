package com.sternitc.simplekafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sternitc.simplekafka.TopicProvider;
import com.sternitc.simplekafka.domain.User;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class UserProducerKafka implements UserProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    private final TopicProvider topicProvider;

    @Override
    public void send(User user) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topicProvider.get("users"), user.getId(), map(user));
        record.headers().add("correlationId", UUID.randomUUID().toString().getBytes());
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(record);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + user +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" +
                        user + "] due to : " + ex.getMessage());
            }
        });

    }

    private String map(User user) {
        try {
            return objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
