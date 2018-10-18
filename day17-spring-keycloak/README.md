# Day 17: Secure Spring-Boot application with Keycloak

Yesterday I installed Keycloak on my windows 10 and created realm "demo-app", and created client "demo-client", user "testuser" in this realm, today I'm going create a spring-boot application which secured by Keycloak througn OAuth2.

## Create spring-boot project

Open [Spring Initializr](https://start.spring.io/) page and setup project structure as below

<img width="880" src="https://user-images.githubusercontent.com/3359299/47128328-c5144980-d25e-11e8-9a2f-44ae91044d95.PNG" />

Click "Create Project" button and download project zip file, extract it to local directory and open the project in Intellij or Eclipse.

## Defining Keycloak’s configuration

Add below Keycloak configuration in applicaiton.properties file based on local Keycloak setting
```
keycloak.auth-server-url=http://localhost:8080/auth
keycloak.realm=demo-app
keycloak.resource=demo-client
keycloak.public-client=true
```

Then define some Security constraints in same file

```
keycloak.security-constraints[0].authRoles[0]=user
keycloak.security-constraints[0].securityCollections[0].patterns[0]=/movies/*
```