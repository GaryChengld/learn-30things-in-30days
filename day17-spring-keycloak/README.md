# Day 17: Secure Spring-Boot application with Keycloak

Yesterday I installed Keycloak on my windows 10 and created realm "demo-app", and created client "demo-client", user "testuser" in this realm, today I'm going create a spring-boot application which secured by Keycloak througn OAuth2.

## Create spring-boot project

Open [Spring Initializr](https://start.spring.io/) page and setup project structure as below

<img width="880" src="https://user-images.githubusercontent.com/3359299/47128328-c5144980-d25e-11e8-9a2f-44ae91044d95.PNG" />

Click "Create Project" button and download project zip file, extract it to local directory and open the project in Intellij or Eclipse.