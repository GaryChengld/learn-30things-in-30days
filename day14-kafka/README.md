# Day 14: Apache Kafka - an open-source stream-processing software platform

Today, I'm starting my new journey, with this Apache Kafka.

## Introduction

### What is Kafka?

Kafka is a high-performance, real-time messaging system, It is an open source tool and it a part of Apache projects, it has following characteristics:

- It is a distributed and partitioned messaging system.
- It is highly fault-tolerant.
- It is highly scalable.
- It can process and send millions of messages per second to several receivers.

### Kafka history

- It was originally developed by LinkedIn and handed over to the open source community in 2011
- It became a main Apache project in 2012
- A stable version 0.8.2.0 was release in Feb, 2015

### Use cases

- Messaging service - Millions of messages can be sent and received in real-time.
- Real-time stream processing - It can be used to process a continuous steam of information in real-tme and pass it to stream processing system such as Storm.
- Log aggregation - Kafka can be used to collect physical log files from multiple systems and store them in a central location such as HDFS.
- Commit log service - Kafka can be used as an external commit log for distributed systems.
- Event sourcing - A time order sequence of events can by maintained through Kafka.

### Kafka Data Model

The Kafka data model consists of messages and topics

- Messages represent information such as lines in a log file, a row of stock market data, or an error message from a system
- Messages are grouped into categories called topics. Example: LogMessage and StockMessage.
- The processes that publish messages into a topic in Kafka are know as producers.
- The processes that receive the messages from a topic in Kafka are know as consumers.
- The processes or servers within Kafka that process the messages are know as brokers.

### Topics

A topic is a category of messages in Kafka

- The producers publish the messages in to topics.
- The consumers read the messages from topics.
- A topic is divided into one or more partitions.
- A partition is also know as a commit log.
- Each partition contains an ordered set of messages.
- Each message is identified by its offset in the partition.
- Message are added at one end of the partition and consumed at the other.

<img width="600" src="https://user-images.githubusercontent.com/3359299/46923759-b32a7080-cfea-11e8-854a-c5318633f2fd.PNG" />

### Partitions

Topics are divided into partitions, which are the unit of parallelism in Kafka.

- Partitions allow message in a topic to be distributed to multiple servers.
- A topic can have any number f partitions.
- Each partition should fit in a single Kafka server.
- The number of partitions decide the parallelism of the topic.

### Partition Distribution

Partitions can be distributed across the Kafka cluster.

- Each Kafka server may handle one or more partitions.
- A partition can be replicated across several servers for fault-tolerance.
- One server is marked as a leader for the partition and the others are marked as followers.
- The leader controls the read and write for the partition, whereas, the followers replicate the data.
- If a leader fails, one of the followers automatically become the leader.
- Zookeeper is used for the leader selection.

### Producers

The producer is the creator of the message in Kafka.

- The producers place the message to a particular topic.
- The producers also decide which partition to place the message into.
- Topics should already exist before a message is placed by the producer.
- Messages are added at one end of the partition.

<img width="600" src="https://user-images.githubusercontent.com/3359299/46924083-d22b0180-cfee-11e8-9800-562700009a7d.PNG" />

### Consumers

The consumer is the receiver of the message in Kafka.

- Each consumer belongs to a consumer group.
- A consumer group may have one or more consumers.
- The consumers specify what topic they want to listen to.
- A message is send to all the consumers in a consumer group.
- The consumer groups are used to control the messaging system.

<img width="600" src="https://user-images.githubusercontent.com/3359299/46924260-030c3600-cff1-11e8-81a2-ae35de779ecc.PNG"/>

### Kafka Architecture

Kafka architecture consists of brokers that take messages from the producers and add to a partition of a topic. Brokers provide the messages to the consumers from the partitions.

- A topic is divided into multiple partitions
- The message are added to the partitions at one end and consumed in the same order.
- Each partition acts as a message queue.
- Consumers are divided into consumer groups.
- Each message is delivered to one consumer in each consumer group.
- Zookeeper is used for coordination.

### Type of messaging systems

Kafka architecture supports the publish-subscribe and queue system.

<img width="880" src="https://user-images.githubusercontent.com/3359299/46924552-fccb8900-cff3-11e8-9706-cf4807ffb0ac.PNG" />

The implementation of queue system

<img width="880" src="https://user-images.githubusercontent.com/3359299/46924635-b4609b00-cff4-11e8-91ce-b89f0b1a7bdf.PNG" />

The implementation of publish-subscribe system

<img width="880" src="https://user-images.githubusercontent.com/3359299/46924637-b9bde580-cff4-11e8-967f-0b17b330772b.PNG" />

### Brokers

Brokers are the Kafka processes that process the messages in Kafka.

- Each machine in the cluster can run one broker.
- The broker coordinate among each other using Zookeeper.
- One broker acts as a leader for a partition and handles the delivery and persistence, whereas, the others act as followers.
- Brokers receive the message from the producer and send it to consumer groups.

### Kafka Guarantees

Kafka guarantees the following:

1. Messages sent by a producer to a topic and a partition are appended in the same order.
2. A consumer instance gets the messages in the same order as they are produced.
3. A topic with replication factor N, tolerates up  to N-1 server failures.

### Replication in Kafka

- One machine (one replica) is called a leader and is chosen as the primary; the remaining machines (replicas) are chosen as the followers ad act as backups.
- The leader propagates the writes to the followers.
- The leader waits until the writes are completed on all the replicas.
- If a replica is down, it is skipped for the write until it comes back.
- If the leader fails, one of the followers will be chosen as the new leader; this mechanism can tolerate n-1 failures f replication factor is 'n'.

### Persistence in Kafka

Kafka uses the Linux file system for persistence of messages.

- Persistence ensures no messages are lost.
- Kafka relies on the file system page cache for fast reads and writes.
- All the data is immediately written to a file in file system.
- Messages are grouped as message sets for more efficient writes.
- Message sets can be compressed to reduce network bandwidth.
- A standardized binary message format is used among producers, brokers, and consumers to minimize data modification.

## Install Apache Kafka in windows

Prerequisite

1. Install JDK or JRE in windows
2. Install and Configure Apache Zookeeper

### Download Apache Kafka

Download Apache Kafka from [here](https://kafka.apache.org/downloads), the one I downloaded is Scala 2.11 binary package.

Extract the package to local windows directory.

### Config Apache Kafka

1. Go to config folder in Apache Kafka and edit “server.properties”
2. Find log.dirs and repelace after “=/tmp/kafka-logs” to “=C:\\Development\\kafka_2.11-2.0.0\\logs”
3. Leave other setting as is. If your Apache Zookeeper on different server then change the “zookeeper.connect” property. 
4. By default Apache Kafka will run on port 9092 and Apache Zookeeper will run on port 2181.

### Running Apache Kafka

**Start Apache Zookeeper**

Open a command prompt, go to Zookeeper bin directory, execute command
>zkServer.cmd

Zookeeper will be started, and following output shows in the command prompt

```
2018-10-14 23:17:24,471 [myid:] - INFO  [main:Environment@100] - Server environment:java.io.tmpdir=C:\Users\Gary\AppData\Local\Temp\
2018-10-14 23:17:24,476 [myid:] - INFO  [main:Environment@100] - Server environment:java.compiler=<NA>
2018-10-14 23:17:24,481 [myid:] - INFO  [main:Environment@100] - Server environment:os.name=Windows 10
2018-10-14 23:17:24,482 [myid:] - INFO  [main:Environment@100] - Server environment:os.arch=amd64
2018-10-14 23:17:24,483 [myid:] - INFO  [main:Environment@100] - Server environment:os.version=10.0
2018-10-14 23:17:24,486 [myid:] - INFO  [main:Environment@100] - Server environment:user.name=Gary
2018-10-14 23:17:24,488 [myid:] - INFO  [main:Environment@100] - Server environment:user.home=C:\Users\Gary
2018-10-14 23:17:24,491 [myid:] - INFO  [main:Environment@100] - Server environment:user.dir=C:\Development\zookeeper-3.4.12\bin
2018-10-14 23:17:24,526 [myid:] - INFO  [main:ZooKeeperServer@835] - tickTime set to 2000
2018-10-14 23:17:24,527 [myid:] - INFO  [main:ZooKeeperServer@844] - minSessionTimeout set to -1
2018-10-14 23:17:24,528 [myid:] - INFO  [main:ZooKeeperServer@853] - maxSessionTimeout set to -1
2018-10-14 23:17:24,766 [myid:] - INFO  [main:ServerCnxnFactory@117] - Using org.apache.zookeeper.server.NIOServerCnxnFactory as server connection factory
2018-10-14 23:17:24,768 [myid:] - INFO  [main:NIOServerCnxnFactory@89] - binding to port 0.0.0.0/0.0.0.0:2181

```

**Start Apache Kafka**

Open another command prompt, go to kafka directory, execute command
>.\bin\windows\kafka-server-start.bat .\config\server.properties

Kafka will be started and shows follow outputs

```
[2018-10-14 23:23:45,349] INFO Loading logs. (kafka.log.LogManager)
[2018-10-14 23:23:45,356] INFO Logs loading complete in 7 ms. (kafka.log.LogManager)
[2018-10-14 23:23:45,365] INFO Starting log cleanup with a period of 300000 ms. (kafka.log.LogManager)
[2018-10-14 23:23:45,367] INFO Starting log flusher with a default period of 9223372036854775807 ms. (kafka.log.LogManager)
[2018-10-14 23:23:45,598] INFO Awaiting socket connections on 0.0.0.0:9092. (kafka.network.Acceptor)
[2018-10-14 23:23:45,631] INFO [SocketServer brokerId=0] Started 1 acceptor threads (kafka.network.SocketServer)
[2018-10-14 23:23:45,647] INFO [ExpirationReaper-0-Produce]: Starting (kafka.server.DelayedOperationPurgatory$ExpiredOperationReaper)
[2018-10-14 23:23:45,648] INFO [ExpirationReaper-0-Fetch]: Starting (kafka.server.DelayedOperationPurgatory$ExpiredOperationReaper)
[2018-10-14 23:23:45,650] INFO [ExpirationReaper-0-DeleteRecords]: Starting (kafka.server.DelayedOperationPurgatory$ExpiredOperationReaper)
[2018-10-14 23:23:45,662] INFO [LogDirFailureHandler]: Starting (kafka.server.ReplicaManager$LogDirFailureHandler)
[2018-10-14 23:23:50,208] INFO Creating /brokers/ids/0 (is it secure? false) (kafka.zk.KafkaZkClient)
[2018-10-14 23:23:50,232] INFO Result of znode creation at /brokers/ids/0 is: OK (kafka.zk.KafkaZkClient)
[2018-10-14 23:23:50,233] INFO Registered broker 0 at path /brokers/ids/0 with addresses: ArrayBuffer(EndPoint(Gary-PC,9092,ListenerName(PLAINTEXT),PLAINTEXT)) (kafka.zk.KafkaZkClient)
[2018-10-14 23:23:50,274] INFO [ExpirationReaper-0-Rebalance]: Starting (kafka.server.DelayedOperationPurgatory$ExpiredOperationReaper)
[2018-10-14 23:23:50,276] INFO [ExpirationReaper-0-Heartbeat]: Starting (kafka.server.DelayedOperationPurgatory$ExpiredOperationReaper)
[2018-10-14 23:23:50,277] INFO [ExpirationReaper-0-topic]: Starting (kafka.server.DelayedOperationPurgatory$ExpiredOperationReaper)
[2018-10-14 23:23:50,275] INFO Creating /controller (is it secure? false) (kafka.zk.KafkaZkClient)
[2018-10-14 23:23:50,317] INFO Result of znode creation at /controller is: OK (kafka.zk.KafkaZkClient)
[2018-10-14 23:23:50,322] INFO [GroupCoordinator 0]: Starting up. (kafka.coordinator.group.GroupCoordinator)
[2018-10-14 23:23:50,323] INFO [GroupCoordinator 0]: Startup complete. (kafka.coordinator.group.GroupCoordinator)
[2018-10-14 23:23:50,325] INFO [GroupMetadataManager brokerId=0] Removed 0 expired offsets in 2 milliseconds. (kafka.coordinator.group.GroupMetadataManager)
[2018-10-14 23:23:50,392] INFO [ProducerId Manager 0]: Acquired new producerId block (brokerId:0,blockStartProducerId:1000,blockEndProducerId:1999) by writing to Zk with path version 2 (kafka.coordinator.transaction.ProducerIdManager)
[2018-10-14 23:23:50,410] INFO [TransactionCoordinator id=0] Starting up. (kafka.coordinator.transaction.TransactionCoordinator)
[2018-10-14 23:23:50,412] INFO [Transaction Marker Channel Manager 0]: Starting (kafka.coordinator.transaction.TransactionMarkerChannelManager)
[2018-10-14 23:23:50,412] INFO [TransactionCoordinator id=0] Startup complete. (kafka.coordinator.transaction.TransactionCoordinator)
[2018-10-14 23:23:50,445] INFO [/config/changes-event-process-thread]: Starting (kafka.common.ZkNodeChangeNotificationListener$ChangeEventProcessThread)
[2018-10-14 23:23:50,454] INFO [SocketServer brokerId=0] Started processors for 1 acceptors (kafka.network.SocketServer)
[2018-10-14 23:23:50,458] INFO Kafka version : 2.0.0 (org.apache.kafka.common.utils.AppInfoParser)
[2018-10-14 23:23:50,458] INFO Kafka commitId : 3402a8361b734732 (org.apache.kafka.common.utils.AppInfoParser)
[2018-10-14 23:23:50,461] INFO [KafkaServer id=0] started (kafka.server.KafkaServer)

```

Now Kafka is started successfully on port 9092.















