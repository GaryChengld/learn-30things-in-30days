package io.examples.kafka;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * @author Gary Cheng
 */
@Component
public class AppRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("kafka:{{kafka.topic}}?brokers={{kafka.server}}:{{kafka.port}}&groupId={{kafka.consumerGroup}}")
                .bean(MessageConsumer.class);

        from("timer://producer?period=5000")
                .bean(MessageProducer.class)
                .to("kafka:{{kafka.topic}}?brokers={{kafka.server}}:{{kafka.port}}");
    }
}
