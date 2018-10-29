# Day 28: Using consul as Service Discovery in Spring Cloud

Service Discovery is one of the key thing in microservice-based architecture, Netflix Eureka is a default Service discovery solution in Spring cloud, some how Spring Cloud also integrate with other service discovery tools.

Today I'm going to use Consul as Service Discovery in spring cloud application

## Consul

[Consul](https://www.consul.io/) is a service mesh solution providing a full featured control plane with service discovery, configuration, and segmentation functionality.

The key features of Consul are:
- Service Discovery
- Health Checking
- key/value Store
- Secure Service Communication
- Support multiple data center

### Download and install Consul

Go to Consul [download page](https://www.consul.io/downloads.html), download windows package, and unzip it in local directory c:\development\consul

### Start consul

- Open a command prompt
- Go to consul directory
- Execute command
>consul agent -dev -bind=127.0.0.1 -data-dir=\tmp\consul -server -ui
in command prompt, it displays
```
C:\Development\consul>consul agent -dev -bind=127.0.0.1 -data-dir=\tmp\consul -server -ui
==> Starting Consul agent...
==> Consul agent running!
           Version: 'v1.3.0'
           Node ID: '22649b87-cee1-c0e1-eb58-898fe81570d7'
         Node name: 'Gary-PC'
        Datacenter: 'dc1' (Segment: '<all>')
            Server: true (Bootstrap: false)
       Client Addr: [127.0.0.1] (HTTP: 8500, HTTPS: -1, gRPC: 8502, DNS: 8600)
      Cluster Addr: 127.0.0.1 (LAN: 8301, WAN: 8302)
           Encrypt: Gossip: false, TLS-Outgoing: false, TLS-Incoming: false

==> Log data will now stream in as it occurs:

    2018/10/28 20:04:45 [INFO] raft: Initial configuration (index=1): [{Suffrage:Voter ID:22649b87-cee1-c0e1-eb58-898fe81570d7 Address:127.0.0.1:8300}]
    2018/10/28 20:04:45 [INFO] raft: Node at 127.0.0.1:8300 [Follower] entering Follower state (Leader: "")
    2018/10/28 20:04:45 [INFO] serf: EventMemberJoin: Gary-PC.dc1 127.0.0.1
    2018/10/28 20:04:45 [INFO] serf: EventMemberJoin: Gary-PC 127.0.0.1
    2018/10/28 20:04:45 [INFO] consul: Adding LAN server Gary-PC (Addr: tcp/127.0.0.1:8300) (DC: dc1)
    2018/10/28 20:04:45 [INFO] consul: Handled member-join event for server "Gary-PC.dc1" in area "wan"
    2018/10/28 20:04:45 [DEBUG] agent/proxy: managed Connect proxy manager started
    2018/10/28 20:04:45 [INFO] agent: Started DNS server 127.0.0.1:8600 (udp)
    2018/10/28 20:04:45 [INFO] agent: Started DNS server 127.0.0.1:8600 (tcp)
    2018/10/28 20:04:45 [INFO] agent: Started HTTP server on 127.0.0.1:8500 (tcp)
    2018/10/28 20:04:45 [INFO] agent: Started gRPC server on 127.0.0.1:8502 (tcp)
    2018/10/28 20:04:45 [INFO] agent: started state syncer
    2018/10/28 20:04:45 [WARN] raft: Heartbeat timeout from "" reached, starting election
    2018/10/28 20:04:45 [INFO] raft: Node at 127.0.0.1:8300 [Candidate] entering Candidate state in term 2
    2018/10/28 20:04:45 [DEBUG] raft: Votes needed: 1
    2018/10/28 20:04:45 [DEBUG] raft: Vote granted from 22649b87-cee1-c0e1-eb58-898fe81570d7 in term 2. Tally: 1
    2018/10/28 20:04:45 [INFO] raft: Election won. Tally: 1
    2018/10/28 20:04:45 [INFO] raft: Node at 127.0.0.1:8300 [Leader] entering Leader state
    2018/10/28 20:04:45 [INFO] consul: cluster leadership acquired
    2018/10/28 20:04:45 [INFO] consul: New leader elected: Gary-PC
    2018/10/28 20:04:45 [INFO] agent: Synced node info
    2018/10/28 20:04:45 [DEBUG] agent: Node info in sync
    2018/10/28 20:04:45 [INFO] connect: initialized CA with provider "consul"
    2018/10/28 20:04:45 [DEBUG] consul: Skipping self join check for "Gary-PC" since the cluster is too small
    2018/10/28 20:04:45 [INFO] consul: member 'Gary-PC' joined, marking health alive
    2018/10/28 20:04:47 [DEBUG] agent: Skipping remote check "serfHealth" since it is managed automatically
    2018/10/28 20:04:47 [DEBUG] agent: Node info in sync
```

The http admin UI is start on port 8500, open URL http://localhost:8500 in browser, will see following page

<img width="880" src="https://user-images.githubusercontent.com/3359299/47623988-88addc80-daed-11e8-8454-8e9c9eb790f3.PNG" />

## Build spring cloud applications to use Consul

This example will create 2 spring cloud application, service1 and service2, service1 get service2 endpoint by consul and call this endpoint to build service1 result.

### Create service1

**Maven dependencies**
>pom.xml
```xml
  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-consul-discovery</artifactId>
    </dependency>
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <version>1.18.0</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>io.projectreactor</groupId>
      <artifactId>reactor-test</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>
```

**Application configure**
>bootstrap.yml
```yaml
spring:
  application:
    name: service1
```    

>application.yaml
```yaml
server:
  port: 9081

spring:
  cloud:
    consul:
      host: localhost
      port: 8500

management:
  endpoints:
    web:
      exposure:
        include: "health"
```        



