# Day 2: Reactive programming on Spring-boot and MangoDB

### Reactive programming
Reactive programming is programming with asynchronous data streams.

### Reactive Systems are:
 - Responsive
 - Resilient
 - Elastic
 - Message Driven
 
Today I will learn Spring-boot 2 reactive programming against MongoDB.

## Initialize Spring-boot project
Open [Spring Initializr](https://start.spring.io/) page and setup project structure as below

<img width="880" src="https://user-images.githubusercontent.com/3359299/46385792-e46e7c80-c68b-11e8-9480-f2513922b426.PNG"/>

Client create project button and download project zip file, extract it to local directory and open the project in Intellij or Eclipse

This sample application will create a RESTful service implements resource as below and store data in MongoDB

|Method|Url|Description|
|:---|:---|:---|
|GET|/v1/pet|Get all pets|
|GET|/v1/pet/{id}|Find pet by id|
|GET|/v1/pet/findByCategory/{category}|Find pets by category|
|POST|/v1/pet|Add a new pet|
|PUT|/v1/pet/{id}|Update a pet|
|Delete|/v1/pet/{id}|Delete a pet|

####Spring-boot main application
```java
@SpringBootApplication
@EnableReactiveMongoRepositories
@EnableMongoAuditing
public class ReactiveMongoApp {
    public static void main(String[] args) {
        SpringApplication.run(ReactiveMongoApp.class, args);
    }
}
```
We added @EnableReactiveMongoRepositories for configuring MongoDb and Mongo Reactive Repository and we’re using @SpringBootApplication as our primary application configuration class.

####Application configuration applicaiton.yml
```yaml
server:
  port: 9080

spring:
  data:
    mongodb:
      database: storedb
      host: localhost
      port: 27017
```      

