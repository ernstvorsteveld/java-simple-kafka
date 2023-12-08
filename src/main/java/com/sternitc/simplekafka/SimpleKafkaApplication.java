package com.sternitc.simplekafka;

import com.sternitc.simplekafka.listener.ListenerConfiguration;
import com.sternitc.simplekafka.producer.ProducerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({KafkaConfiguration.class, ListenerConfiguration.class, ProducerConfiguration.class})
public class SimpleKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleKafkaApplication.class, args);
	}

}
