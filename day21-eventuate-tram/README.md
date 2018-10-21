# Day 21: Implement Saga pattern with eventuate Tram

Yesterday I learned Saga pattern, today I'm going to create a sample application which implement Saga pattern with Eventuate tram.

I will take the classic trip booking to build this sample.

<img width="800" src="https://cdn-images-1.medium.com/max/800/1*2iJ9L9-PxPU8cT1tRH2VPA.png" />

First take a quick look on eventuate Tram first

## What is eventuate Tram

Based on eventuate.io, the Eventuate Tram framework enables a Java/Spring application to send messages as part of an database transaction. This enables an application atomically update state and send a message or a domain event. It is a foundation of maintaining data consistency within a microservice architecture.

Now I'm starting build this sample application to  demonstrates how to maintain data consistency in an Java/JDBC/JPA-based microservice architecture using sagas.

## Services

To simplify the simple, I will just do book flight only this time, the application contains 2 services

- TripService
- FlightService

It consists of the follow steps:
1. The Trip Service create Trip in a pending state
2. The Trip Service creates a CreateTripSaga to coordinate the creation of the trip.
3. The CreateTripSaga sends a BookFlight command to the Flight Service
4. The Flight Service receives the command and attempts to book flight for that trip. It replies with a message indicating the outcome.
5. The CreateTripSaga receives the reply
6. It send either an ApproveTrip or a RejectTrip command to the TripService

 - Flight Service - implements the REST endpoints for managing flights. The service persists in memory. Using the Eventuate Tram Saga framework, it processes command messages, updates its the Flight entity, and sends back a reply message.
 - Trip Service - implements a REST endpoint for managing trips. The service persists in memory and the CreateTripSaga in memory also. Using the Eventuate Tram Saga framework, it sends command messages and processes replies.
 
 

