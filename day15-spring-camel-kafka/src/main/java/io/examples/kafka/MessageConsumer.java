package io.examples.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Gary Cheng
 */
public class MessageConsumer {
    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    public void printKafkaBody(String body) {
        logger.info(body);
    }
}
