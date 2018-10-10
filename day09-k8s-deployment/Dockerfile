FROM openjdk:8-jdk-alpine

ENV SERVICE_APP_FILE target/petstore-0.0.1-SNAPSHOT.jar

# Set the location of the services
ENV SERVICE_HOME /opt/services

EXPOSE 9080

COPY $SERVICE_APP_FILE $SERVICE_HOME/petstore.jar

WORKDIR $SERVICE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar petstore.jar --debug"]
