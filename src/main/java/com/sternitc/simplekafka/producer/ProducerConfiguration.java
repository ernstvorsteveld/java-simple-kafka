package com.sternitc.simplekafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sternitc.simplekafka.TopicProvider;
import com.sternitc.simplekafka.TopicProviderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class ProducerConfiguration {

    @Bean
    public UserProducer userProducerKafka(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper,
            TopicProvider topicProvider) {
        return new UserProducerKafka(kafkaTemplate, objectMapper, topicProvider);
    }

}
