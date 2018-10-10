# Day 9: Deploy a java based MicroService to Minikube

Today I'm going to deploy a spring-boot MicroService application to MiniKube

The sample MicorService implements following resources
                        
|Method|Url|Description|
|:---|:---|:---|
|GET|/v1/pet|Get all pets|
|GET|/v1/pet/{id}|Find pet by id|
|GET|/v1/pet/findByCategory/{category}|Find pets by category|
|POST|/v1/pet|Add a new pet|
|PUT|/v1/pet/{id}|Update a pet|
|Delete|/v1/pet/{id}|Delete a pet|

The source is already copied to this directory.

### The Dockerfile to build a Docker image from application

```
FROM openjdk:8-jdk-alpine

ENV SERVICE_APP_FILE target/petstore-0.0.1-SNAPSHOT.jar
# Set the location of the services
ENV SERVICE_HOME /opt/services

EXPOSE 9080
COPY $SERVICE_APP_FILE $SERVICE_HOME/petstore.jar
WORKDIR $SERVICE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar petstore.jar --debug"]
```

#### Build MicroService application

```
mvn clean install
```

#### Test MicroService application locally

Run command

```
mvn spring-boot:run
```

It shows follow messages in command prompt

<img width="880" src="https://user-images.githubusercontent.com/3359299/46710648-d03ff780-cc16-11e8-803f-4fdeb7a0116b.PNG" />

Open http://localhost:9080/v1/pet/ in browser, it displays

<img width="880" src="https://user-images.githubusercontent.com/3359299/46710742-39276f80-cc17-11e8-867e-64af2d625ccc.PNG" />

The jar build is working properly, stop local application by press Ctrl+C

