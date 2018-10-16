package io.examples.kafka;

import java.util.UUID;

/**
 * @author Gary Cheng
 */
public class MessageProducer {
    public String produceMessage() {
        return "Hello from Apache Camel " + UUID.randomUUID().toString();
    }
}
