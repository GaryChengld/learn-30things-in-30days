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

### Create service2

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
    name: service2
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

**Service result**
>Service2Result.java
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Service2Result {
    private String name;
    private String message;
}
```

**Controller**
>
```java
@RequestMapping("/v1/service2")
@RestController
public class Service2Controller {

    @GetMapping
    public Mono<Service2Result> service2() {
        return Mono.just(new Service2Result("service2", "Welcome to service2"));
    }
}
```

**Application class**
>Service2Application.java
```java
package io.example.consul.service2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Service2Application {

	public static void main(String[] args) {
		SpringApplication.run(Service2Application.class, args);
	}
}
```

**Run Service2**
Start application by command
>mvn spring-boot:run

Open http://localhost:9081/v1/service2 in browser, it shows
```json
{
    "name": "service2",
    "message": "Welcome to service2"
}
```

And service2 is registered in Consul

- Services page
<img width="880" src="https://user-images.githubusercontent.com/3359299/47625103-599d6800-daf9-11e8-9d1c-272456d9ee25.PNG" />

- Detail page
<img width="880" src="https://user-images.githubusercontent.com/3359299/47625104-5ace9500-daf9-11e8-83b4-ba39d4c0e227.PNG" />

- Health check page
<img width="880" src="https://user-images.githubusercontent.com/3359299/47625288-9cac0b00-dafa-11e8-8da0-6cfc3558194b.PNG" />

### Create service1

Service1 will get service2 endpoint from consul by service2's name

The maven dependencies are the same as service2

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
  port: 9080

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

**Service result**
>Service1Result.java
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Service1Result {
    private String name;
    private String message;
    @JsonProperty("service2 result")
    private Service2Result service2Result;
}

```
>Service2Result.java
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Service2Result {
    private String name;
    private String message;
}
```

**Controller**
>Service1Controller.java
```java
@RequestMapping("/v1/service1")
@RestController
@Slf4j
public class Service1Controller {
    private String SERVICE2_URL = "http://service2/v1/service2";

    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping
    public Mono<Service1Result> service2() {
        log.info("Received service1 request");
        return this.rxBuildData();
    }

    private Mono<Service1Result> rxBuildData() {
        Service1Result service1Result = new Service1Result();
        service1Result.setName("service1");
        service1Result.setMessage("Welcome to service1");
        log.info("Sending service request");
        return Mono.create(emitter ->
                webClientBuilder.build().get().uri(SERVICE2_URL).exchange()
                        .flatMap(res -> res.bodyToMono(Service2Result.class))
                        .doOnError(ex -> {
                            log.error(ex.getLocalizedMessage());
                            service1Result.setService2Result(new Service2Result("service2", ex.getLocalizedMessage()));
                            emitter.success(service1Result);
                        })
                        .subscribe(serviceData -> {
                            log.info("Received service data:{}", serviceData.toString());
                            service1Result.setService2Result(serviceData);
                            emitter.success(service1Result);
                        })
        );
    }
}
```

**Application**
>Service1Application.java
```java
package io.example.consul.service1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Service1Application {

    public static void main(String[] args) {
        SpringApplication.run(Service1Application.class, args);
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
```

**Run service1**

Keep service2 running, and open a command prompt, run command
>mvn spring-boot:run

Open http://localhost:9080/v1/service1 in browser, it shows
```json
{
    "name": "service1",
    "message": "Welcome to service1",
    "service2 result": {
        "name": "service2",
        "message": "Welcome to service2"
    }
}
```

Stop service2, and open same url in browser, it shows

```json
{
    "name": "service1",
    "message": "Welcome to service1",
    "service2 result": {
        "name": "service2",
        "message": "Connection refused: no further information: localhost/127.0.0.1:9081"
    }
}
```

 That's all for today, you can find the complete source code under [this folder](examples).  


