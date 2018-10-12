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