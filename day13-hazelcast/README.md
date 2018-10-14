# Day 13: Hazelcast - an open source in-memory data grid based on Java

Today I'm going to learn Hazelcast and how to programming on Hazelcast on Java

## What is Hazelcast

Hazelcast is the Leading Open Source In-Memory Data Grid: Distributed Computing, Simplified.

The main areas where Hazelcast can do really wonders are: in-memory data grid, caching, in-memory NoSQL, messaging, application scaling, and clustering.

The Hazelcast platform can manage memory for many different types of applications. It offers an Open Binary Client Protocol to support APIs for any binary programing language. The Hazelcast and open source community members have created client APIs for programming languages that include Java, Scala, .NET Framework, C++, Python, Node.js, Golang and Clojure. Java and Scala can be used for both clients and embedded members.

## Introduction

**Java hashmap**

```java
Map map = new HashMap();
map.put("key-1", "valye-1");
...
map.get("key-1");
```

**BUT THIS IS NOT THREAD SAFE**

so we have ConcurrentHaspMap to share map by multiple threads

```java
Map map = new ConcurrentHaspMap();
map.put("key-1", "valye-1");
...
map.get("key-1");
```

**What if you want share a Map cross to multiple JVM???**

Hazelcast is the answer

```java
HazelcastInstance hazelcast = Hazelcast.newHazelcastIntance(new Config());
...
Map map = hazelcast.getMap("my-map");
map.put("key-1", "valye-1");
...
map.get("key-1");
```

- Open source
- 5MB single jar
- no-dependency
- maven-friendly

## Running mode ##

- Embedded
- Client / Server

**Embedded Mode**

<img width="600" src="https://user-images.githubusercontent.com/3359299/46912317-2b882780-cf40-11e8-8591-803c4e023754.PNG"/>
 

**Client / Server mode**

<img width="600" src="https://user-images.githubusercontent.com/3359299/46912319-30e57200-cf40-11e8-93c4-22a046efd542.PNG" />

## Try Hazelcast connections

- Download Hazelcast from https://hazelcast.org/download/
- unzip it to local directory
- Go do demo directory
- execute console.bat

it shows

```
Oct 14, 2018 12:01:36 AM com.hazelcast.spi.impl.operationservice.impl.BackpressureRegulator
INFO: [192.168.99.1]:5701 [dev] [3.10.6] Backpressure is disabled
Oct 14, 2018 12:01:36 AM com.hazelcast.spi.impl.operationservice.impl.InboundResponseHandlerSupplier
INFO: [192.168.99.1]:5701 [dev] [3.10.6] Running with 2 response threads
Oct 14, 2018 12:01:37 AM com.hazelcast.instance.Node
INFO: [192.168.99.1]:5701 [dev] [3.10.6] Creating MulticastJoiner
Oct 14, 2018 12:01:37 AM com.hazelcast.spi.impl.operationexecutor.impl.OperationExecutorImpl
INFO: [192.168.99.1]:5701 [dev] [3.10.6] Starting 4 partition threads and 3 generic threads (1 dedicated for priority tasks)
Oct 14, 2018 12:01:37 AM com.hazelcast.internal.diagnostics.Diagnostics
INFO: [192.168.99.1]:5701 [dev] [3.10.6] Diagnostics disabled. To enable add -Dhazelcast.diagnostics.enabled=true to the JVM arguments.
Oct 14, 2018 12:01:37 AM com.hazelcast.core.LifecycleService
INFO: [192.168.99.1]:5701 [dev] [3.10.6] [192.168.99.1]:5701 is STARTING
Oct 14, 2018 12:01:39 AM com.hazelcast.system
INFO: [192.168.99.1]:5701 [dev] [3.10.6] Cluster version set to 3.10
Oct 14, 2018 12:01:39 AM com.hazelcast.internal.cluster.ClusterService
INFO: [192.168.99.1]:5701 [dev] [3.10.6]

Members {size:1, ver:1} [
        Member [192.168.99.1]:5701 - f2d88583-d86a-40f1-895d-521b39e52c69 this
]

Oct 14, 2018 12:01:39 AM com.hazelcast.core.LifecycleService
INFO: [192.168.99.1]:5701 [dev] [3.10.6] [192.168.99.1]:5701 is STARTED
Oct 14, 2018 12:01:39 AM com.hazelcast.internal.partition.impl.PartitionStateManager
INFO: [192.168.99.1]:5701 [dev] [3.10.6] Initializing cluster partition table arrangement...
hazelcast[default] >
```

Hazelcast server started on [192.168.99.1]:5701

- Open another command prompt, and execute same console.bat

it displays

```
Oct 14, 2018 12:05:45 AM com.hazelcast.system
INFO: [192.168.99.1]:5702 [dev] [3.10.6] Configured Hazelcast Serialization version: 1
Oct 14, 2018 12:05:45 AM com.hazelcast.instance.Node
INFO: [192.168.99.1]:5702 [dev] [3.10.6] A non-empty group password is configured for the Hazelcast member. Starting with Hazelcast version 3.8.2, members with the same group name, but with different group passwords (that do not use authentication) form a cluster. The group password configuration will be removed completely in a future release.
Oct 14, 2018 12:05:46 AM com.hazelcast.spi.impl.operationservice.impl.BackpressureRegulator
INFO: [192.168.99.1]:5702 [dev] [3.10.6] Backpressure is disabled
Oct 14, 2018 12:05:46 AM com.hazelcast.spi.impl.operationservice.impl.InboundResponseHandlerSupplier
INFO: [192.168.99.1]:5702 [dev] [3.10.6] Running with 2 response threads
Oct 14, 2018 12:05:46 AM com.hazelcast.instance.Node
INFO: [192.168.99.1]:5702 [dev] [3.10.6] Creating MulticastJoiner
Oct 14, 2018 12:05:46 AM com.hazelcast.spi.impl.operationexecutor.impl.OperationExecutorImpl
INFO: [192.168.99.1]:5702 [dev] [3.10.6] Starting 4 partition threads and 3 generic threads (1 dedicated for priority tasks)
Oct 14, 2018 12:05:47 AM com.hazelcast.internal.diagnostics.Diagnostics
INFO: [192.168.99.1]:5702 [dev] [3.10.6] Diagnostics disabled. To enable add -Dhazelcast.diagnostics.enabled=true to the JVM arguments.
Oct 14, 2018 12:05:47 AM com.hazelcast.core.LifecycleService
INFO: [192.168.99.1]:5702 [dev] [3.10.6] [192.168.99.1]:5702 is STARTING
Oct 14, 2018 12:05:47 AM com.hazelcast.internal.cluster.impl.MulticastJoiner
INFO: [192.168.99.1]:5702 [dev] [3.10.6] Trying to join to discovered node: [192.168.99.1]:5701
Oct 14, 2018 12:05:47 AM com.hazelcast.nio.tcp.TcpIpConnector
INFO: [192.168.99.1]:5702 [dev] [3.10.6] Connecting to /192.168.99.1:5701, timeout: 0, bind-any: true
Oct 14, 2018 12:05:47 AM com.hazelcast.nio.tcp.TcpIpConnectionManager
INFO: [192.168.99.1]:5702 [dev] [3.10.6] Established socket connection between /192.168.99.1:61575 and /192.168.99.1:5701
Oct 14, 2018 12:05:53 AM com.hazelcast.system
INFO: [192.168.99.1]:5702 [dev] [3.10.6] Cluster version set to 3.10
Oct 14, 2018 12:05:53 AM com.hazelcast.internal.cluster.ClusterService
INFO: [192.168.99.1]:5702 [dev] [3.10.6]

Members {size:2, ver:2} [
        Member [192.168.99.1]:5701 - f2d88583-d86a-40f1-895d-521b39e52c69
        Member [192.168.99.1]:5702 - 450c23d5-2542-48e1-9a17-3f484311d03c this
]

Oct 14, 2018 12:05:54 AM com.hazelcast.core.LifecycleService
INFO: [192.168.99.1]:5702 [dev] [3.10.6] [192.168.99.1]:5702 is STARTED
hazelcast[default] >
```

And in console 1 also shows

```
Members {size:2, ver:2} [
        Member [192.168.99.1]:5701 - f2d88583-d86a-40f1-895d-521b39e52c69 this
        Member [192.168.99.1]:5702 - 450c23d5-2542-48e1-9a17-3f484311d03c
]

Oct 14, 2018 12:05:53 AM com.hazelcast.internal.partition.impl.MigrationManager
INFO: [192.168.99.1]:5701 [dev] [3.10.6] Re-partitioning cluster data... Migration queue size: 271
Oct 14, 2018 12:05:55 AM com.hazelcast.internal.partition.impl.MigrationThread
INFO: [192.168.99.1]:5701 [dev] [3.10.6] All migration tasks have been completed, queues are empty.
```

The second hazelcast started on [192.168.99.1]:5702, and both hazelcast connected to each other.

Let's open one more console

The third console shows

```
Members {size:3, ver:3} [
        Member [192.168.99.1]:5701 - f2d88583-d86a-40f1-895d-521b39e52c69
        Member [192.168.99.1]:5702 - 450c23d5-2542-48e1-9a17-3f484311d03c
        Member [192.168.99.1]:5703 - e3a0dc64-ab49-4bca-8a54-8504839d6290 this
```

All 3 consoles found 3 hazelcast members

>execute put command in console-1

```
hazelcast[default] > m.put mykey value-1
null
hazelcast[default] >
```

>Execute get command on same console
```
hazelcast[default] > m.get mykey
value-1
hazelcast[default] >
```

In console-2, execute same get command, do get same value

```
hazelcast[default] > m.get mykey
value-1
hazelcast[default] >
```

close console-1 and console-2, then execute same command in console-3

```
hazelcast[default] > m.get mykey
value-1
hazelcast[default] >
```

It got same results, this means the map are copied to all 3 hazelcast, and even some hazelcast is closed, the value is still in other instance.


## Programming on Hazelcast

Now let's start do some programming using Hazelcast, still use vert.x create a Microservice application.

This is a demo RESTful application implements below methods

|Method|Url|Description|
|:---|:---|:---|
|POST|/v1/hazelcast/{key}|Put a key to hazelcast|
|GET|/v1/hazelcast/{key}|Get a key from hazelcast|

**Init hazelcast map after http server started**

```java
    private Map myMap;

    @Override
    public void start(Future<Void> startFuture) {
        logger.debug("start http server");
        this.startHttpServer()
                .doAfterSuccess(s -> logger.debug("http server started on port {}", s.actualPort()))
                .subscribe(s -> {
                    Set<HazelcastInstance> instances = Hazelcast.getAllHazelcastInstances();
                    HazelcastInstance hz = instances.stream().findFirst().get();
                    myMap = hz.getMap("myMap");
                    logger.debug("map size:{}", myMap.size());
                    startFuture.complete();
                }, startFuture::fail);
    }
```

**http server**
```java
    private Single<HttpServer> startHttpServer() {
        Router router = Router.router(vertx);
        router.put("/v1/hazelcast/:key").handler(this::putKey);
        router.get("/v1/hazelcast/:key").handler(this::getKey);
        return vertx.createHttpServer()
                .requestHandler(router::accept)
                .rxListen(this.config().getInteger(KEY_PORT));
    }
```

**GET handler**
```java
    private void getKey(RoutingContext context) {
        String key = context.request().getParam("key");
        logger.debug("Received getKey request, key={}", key);
        String value = (String) myMap.get(key);
        logger.debug("get value:{}", value);
        this.keyValueResponse(context, key, value);
    }
```

**PUT handler**
 ```java
     private void putKey(RoutingContext context) {
         String key = context.request().getParam("key");
         context.request().bodyHandler(buffer -> {
             logger.debug("Request body:{}", buffer.toJsonObject());
             String value = buffer.toJsonObject().getString("value");
             logger.debug("put value:{}", value);
             myMap.put(key, value);
             this.keyValueResponse(context, key, value);
 
         });
     }
 ```
 
 **Create a config file for instance 1**
 >config-1.json
 
 ```json
 {
   "port": 9080
 }
 ```
 
**Create a config file for instance 2**
>config-1.json
  
```json
{
  "port": 9081
}
```

**Build**
>mvn clean install

**Execute application**

open a command prompt, run command
>java -jar target\hazelcast-0.0.1-SNAPSHOT-fat.jar --cluster --conf config-1.json

It displays
```
Members [1] {
        Member [192.168.99.1]:5701 - 5fe737b9-feca-4969-a71d-d126af65b8ed this
}

Oct 14, 2018 9:10:03 AM com.hazelcast.core.LifecycleService
INFO: [192.168.99.1]:5701 [dev] [3.8.2] [192.168.99.1]:5701 is STARTED
Oct 14, 2018 9:10:03 AM com.hazelcast.internal.partition.impl.PartitionStateManager
INFO: [192.168.99.1]:5701 [dev] [3.8.2] Initializing cluster partition table arrangement...
09:10:03.886 [vert.x-eventloop-thread-1] DEBUG io.examples.hazelcast.ServerVerticle - start http server
09:10:03.960 [vert.x-eventloop-thread-1] DEBUG io.examples.hazelcast.ServerVerticle - map size:0
09:10:03.962 [vert.x-eventloop-thread-1] DEBUG io.examples.hazelcast.ServerVerticle - http server started on port 9080
Oct 14, 2018 9:10:03 AM io.vertx.core.impl.launcher.commands.VertxIsolatedDeployer
INFO: Succeeded in deploying verticle
```

There is 1 hazelcast instance created and http server is started on port 9080


open another command prompt, run command
>java -jar target\hazelcast-0.0.1-SNAPSHOT-fat.jar --cluster --conf config-2.json

It displays

```
Members [2] {
        Member [192.168.99.1]:5701 - 5fe737b9-feca-4969-a71d-d126af65b8ed
        Member [192.168.99.1]:5702 - 8e8b64fd-d6b9-4825-be64-d41cf0882f21 this
}

Oct 14, 2018 9:12:16 AM com.hazelcast.core.LifecycleService
INFO: [192.168.99.1]:5702 [dev] [3.8.2] [192.168.99.1]:5702 is STARTED
09:12:17.469 [vert.x-eventloop-thread-1] DEBUG io.examples.hazelcast.ServerVerticle - start http server
09:12:17.526 [vert.x-eventloop-thread-1] DEBUG io.examples.hazelcast.ServerVerticle - map size:0
09:12:17.528 [vert.x-eventloop-thread-1] DEBUG io.examples.hazelcast.ServerVerticle - http server started on port 9081
Oct 14, 2018 9:12:17 AM io.vertx.core.impl.launcher.commands.VertxIsolatedDeployer
INFO: Succeeded in deploying verticle
```

The second http server is started on 9081

In first command prompt also shows 2 hazelcast instance are connected

```
Oct 14, 2018 9:12:13 AM com.hazelcast.nio.tcp.SocketAcceptorThread
INFO: [192.168.99.1]:5701 [dev] [3.8.2] Accepting socket connection from /192.168.99.1:57350
Oct 14, 2018 9:12:13 AM com.hazelcast.nio.tcp.TcpIpConnectionManager
INFO: [192.168.99.1]:5701 [dev] [3.8.2] Established socket connection between /192.168.99.1:5701 and /192.168.99.1:57350
Oct 14, 2018 9:12:14 AM com.hazelcast.internal.cluster.ClusterService
INFO: [192.168.99.1]:5701 [dev] [3.8.2]

Members [2] {
        Member [192.168.99.1]:5701 - 5fe737b9-feca-4969-a71d-d126af65b8ed this
        Member [192.168.99.1]:5702 - 8e8b64fd-d6b9-4825-be64-d41cf0882f21
}

Oct 14, 2018 9:12:14 AM com.hazelcast.internal.partition.impl.MigrationManager
INFO: [192.168.99.1]:5701 [dev] [3.8.2] Re-partitioning cluster data... Migration queue size: 271
Oct 14, 2018 9:12:16 AM com.hazelcast.internal.partition.impl.MigrationThread
INFO: [192.168.99.1]:5701 [dev] [3.8.2] All migration tasks have been completed, queues are empty.
```

Now use postman to test the application

- URL: http://localhost:9080/v1/hazelcast/mykey
- Method: PUT
- Body:
>{"value": "value1"}

The value is saved to hazelcast from first http server

Then try to get the value from second http server 

- URL: http://localhost:9081/v1/hazelcast/mykey
- Method: GET

Results:
```json
{
    "mykey": "value1"
}
```

That's all for today, you can find the complete source code under [this folder](.).


