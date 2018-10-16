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
  consumerGroup : my-consumerGroup
``` 

  