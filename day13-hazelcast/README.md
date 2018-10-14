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

