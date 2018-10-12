package io.examples.mqtt;

import io.vertx.core.Future;
import io.vertx.mqtt.MqttServerOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.mqtt.MqttEndpoint;
import io.vertx.reactivex.mqtt.MqttServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Gary Cheng
 */
public class ServerVerticle extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(ServerVerticle.class);
    MqttServer mqttServer;

    // Convenience method so you can run it in IDE
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.rxDeployVerticle(ServerVerticle.class.getName())
                .subscribe(id -> logger.debug("ServerVerticle deployed successfully with deployment ID {}", id),
                        ex -> {
                            logger.error(ex.getLocalizedMessage());
                            vertx.close();
                        });
    }

    @Override
    public void start(Future<Void> startFuture) {
        MqttServerOptions options = new MqttServerOptions()
                .setPort(1883)
                .setHost("127.0.0.1");
        mqttServer = MqttServer.create(vertx, options);
        mqttServer.endpointHandler(this::handler)
                .rxListen()
                .subscribe(s -> startFuture.complete(), startFuture::fail);
    }

    @Override
    public void stop(Future<Void> startFuture) {
        if (null != mqttServer) {
            mqttServer.rxClose().subscribe(() -> startFuture.complete());
        } else {
            startFuture.complete();
        }
    }

    private void handler(MqttEndpoint endpoint) {
        logger.debug("connected client " + endpoint.clientIdentifier());
        endpoint.publishHandler(message -> logger.debug("Just received message on [{}] payload [{}] with QoS[{}]" + message.topicName(), message.payload(), message.qosLevel()));
        endpoint.accept(false);
    }
}
