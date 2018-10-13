# Day 12: gRPC - A Modern Toolkit for Microservice Communication

Today I'm going to learn a hot new buzz in technology call gRPC.

## What is gRPC

gRPC is a RPC platform developed by Google which was announced and made open source  in late Feb 2015.  The letters “gRPC” are a recursive acronym which means, gRPC Remote Procedure Call.

gRPC has two parts, the gRPC protocol, and the data serialization. By default gRPC utilizes Protocol Buffers for serialization.

## Protocol Buffers

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