# Day 16: Secure Spring-boot application with Keycloak

## What is Keycloak

Keycloak is an open source software product to allow single sign-on with Identity Management and Access Management aimed at modern applications and services.

## Download and install Keycloak server on windows

 - Open browser and goto Keycloak [download page](https://www.keycloak.org/downloads.html), download Standalone server distribution ZIP package.
 - Unzip the ZIP package to local directory
 - Goto installation directory, double click bin/standalone.bat, the Keycloak will be started and show following messages
 
 <img width="880" src="https://user-images.githubusercontent.com/3359299/47057131-69c65680-d18d-11e8-9fc6-6f956984277a.PNG" />
 
 - Open url http://localhost:8080 in browser, it's the first time run Keyclock, it shows admin console and ask to create an initial admin user
 
 <img width="880" src="https://user-images.githubusercontent.com/3359299/47057285-f113ca00-d18d-11e8-8477-d59dc6aed772.PNG" />
 
 Type in Admin username and password, then click "Create" button, once the user created, the browser shows
 
 <img width="880" src="https://user-images.githubusercontent.com/3359299/47057551-3d133e80-d18f-11e8-8e35-6069384adad2.PNG" />
 
 ## Creating a new Realm
 
 Keycloak defines the concept of a realm in which you will define your clients, which in Keycloak terminology means an application that will be secured by Keycloak, it can be a Web App, a Java EE backend, a Spring Boot etc.
 
 Login to Keycloak use initial admin, it show following screen
 
 <img width="880" src="https://user-images.githubusercontent.com/3359299/47058390-dbed6a00-d192-11e8-8fcf-988471025b35.PNG" />
 
 - Move the mouse under "Master", it will show a "Add realm" button, Click it, go to Add Realm screen
 
 <img width="880" src="https://user-images.githubusercontent.com/3359299/47058494-4d2d1d00-d193-11e8-9103-6ebff8c01adf.PNG" />
 
 - Enter realm name "demo-app" and click "Create" button, now it goes to realm setup page for new realm "Demo-app"
 
## Creating the client, the role, and the user

Now we need to define a client, which will be our Spring Boot app. Go to the “Clients” section and click the “create” button. We will call our client “demo-client”: 

<img width="880" src="https://user-images.githubusercontent.com/3359299/47058891-0e986200-d195-11e8-87da-0fd3062b0eb9.PNG" />

In next screen (Client setting screen), enter a valid redirect URL that Keycloak will use once the user is authenticated, Put as value: “http://localhost:9080/*”, then click save

<img width="880" src="https://user-images.githubusercontent.com/3359299/47059204-5ff52100-d196-11e8-9f7c-cf6f1e91c65e.PNG" />
 
 
 
 



