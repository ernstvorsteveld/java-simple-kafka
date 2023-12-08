package com.sternitc.simplekafka.producer;

import com.sternitc.simplekafka.KafkaConfiguration;
import com.sternitc.simplekafka.SimpleKafkaApplication;
import com.sternitc.simplekafka.TopicProvider;
import com.sternitc.simplekafka.domain.User;
import com.sternitc.simplekafka.listener.ListenerConfiguration;
import com.sternitc.simplekafka.listener.UserHandler;
import com.sternitc.simplekafka.listener.UserListener;
import org.apache.kafka.clients.admin.AdminClient;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = SimpleKafkaApplication.class)
@Import({KafkaConfiguration.class, ProducerConfiguration.class,
        ListenerConfiguration.class, UserProducerKafkaTest.UserProducerKafkaTestConfiguration.class})
@ActiveProfiles({"test"})
class UserProducerKafkaTest {

    @Autowired
    private UserProducer userProducer;

    @Autowired
    private UserListener userListener;

    @Autowired
    private UserHandler userHandler;

    @Autowired
    private AdminClient adminClient;

    @Autowired
    private TopicProvider topicProvider;

    @Test
    public void should_produce_user() {
        userProducer.send(User.builder().name("John Doe1").role("General1").build());
        userProducer.send(User.builder().name("John Doe2").role("General2").build());

        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    TestHandler handler = (TestHandler) userHandler;
                    assertThat(handler.getMessages().size()).isEqualTo(2);
                });
    }

    @AfterEach
    public void cleanUp() {
        Collection<String> topics = Collections.singletonList(topicProvider.get("users"));
        try {
            adminClient.deleteTopics(topics).all().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @TestConfiguration
    public static class UserProducerKafkaTestConfiguration {

        @Bean
        public UserHandler userHandler() {
            return new TestHandler();
        }

        @Bean
        public TopicProvider topicProvider() {
            return new TestTopicProvider();
        }

    }

    public static class TestHandler implements UserHandler {

        Map<String, Tuple> messages = new HashMap<>();

        @Override
        public void handle(String key, String correlationId, String user) {
            messages.put(key, Tuple.tuple(correlationId, user));
        }

        public Map<String, Tuple> getMessages() {
            return messages;
        }

    }

    public static class TestTopicProvider implements TopicProvider {

        private final String userTopic = UUID.randomUUID().toString();

        @Override
        public String get(String base) {
            return userTopic;
        }
    }

}