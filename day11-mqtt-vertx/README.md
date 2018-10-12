# Day 11:  MQTT - a machine-to-machine connectivity protocol

## What is MQTT?

MQTT stands for MQ Telemetry Transport but previously was known as Message Queuing Telemetry Transport.

MQTT is a lightweight publish/subscribe messaging protocol designed for M2M (machine to machine) telemetry in low bandwidth environments.

It was designed by Andy Stanford-Clark (IBM) and Arlen Nipper in 1999 for connecting Oil Pipeline telemetry systems over satellite. Although it started as a proprietary protocol it was released Royalty free in 2010 and became an OASIS standard in 2014.

MQTT is fast becoming one of the main protocols for IOT (internet of things) deployments.

## How MQTT Works

 - MQTT is a messaging protocol i.e it was designed for transferring messages, and uses a publish and subscribe model.
 - This model makes it possible to send messages to 0,1 or multiple clients.
 - There is no direct connection between the broadcaster and the viewer.
 - In MQTT a publisher publishes messages on a topic and a subscriber must subscribe to that topic to view the message.
 - MQTT requires the use of a central Broker.
 
 <img width="560" src="http://www.steves-internet-guide.com/wp-content/uploads/MQTT-Publish-Subscribe-Model.jpg" />
 
 ## Difference between MQTT and AMQP
 
 **MQTT** is a lightweight protocol working only with a broker in the middle with no concept of queue. It supports only pub/sub and have no metadata in the messages.
 
 **AMQP** is more oriented to messaging than MQTT, including reliable queuing, topic-based publish-and-subscribe messaging, flexible routing, transactions, and security. AMQP exchanges route messages directly.
 
 ## Important Points to Note
 
 1. Clients do not have addresses like in email systems, and messages are not sent to clients.
 2. Messages are published to a broker on a topic.
 3. The job of an MQTT broker is to filter messages based on topic, and then distribute them to subscribers.
 4. A client can receive these messages by subscribing to that topic on the same broker.
 5. There is no direct connection between a publisher and subscriber.
 6. All clients can publish (broadcast) and subscribe (receive).
 7. MQTT brokers do not normally store messages.
 8. MQTT uses TCP/IP to connect to the broker.
 
 ## Example on vert.x
 
 Now I'm going to create a MQTT sample on vert.x
 
 Vert.x provides MQTT server component and client component
 
 in pom.xml, include mqtt dependency
 
 ```xml
 <dependency>
     <groupId>io.vertx</groupId>
     <artifactId>vertx-mqtt</artifactId>
     <version>3.5.4</version>
 </dependency>
 ```
 
 **Create MQTT server**
 
 ```java
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
```

 **Create MQTT publisher**
 
 ```java
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
 ```
 
 The publisher publishes message to broker every 10 second
 
 **Build**
 
 ```
 mvn clean install
 ```
 
 **Start Server**
 
 Open a command prompt
 
 ```
 cd server
java -jar target\mqtt-server-0.0.1-SNAPSHOT-fat.jar
```

it shows
```
Oct 12, 2018 12:38:06 AM io.vertx.core.impl.launcher.commands.VertxIsolatedDeployer
INFO: Succeeded in deploying verticle
```

**Start publisher**

Open another command prompt

```
cd publisher
java -jar target\mqtt-publisher-0.0.1-SNAPSHOT-fat.jar
```

The publisher client connect to broker and start send messages

```
Oct 12, 2018 12:39:52 AM io.vertx.mqtt.impl.MqttClientImpl
INFO: Connection with localhost:1883 established successfully
00:39:52.092 [vert.x-eventloop-thread-0] DEBUG io.examples.mqtt.PublisherVerticle - publish Messages
00:40:02.109 [vert.x-eventloop-thread-0] DEBUG io.examples.mqtt.PublisherVerticle - Message published
00:40:12.097 [vert.x-eventloop-thread-0] DEBUG io.examples.mqtt.PublisherVerticle - Message published
```

And in server command prompt shows
```
00:39:52.081 [vert.x-eventloop-thread-0] DEBUG io.examples.mqtt.ServerVerticle - connected client b0850354-d6c4-41af-90cc-fbf553ab0aed
00:40:02.128 [vert.x-eventloop-thread-0] DEBUG io.examples.mqtt.ServerVerticle - Just received message on [Hello MQTT] payload [AT_MOST_ONCE] with QoS[{}]/mqtt_topic
00:40:12.097 [vert.x-eventloop-thread-0] DEBUG io.examples.mqtt.ServerVerticle - Just received message on [Hello MQTT] payload [AT_MOST_ONCE] with QoS[{}]/mqtt_topic
00:40:22.098 [vert.x-eventloop-thread-0] DEBUG io.examples.mqtt.ServerVerticle - Just received message on [Hello MQTT] payload [AT_MOST_ONCE] with QoS[{}]/mqtt_topic
00:40:32.097 [vert.x-eventloop-thread-0] DEBUG io.examples.mqtt.ServerVerticle - Just received message on [Hello MQTT] payload [AT_MOST_ONCE] with QoS[{}]/mqtt_topic
00:40:42.098 [vert.x-eventloop-thread-0] DEBUG io.examples.mqtt.ServerVerticle - Just received message on [Hello MQTT] payload [AT_MOST_ONCE] with QoS[{}]/mqtt_topic
```

That's all for today, you can find the complete source code under [this folder](.).