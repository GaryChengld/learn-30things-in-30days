package io.examples.mqtt;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Future;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.mqtt.MqttClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Gary Cheng
 */
public class PublisherVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(PublisherVerticle.class);

    private static final String MQTT_TOPIC = "/mqtt_topic";
    private static final String MQTT_MESSAGE = "Hello MQTT";
    private static final String BROKER_HOST = "localhost";
    private static final int BROKER_PORT = 1883;

    MqttClient mqttClient;

    // Convenience method so you can run it in IDE
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.rxDeployVerticle(PublisherVerticle.class.getName())
                .subscribe(id -> logger.debug("PublisherVerticle deployed successfully with deployment ID {}", id),
                        ex -> {
                            logger.error(ex.getLocalizedMessage());
                            vertx.close();
                        });
    }

    @Override
    public void start(Future<Void> startFuture) {
        mqttClient = MqttClient.create(vertx);
        mqttClient.rxConnect(BROKER_PORT, BROKER_HOST)
                .subscribe(ack -> this.publishMessage(), startFuture::fail);
    }

    @Override
    public void stop(Future<Void> startFuture) {
        mqttClient.rxDisconnect().subscribe(() -> startFuture.complete(), startFuture::fail);
    }


    private void publishMessage() {
        logger.debug("publish Messages");
        vertx.setPeriodic(10_000, i -> mqttClient.rxPublish(MQTT_TOPIC, Buffer.buffer(MQTT_MESSAGE), MqttQoS.AT_MOST_ONCE, false, false)
                .subscribe(i2 -> logger.debug("Message published"), t -> logger.error(t.getLocalizedMessage())));
    }
}
