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

 - Flight Service - implements the REST endpoints for managing flights. The service persists in H2 database. Using the Eventuate Tram Saga framework, it processes command messages, updates its the Flight entity, and sends back a reply message.
 - Trip Service - implements a REST endpoint for managing trips. The service persists in H2 database and the CreateTripSaga in H2 database also. Using the Eventuate Tram Saga framework, it sends command messages and processes replies.
 
## TripService

**Create Spring-boot TripService project** 

Open [Spring Initializr](https://start.spring.io/) page and setup project structure as below

<img width="880" src="https://user-images.githubusercontent.com/3359299/47271789-a4900c00-d54a-11e8-9fa0-230810707fc9.PNG" />

Click "Create Project" button and download project zip file, extract it to local directory and open the project in Intellij or Eclipse.

**Add following dependencies to maven pom.xml**

```xml
		<dependency>
			<groupId>io.eventuate.tram.core</groupId>
			<artifactId>eventuate-tram-jdbc-kafka</artifactId>
			<version>${eventuatectram.version}</version>
		</dependency>
		<dependency>
			<groupId>io.eventuate.tram.sagas</groupId>
			<artifactId>eventuate-jpa-sagas-framework</artifactId>
			<version>${eventuate.tram.sagas.version}</version>
		</dependency>
		<dependency>
			<groupId>io.eventuate.tram.sagas</groupId>
			<artifactId>eventuate-tram-sagas-simple-dsl</artifactId>
			<version>${eventuate.tram.sagas.version}</version>
		</dependency>
```

**Create application properties file**

```yaml
server:
  port: 9080
spring:
  application:
    name:
      trip-service
  jpa:
    generate-ddl: true

logging:
  level:
    org.springframework.orm.jpa: INFO
    org.hibernate.SQL: DEBUG
    io.eventuate: DEBUG
    io.examples: DEBUG
```

**Spring Application class**

```java
@SpringBootApplication
@Configuration
@Import({TramEventsPublisherConfiguration.class,
        TramCommandProducerConfiguration.class,
        SagaOrchestratorConfiguration.class,
        TramJdbcKafkaConfiguration.class,
        SagaParticipantConfiguration.class})
public class TripApplication {

    public static void main(String[] args) {
        SpringApplication.run(TripApplication.class, args);
    }

    @Bean
    public ChannelMapping channelMapping() {
        return DefaultChannelMapping.builder().build();
    }
}
```

**BookingState enum**
```java
public enum BookingState {
      PENDING, APPROVED, REJECTED
  }
```

**Trip.java domain**

```java
@Entity
@Table(name = "trips")
@Access(AccessType.FIELD)
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BookingState bookingState;
    @Embedded
    private TripDetail tripDetail;

    public Trip() {
    }

    public Trip(TripDetail tripDetail) {
        this.tripDetail = tripDetail;
        this.bookingState = BookingState.PENDING;
    }

    public static ResultWithEvents<Trip> createTrip(TripDetail orderDetails) {
        return new ResultWithEvents<>(new Trip(orderDetails), Collections.emptyList());
    }

    public void flightBooked() {
        this.bookingState = BookingState.APPROVED;
    }

    public void noteBookFlightFailed() {
        this.bookingState = BookingState.REJECTED;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookingState getBookingState() {
        return bookingState;
    }

    public void setBookingState(BookingState bookingState) {
        this.bookingState = bookingState;
    }

    public TripDetail getTripDetail() {
        return tripDetail;
    }

    public void setTripDetail(TripDetail tripDetail) {
        this.tripDetail = tripDetail;
    }
}
```

**TripRepository.java**

```java
public interface TripRepository extends CrudRepository<Trip, Long> {
}
```

**Saga Class**

```java
public class CreateTripSaga implements SimpleSaga<CreateTripSagaData> {
    private SagaDefinition<CreateTripSagaData> sagaDefinition =
            step()
                    .withCompensation(this::reject)
                    .step()
                    .invokeParticipant(this::bookFlight)
                    .step()
                    .invokeParticipant(this::approve)
                    .build();


    @Override
    public SagaDefinition<CreateTripSagaData> getSagaDefinition() {
        return this.sagaDefinition;
    }


    private CommandWithDestination bookFlight(CreateTripSagaData data) {
        long tripId = data.getTripId();
        return send(new BookFlightCommand(tripId, data.getTripDetail().getTripDate(), data.getTripDetail().getFromCity(), data.getTripDetail().getToCity()))
                .to("flightService")
                .build();
    }

    public CommandWithDestination reject(CreateTripSagaData data) {
        return send(new RejectTripCommand(data.getTripId()))
                .to("orderService")
                .build();
    }

    private CommandWithDestination approve(CreateTripSagaData data) {
        return send(new ApproveTripCommand(data.getTripId()))
                .to("orderService")
                .build();
    }
}
```

**TripService class**

```java
@Service
public class TripService {
    @Autowired
    private SagaManager<CreateTripSagaData> createTripSagaManager;

    @Autowired
    private TripRepository tripRepository;

    @Transactional
    public Trip createOrder(TripDetail tripDetail) {
        ResultWithEvents<Trip> resultWithEvents = Trip.createTrip(tripDetail);
        Trip trip = resultWithEvents.result;
        tripRepository.save(trip);
        CreateTripSagaData data = new CreateTripSagaData(trip.getId(), tripDetail);
        createTripSagaManager.create(data, Trip.class, trip.getId());
        return trip;
    }
}
```

**TripCommandHandler**

```java
public class TripCommandHandler {
    @Autowired
    private TripRepository tripRepository;

    public CommandHandlers commandHandlerDefinitions() {
        return SagaCommandHandlersBuilder
                .fromChannel("orderService")
                .onMessage(ApproveTripCommand.class, this::approve)
                .onMessage(RejectTripCommand.class, this::reject)
                .build();
    }

    public Message approve(CommandMessage<ApproveTripCommand> cm) {
        long tripId = cm.getCommand().getTripId();
        Trip trip = tripRepository.findById(tripId).get();
        trip.flightBooked();
        return withSuccess();
    }

    public Message reject(CommandMessage<RejectTripCommand> cm) {
        long tripId = cm.getCommand().getTripId();
        Trip trip = tripRepository.findById(tripId).get();
        trip.noteBookFlightFailed();
        return withSuccess();
    }
}
```

## Flight Service

Similar to Trip service, create Flight service project in Spring Initializr.

**Application properties**

```yaml
server:
  port: 9081
spring:
  application:
    name:
      flight-service
  jpa:
    generate-ddl: true
  h2:
    console:
      enabled: true

logging:
  level:
    org.springframework.orm.jpa: INFO
    org.hibernate.SQL: DEBUG
    io.eventuate: DEBUG
    io.examples: DEBUG
```

**Flight Domain**

```java
@Entity
@Table(name = "flights")
@Access(AccessType.FIELD)
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String flightNo;
    private String flightDate;
    private String from;
    private String to;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
```    

**Repository**
```java
public interface FlightRepository extends CrudRepository<Flight, Long> {
    List<Flight> findByFromAndTo(String from, String to);
}
```

**Service**
```java
@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    public Flight createFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public Flight findFlightByFromAndTo(String from, String to) {
        List<Flight> flights = flightRepository.findByFromAndTo(from, to);
        if (flights.size() > 0) {
            return flights.get(0);
        } else {
            return null;
        }
    }
}
```

**FlightCommandHandler**

```java
@Component
public class FlightCommandHandler {
    @Autowired
    private FlightService flightService;

    public CommandHandlers commandHandlerDefinitions() {
        return SagaCommandHandlersBuilder
                .fromChannel("flightService")
                .onMessage(BookFlightCommand.class, this::reserveCredit)
                .build();
    }

    public Message reserveCredit(CommandMessage<BookFlightCommand> cm) {
        BookFlightCommand cmd = cm.getCommand();
        Flight flight = flightService.findFlightByFromAndTo(cmd.getFromCity(), cmd.getToCity());
        if (flight != null) {
            return withSuccess(new FlightBooked());
        } else {
            return withFailure(new FlightBookFailed());
        }
    }
}
```

That's all for today, you can find the complete source code under [this folder](.).










