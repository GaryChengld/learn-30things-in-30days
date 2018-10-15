#Day 14: Apache Kafka - an open-source stream-processing software platform

Today, I'm starting my new journey, with this Apache Kafka.

## What is Kafka?

Kafka is a high-performance, real-time messaging system, It is an open source tool and it a part of Apache projects, it has following characteristics:

- It is a distributed and partitioned messaging system.
- It is highly fault-tolerant.
- It is highly scalable.
- It can process and send millions of messages per second to several receivers.

## Kafka history

- It was originally developed by LinkedIn and handed over to the open source community in 2011
- It became a main Apache project in 2012
- A stable version 0.8.2.0 was release in Feb, 2015
- The latest version 0.8.2.1 was released in May, 2015

## Use cases

- Messaging service - Millions of messages can be sent and received in real-time.
- Real-time stream processing - It can be used to process a continuous steam of information in real-tme and pass it to stream processing system such as Storm.
- Log aggregation - Kafka can be used to collect physical log files from multiple systems and store them in a central location such as HDFS.
- Commit log service - Kafka can be used as an external commit log for distributed systems.
- Event sourcing - A time order sequence of events can by maintained through Kafka.

## Kafka Data Model

The Kafka data model consists of messages and topics

- Messages represent information such as lines in a log file, a row of stock market data, or an error message from a system
- Messages are grouped into categories called topics. Example: LogMessage and StockMessage.
- The processes that publish messages into a topic in Kafka are know as producers.
- The processes that receive the messages from a topic in Kafka are know as consumers.
- The processes or servers within Kafka that process the messages are know as brokers.

## Topics

A topic is a category of messages in Kafka

- The producers publish the messages in to topics.
- The consumers read the messages from topics.
- A topic is divided into one or more partitions.
- A partition is also know as a commit log.
- Each partition contains an ordered set of messages.
- Each message is identified by its offset in the partition.
- Message are added at one end of the partition and consumed at the other.

<img width="600" src="https://user-images.githubusercontent.com/3359299/46923759-b32a7080-cfea-11e8-854a-c5318633f2fd.PNG" />

## Partitions

Topics are divided into partitions, which are the unit of parallelism in Kafka.

- Partitions allow message in a topic to be distributed to multiple servers.
- A topic can have any number f partitions.
- Each partition should fit in a single Kafka server.
- The number of partitions decide the parallelism of the topic.

## Partition Distribution

Partitions can be distributed across the Kafka cluster.

- Each Kafka server may handle one or more partitions.
- A partition can be replicated across several servers for fault-tolerance.
- One server is marked as a leader for the partition and the others are marked as followers.
- The leader controls the read and write for the partition, whereas, the followers replicate the data.
- If a leader fails, one of the followers automatically become the leader.
- Zookeeper is used for the leader selection.

## Producers

The producer is the creator of the message in Kafka.

- The producers place the message to a particular topic.
- The producers also decide which partition to place the message into.
- Topics should already exist before a message is placed by the producer.
- Messages are added at one end of the partition.

<img width="600" src="https://user-images.githubusercontent.com/3359299/46924083-d22b0180-cfee-11e8-9800-562700009a7d.PNG" />

## Consumers

The consumer is the receiver of the message in Kafka.

- Each consumer belongs to a consumer group.
- A consumer group may have one or more consumers.
- The consumers specify what topic they want to listen to.
- A message is send to all the consumers in a consumer group.
- The consumer groups are used to control the messaging system.

<img width="600" src="https://user-images.githubusercontent.com/3359299/46924260-030c3600-cff1-11e8-81a2-ae35de779ecc.PNG"/>

## Kafka Architecture

Kafka architecture consists of brokers that take messages from the producers and add to a partition of a topic. Brokers provide the messages to the consumers from the partitions.

- A topic is divided into multiple partitions
- The message are added to the partitions at one end and consumed in the same order.
- Each partition acts as a message queue.
- Consumers are divided into consumer groups.
- Each message is delivered to one consumer in each consumer group.
- Zookeeper is used for coordination.

## Type of messaging systems

Kafka architecture supports the publish-subscribe and queue system.

<img width="880" src="https://user-images.githubusercontent.com/3359299/46924552-fccb8900-cff3-11e8-9706-cf4807ffb0ac.PNG" />


 