# Day 15: Integrate Spring-boot with Apache camel and Kafka

Yesterday I learned Apache Kafka architecture and installed Kafka on windows, today I will try create a sample application on Spring-boot, Apache Camel and Kafka.

First of all, let's learn some Kafka commands.

### Start Servers

**Start Zookeeper**

Open a command prompt, go to Zookeeper bin directory, execute command
>zkServer.cmd

**Start Kafka**

Open another command prompt, go to kafka directory, execute command
>.\bin\windows\kafka-server-start.bat .\config\server.properties

**Create Kafka Topic**

Open the third command prompt, go to kafka directory, execute command
>.\bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic my-topic

It shows

```
Created topic "my-topic".
```

And in Kafka command prompt, displays 

```
[2018-10-15 21:22:59,639] INFO [Log partition=my-topic-0, dir=C:\Development\kafka_2.11-2.0.0\logs] Completed load of log with 1 segments, log start offset 0 and log end offset 0 in 157 ms (kafka.log.Log)
[2018-10-15 21:22:59,641] INFO Created log for partition my-topic-0 in C:\Development\kafka_2.11-2.0.0\logs with properties {compression.type -> producer, message.format.version -> 2.0-IV1, file.delete.delay.ms -> 60000, max.message.bytes -> 1000012, min.compaction.lag.ms -> 0, message.timestamp.type -> CreateTime, message.downconversion.enable -> true, min.insync.replicas -> 1, segment.jitter.ms -> 0, preallocate -> false, min.cleanable.dirty.ratio -> 0.5, index.interval.bytes -> 4096, unclean.leader.election.enable -> false, retention.bytes -> -1, delete.retention.ms -> 86400000, cleanup.policy -> [delete], flush.ms -> 9223372036854775807, segment.ms -> 604800000, segment.bytes -> 1073741824, retention.ms -> 604800000, message.timestamp.difference.max.ms -> 9223372036854775807, segment.index.bytes -> 10485760, flush.messages -> 9223372036854775807}. (kafka.log.LogManager)
[2018-10-15 21:22:59,662] INFO [Partition my-topic-0 broker=0] No checkpointed highwatermark is found for partition my-topic-0 (kafka.cluster.Partition)
[2018-10-15 21:22:59,704] INFO Replica loaded for partition my-topic-0 with initial high watermark 0 (kafka.cluster.Replica)
[2018-10-15 21:22:59,706] INFO [Partition my-topic-0 broker=0] my-topic-0 starts at Leader Epoch 0 from offset 0. Previous Leader Epoch was: -1 (kafka.cluster.Partition)
[2018-10-15 21:22:59,831] INFO [ReplicaAlterLogDirsManager on broker 0] Added fetcher for partitions List() (kafka.server.ReplicaAlterLogDirsManager)
```

### Create project

Open [Spring Initializr](https://start.spring.io/) page and setup project structure as below

<img width="880" src="https://user-images.githubusercontent.com/3359299/46987793-b180ab80-d0c3-11e8-86aa-2d161d9fa4c7.PNG" />

Click "Create Project" button and download project zip file, extract it to local directory and open the project in Intellij or Eclipse.

### Maven pom

Add follow dependencies to Maven pom.xml

```xml
    <dependency>
      <groupId>org.apache.camel</groupId>
      <artifactId>camel-core</artifactId>
      <version>2.22.1</version>
    </dependency>
    <dependency>
      <groupId>org.apache.camel</groupId>
      <artifactId>camel-kafka</artifactId>
      <version>2.22.1</version>
    </dependency>
```

### Create application yaml file

>applicaiton.yml
```yaml
kafka:
  server : localhost
  port : 9092
  topic : my-topic
  consumerGroup : my-ConsumerGroup
``` 

 ### Camel router
 
 **Kafka message consumer**
 
 ```java
    from("kafka:{{kafka.topic}}?brokers={{kafka.server}}:{{kafka.port}}&groupId={{kafka.consumerGroup}}")
        .bean(MessageConsumer.class);
 ```
 
 **Kafka message producer**
 
 The producer produce a message every 5 seconds
 ```java
    from("timer://producer?period=5000")
         .bean(MessageProducer.class)
         .to("kafka:{{kafka.topic}}?brokers={{kafka.server}}:{{kafka.port}}");
 ```
 
 **MessageProducer.java**
 
 ```java
 public class MessageProducer {
     public String produceMessage() {
         return "Hello from Apache Camel " + UUID.randomUUID().toString();
     }
 }
 ```
 
 **MessageConsumer.java**
 
 ```Java
 public class MessageConsumer {
     private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
     
     public void printKafkaBody(String body) {
         logger.info(body);
     }
 }
 ```
 
 **Application class**
 
 ```
 @SpringBootApplication
 public class KafkaApplication {
 
     public static void main(String[] args) {
         SpringApplication.run(KafkaApplication.class, args);
         while (true) {
         }
     }
 }
 ```
 
 ### Run class
 
 Start the application, it output
 
 ```
 2018-10-16 00:04:26.696  INFO 9376 --- [           main] io.examples.kafka.KafkaApplication       : Starting KafkaApplication on Gary-PC with PID 9376 (E:\Workspaces\learn-30things-in-30days\day15-spring-camel-kafka\target\classes started by Gary in E:\Workspaces\learn-30things-in-30days)
 2018-10-16 00:04:26.700  INFO 9376 --- [           main] io.examples.kafka.KafkaApplication       : No active profile set, falling back to default profiles: default
 2018-10-16 00:04:26.813  INFO 9376 --- [           main] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@4b5a5ed1: startup date [Tue Oct 16 00:04:26 EDT 2018]; root of context hierarchy
 2018-10-16 00:04:28.258  INFO 9376 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'org.apache.camel.spring.boot.CamelAutoConfiguration' of type [org.apache.camel.spring.boot.CamelAutoConfiguration$$EnhancerBySpringCGLIB$$d1bf4fab] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
 2018-10-16 00:04:28.544  INFO 9376 --- [           main] o.a.c.i.converter.DefaultTypeConverter   : Type converters loaded (core: 195, classpath: 1)
 2018-10-16 00:04:29.145  INFO 9376 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
 2018-10-16 00:04:29.207  INFO 9376 --- [           main] o.a.camel.spring.boot.RoutesCollector    : Loading additional Camel XML routes from: classpath:camel/*.xml
 2018-10-16 00:04:29.208  INFO 9376 --- [           main] o.a.camel.spring.boot.RoutesCollector    : Loading additional Camel XML rests from: classpath:camel-rest/*.xml
 2018-10-16 00:04:29.213  INFO 9376 --- [           main] o.a.camel.spring.SpringCamelContext      : Apache Camel 2.22.1 (CamelContext: camel-1) is starting
 2018-10-16 00:04:29.215  INFO 9376 --- [           main] o.a.c.m.ManagedManagementStrategy        : JMX is enabled
 2018-10-16 00:04:29.442  INFO 9376 --- [           main] o.a.camel.spring.SpringCamelContext      : StreamCaching is not in use. If using streams then its recommended to enable stream caching. See more details at http://camel.apache.org/stream-caching.html
 2018-10-16 00:04:29.527  INFO 9376 --- [           main] o.a.k.clients.producer.ProducerConfig    : ProducerConfig values: 
 
 2018-10-16 00:04:29.847  INFO 9376 --- [           main] o.a.kafka.common.utils.AppInfoParser     : Kafka version : 1.0.2
 2018-10-16 00:04:29.847  INFO 9376 --- [           main] o.a.kafka.common.utils.AppInfoParser     : Kafka commitId : 2a121f7b1d402825
 2018-10-16 00:04:29.853  INFO 9376 --- [           main] o.a.camel.component.kafka.KafkaConsumer  : Starting Kafka consumer on topic: my-topic with breakOnFirstError: false
 2018-10-16 00:04:29.860  INFO 9376 --- [           main] o.a.k.clients.consumer.ConsumerConfig    : ConsumerConfig values: 
 
 2018-10-16 00:04:29.888  INFO 9376 --- [           main] o.a.kafka.common.utils.AppInfoParser     : Kafka version : 1.0.2
 2018-10-16 00:04:29.888  INFO 9376 --- [           main] o.a.kafka.common.utils.AppInfoParser     : Kafka commitId : 2a121f7b1d402825
 2018-10-16 00:04:29.889  INFO 9376 --- [           main] o.a.camel.spring.SpringCamelContext      : Route: route1 started and consuming from: kafka://my-topic?brokers=localhost%3A9092&groupId=my-consumerGroup
 2018-10-16 00:04:29.893  INFO 9376 --- [           main] o.a.camel.spring.SpringCamelContext      : Route: route2 started and consuming from: timer://producer?period=5000
 2018-10-16 00:04:29.894  INFO 9376 --- [           main] o.a.camel.spring.SpringCamelContext      : Total 2 routes, of which 2 are started
 2018-10-16 00:04:29.895  INFO 9376 --- [           main] o.a.camel.spring.SpringCamelContext      : Apache Camel 2.22.1 (CamelContext: camel-1) started in 0.681 seconds
 2018-10-16 00:04:29.903  INFO 9376 --- [           main] io.examples.kafka.KafkaApplication       : Started KafkaApplication in 3.581 seconds (JVM running for 4.035)
 2018-10-16 00:04:29.920  INFO 9376 --- [sumer[my-topic]] o.a.camel.component.kafka.KafkaConsumer  : Subscribing my-topic-Thread 0 to topic my-topic
 2018-10-16 00:04:30.033  INFO 9376 --- [sumer[my-topic]] o.a.k.c.c.internals.AbstractCoordinator  : [Consumer clientId=consumer-1, groupId=my-consumerGroup] Discovered group coordinator Gary-PC:9092 (id: 2147483647 rack: null)
 2018-10-16 00:04:30.034  INFO 9376 --- [sumer[my-topic]] o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-1, groupId=my-consumerGroup] Revoking previously assigned partitions []
 2018-10-16 00:04:30.035  INFO 9376 --- [sumer[my-topic]] o.a.k.c.c.internals.AbstractCoordinator  : [Consumer clientId=consumer-1, groupId=my-consumerGroup] (Re-)joining group
 2018-10-16 00:04:30.075  INFO 9376 --- [sumer[my-topic]] o.a.k.c.c.internals.AbstractCoordinator  : [Consumer clientId=consumer-1, groupId=my-consumerGroup] Successfully joined group with generation 9
 2018-10-16 00:04:30.076  INFO 9376 --- [sumer[my-topic]] o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-1, groupId=my-consumerGroup] Setting newly assigned partitions [my-topic-0]
 ```
 
 Then the producer produce a message every 5 second, and the consumer print it to console
 
 ```
 2018-10-16 00:04:35.919  INFO 9376 --- [sumer[my-topic]] io.examples.kafka.MessageConsumer        : Hello from Apache Camel f0df43e1-cf99-40f5-992b-bdddb039a39e
 2018-10-16 00:04:40.900  INFO 9376 --- [sumer[my-topic]] io.examples.kafka.MessageConsumer        : Hello from Apache Camel 49614179-ce69-44bf-874a-b5fa753281c6
 2018-10-16 00:04:45.899  INFO 9376 --- [sumer[my-topic]] io.examples.kafka.MessageConsumer        : Hello from Apache Camel 890f04af-7867-4b0d-96a6-40a7612c2c89
 2018-10-16 00:04:50.900  INFO 9376 --- [sumer[my-topic]] io.examples.kafka.MessageConsumer        : Hello from Apache Camel eed7b802-10ae-4f04-8307-6f3d957b8382
 2018-10-16 00:04:55.900  INFO 9376 --- [sumer[my-topic]] io.examples.kafka.MessageConsumer        : Hello from Apache Camel 73f78141-d501-4043-aaf0-c00e75fb2ee2
 2018-10-16 00:05:00.902  INFO 9376 --- [sumer[my-topic]] io.examples.kafka.MessageConsumer        : Hello from Apache Camel 28d27fd5-5352-4ee0-8d48-4181fe4780c1
 ```
 
 That's all for today, you can find the complete source code under [this folder](.).
 