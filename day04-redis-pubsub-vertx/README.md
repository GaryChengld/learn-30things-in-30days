# Day 4: Redis message publish/subscribe programming on Vert.x

Redis is a key/value in memory data store, but it also has an utility which enable you to do quick messaging and communication between processes. There’s plenty of other messaging systems out there, but Redis is worth a look too.

Today I will create a simple Redis message publish/subscribe application by Java on Vert.x.

Let's take a look on Redis publish and subscribe command first.

 - SUBSCRIBE command listens to a channel
 - PUBLISH pushes a message into a channel
 
 Start Redis server and open 2 Redis-cli programs, in first Redis client, execute SUBSCRIBE command to listen my-channel
 ```
 redis 127.0.0.1:6379> subscribe my-channel
 Reading messages... (press Ctrl-C to quit)
 1) "subscribe"
 2) "my-channel"
 3) (integer) 1
 ```
 Please note that redis-cli will not accept any commands once in subscribed mode and can only quit the mode with Ctrl-C.
 
 In second Redis client, publish a message to my-channel
 ```
 redis 127.0.0.1:6379> publish my-channel "hello"
 (integer) 1
 redis 127.0.0.1:6379>
 ```
 The message "hello" is received in first Redis client
 ```
 redis 127.0.0.1:6379> subscribe my-channel
 Reading messages... (press Ctrl-C to quit)
 1) "subscribe"
 2) "my-channel"
 3) (integer) 1
 1) "message"
 2) "my-channel"
 3) "hello"
 ```
 
 Now I start create a Vert.x based java program to publish/subscribe message through Redis.
 
 Create a maven project with following dependencies
 ```xml
   <dependencyManagement>
     <dependencies>
       <dependency>
         <groupId>io.vertx</groupId>
         <artifactId>vertx-dependencies</artifactId>
         <version>${vertx.version}</version>
         <type>pom</type>
         <scope>import</scope>
       </dependency>
     </dependencies>
   </dependencyManagement>
 
   <dependencies>
     <dependency>
       <groupId>io.vertx</groupId>
       <artifactId>vertx-core</artifactId>
     </dependency>
     <dependency>
       <groupId>io.vertx</groupId>
       <artifactId>vertx-web</artifactId>
     </dependency>
     <dependency>
       <groupId>io.vertx</groupId>
       <artifactId>vertx-rx-java2</artifactId>
     </dependency>
     <dependency>
       <groupId>io.vertx</groupId>
       <artifactId>vertx-redis-client</artifactId>
     </dependency>
     <dependency>
       <groupId>ch.qos.logback</groupId>
       <artifactId>logback-classic</artifactId>
       <version>1.2.3</version>
     </dependency>
   </dependencies>
 ```
 
 First step, create a program which can publish messages to Redis channel only
 
 