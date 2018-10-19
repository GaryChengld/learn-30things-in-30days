# Day 18: Tracing spring-boot Microservices with Zipkin

Today I'm going to understand the microservices distributed tracing using Zipkin and Spring cloud sleuth framework.

## What is Zipkin

[Zipkin](https://zipkin.io/) is a distributed tracing system. It helps gather timing data needed to troubleshoot latency problems in microservice architectures. It manages both the collection and lookup of this data.

Internally it has 4 modules –

1. Collector – Once any component sends the trace data arrives to Zipkin collector daemon, it is validated, stored, and indexed for lookups by the Zipkin collector.
2. Storage – This module store and index the lookup data in backend. Cassandra, ElasticSearch and MySQL are supported.
3. Search – This module provides a simple JSON API for finding and retrieving traces stored in backend. The primary consumer of this API is the Web UI.
4. Web UI – A very nice UI interface for viewing traces. 

## Install Ziokin

For windows installation, just download the latest Zipkin server from [maven repository](https://search.maven.org/remote_content?g=io.zipkin.java&a=zipkin-server&v=LATEST&c=exec) and run the executable jar file using below command
>java -jar zipkin-server-2.11.7-exec.jar

Once Zipkin is started, it shows below in the command prompt

<img width="880" src="https://user-images.githubusercontent.com/3359299/47195773-623cb400-d32b-11e8-980f-de2e7c1303e4.PNG" />

And Web UI page can be displayed at http://localhost:9411/zipkin/.

<img width="880" src="https://user-images.githubusercontent.com/3359299/47195993-79c86c80-d32c-11e8-8f74-c1ef533f584d.PNG" />

## Sleuth

Sleuth is a tool from Spring cloud family. It is used to generate the trace id, span id and add these information to the service calls in the headers and MDC, so that It can be used by tools like Zipkin and ELK etc. 

## Zipkin and Sleuth Integration


Create project service1

**pom.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>io.examples</groupId>
  <artifactId>zipkin-service-1</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>zipkin-service-1</name>
  <description>Demo project for Spring Boot</description>

  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>1.5.6.RELEASE</version>
    <relativePath/> <!-- lookup parent from repository -->
  </parent>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <java.version>1.8</java.version>
    <spring-cloud.version>Dalston.SR3</spring-cloud.version>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-sleuth</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-zipkin</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>ch.qos.logback</groupId>
      <artifactId>logback-classic</artifactId>
      <version>1.2.3</version>
      <scope>compile</scope>
    </dependency>
  </dependencies>

  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-dependencies</artifactId>
        <version>${spring-cloud.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>

  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </build>
</project>
```

**Application properties**
application.yml

```yaml
server:
  port: 9080
spring:
  application:
    name:
      zipkin-service1
```

**Application class**

```java
package io.examples.zipkin.service1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ZipkinService1Application {

    public static void main(String[] args) {
        SpringApplication.run(ZipkinService1Application.class, args);
    }
}

@RestController
@RequestMapping(value = "/v1/", produces = MediaType.APPLICATION_JSON_VALUE)
class ZipkinController {
    private static final Logger logger = LoggerFactory.getLogger(ZipkinController.class);

    @Bean
    public AlwaysSampler alwaysSampler() {
        return new AlwaysSampler();
    }


    @GetMapping(value = "/service1")
    @ResponseBody
    public Result1 service1() {
        logger.info("Inside zipkin Service 1..");
        Result1 result1 = new Result1();
        result1.setMessage("service1");
        return result1;
    }
}
```

Start spring-boot application

in browser, goto http://localhost:9080/v1/service1, it shows
```json
{"message":"service1"}
```

Refresh several times

Then goto Zipkin UI page, select service name "zipkin-service1" and click trace, it shows request logs of this service.

<img width="880" src="https://user-images.githubusercontent.com/3359299/47197749-23602b80-d336-11e8-8ba4-68bf725bc59f.PNG" />

Click a request, will show the request

<img width="880" src="https://user-images.githubusercontent.com/3359299/47197751-26f3b280-d336-11e8-8a09-0ca53cfdc959.PNG" />



