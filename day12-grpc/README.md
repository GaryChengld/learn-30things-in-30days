# Day 12: gRPC - A Modern Toolkit for Microservice Communication

Today I'm going to learn a hot new buzz in technology call gRPC.

## What is gRPC

gRPC is a RPC platform developed by Google which was announced and made open source  in late Feb 2015.  The letters “gRPC” are a recursive acronym which means, gRPC Remote Procedure Call.

gRPC has wo parts, the gRPC protocol, and the data serialization. By default gRPC utilizes protobuf for serialization.

- HTTP/2
- protobuf serializaiton
- Client open one long-live connection to gRPC server
- Allows client-side and serve side streaming

## Protocol Buffers (protobuf)

Protocol buffers are a flexible, efficient, automated mechanism for serializing structured data. You define how you want your data to be structured once, then you can use special generated source code to easily write and read your structured data to and from a variety of data streams and using a variety of languages.

For example

```
// The greeter service definition.
service Greeter {
  // Sends a greeting
  rpc SayHello (HelloRequest) returns (HelloReply) {}
}

// The request message containing the user's name.
message HelloRequest {
  string name = 1;
}

// The response message containing the greetings
message HelloReply {
  string message = 1;
}
``` 

## gRPC vs REST

|gRPC|REST|
|:---|:---|
|protobuf|Json|
|HTTP/2|HTTP 1.1|
|Messages|HTTP Verb|
|Streaming|Request-Response|
|Strong Typing|Serialization|

## Language supported

- C++
- Java
- Python
- GO
- Ruby
- C#
- Node.js (JavaScript)
- Object C
- PHP


## Programming steps

1. Define a service protobuf in a .proto file
2. Generate server and client code using the protobuf compiler
3. Create the server application, implementing the generated service interfaces and spawning the gRPC server
4. Create the client application, making RPC calls using generated stubs

## Create a sample gRPC java program on vert.x

### Maven Dependencies

```xml
    <dependency>
      <groupId>io.vertx</groupId>
      <artifactId>vertx-grpc</artifactId>
    </dependency>
```

### Define protobuf file

**Create greeting.proto**

```proto
syntax = "proto3";

option java_multiple_files = true;
option java_package = "io.examples.grpc.greeting";
option java_outer_classname = "GreetingProto";
option objc_class_prefix = "HLW";

package greeting;

// The greeting service definition.
service Greeter {
  // Sends a greeting
  rpc SayHello (HelloRequest) returns (HelloReply) {}
}

// The request message containing the user's name.
message HelloRequest {
  string name = 1;
}

// The response message containing the greetings
message HelloReply {
  string message = 1;
}
```

### Generating the Code

There are 2 way to generate the code by proto file

1. Command line compiler

Download compiler from https://developers.google.com/protocol-buffers/docs/downloads.

You can use the following command to generate the code:

>protoc -I=$SRC_DIR --java_out=$DST_DIR $SRC_DIR/greeting.proto

2. For java development, can use maven plugin to generate cod during build

```xml
<plugins>
      <plugin>
        <groupId>org.xolstice.maven.plugins</groupId>
        <artifactId>protobuf-maven-plugin</artifactId>
        <version>0.5.0</version>
        <configuration>
          <protocArtifact>com.google.protobuf:protoc:3.3.0:exe:${os.detected.classifier}</protocArtifact>
          <pluginId>grpc-java</pluginId>
          <pluginArtifact>io.vertx:protoc-gen-grpc-java:${vertx.grpc.version}:exe:${os.detected.classifier}</pluginArtifact>
        </configuration>
        <executions>
          <execution>
            <id>compile</id>
            <goals>
              <goal>compile</goal>
              <goal>compile-custom</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
```

### Server class

```java
package io.examples.grpc.greeting;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.grpc.VertxServer;
import io.vertx.grpc.VertxServerBuilder;


public class Server extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(Server.class.getName());
    }

    @Override
    public void start() {
        VertxServer server = VertxServerBuilder.forAddress(vertx, "localhost", 8080)
                .addService(new GreeterGrpc.GreeterVertxImplBase() {
                    @Override
                    public void sayHello(HelloRequest request, Future<HelloReply> future) {
                        System.out.println("Hello " + request.getName());
                        future.complete(HelloReply.newBuilder().setMessage(request.getName()).build());
                    }
                }).build();
        server.start(ar -> {
            if (ar.succeeded()) {
                System.out.println("gRPC service started");
            } else {
                System.out.println("Could not start server " + ar.cause().getMessage());
            }
        });
    }
}
```

### Client class

```java
package io.examples.grpc.greeting;

import io.grpc.ManagedChannel;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.grpc.VertxChannelBuilder;


public class Client extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(Client.class.getName());
    }

    @Override
    public void start() {
        ManagedChannel channel = VertxChannelBuilder
                .forAddress(vertx, "localhost", 8080)
                .usePlaintext(true)
                .build();
        GreeterGrpc.GreeterVertxStub stub = GreeterGrpc.newVertxStub(channel);
        HelloRequest request = HelloRequest.newBuilder().setName("Gary").build();
        stub.sayHello(request, asyncResponse -> {
            if (asyncResponse.succeeded()) {
                System.out.println("Succeeded " + asyncResponse.result().getMessage());
            } else {
                asyncResponse.cause().printStackTrace();
            }
        });
    }
}
```

#### build

>mvn clean install

### Running

1. Run Server in IDE, it shows

```
gRPC service started
```

2. Run Client in IDE, shows output

```
Succeeded Gary
```

and Server outputs

```
Hello Gary
```

That's all for today, you can find the complete source code under [this folder](.).





