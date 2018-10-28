# Day 27: Implement CQRS using Axon framework

## What is CQRS

CQRS means Command Query Responsibility Segregation. The main idea behind CQS is: “A method should either change state of an object, or return a result, but not both. In other words, asking the question should not change the answer. More formally, methods should return a value only if they are referentially transparent and hence possess no side effects.” (Wikipedia) Because of this we can divide a methods into two sets:
                                                     
- Commands - change the state of an object or entire system (sometimes called as modifiers or mutators).
- Queries - return results and do not change the state of an object.

<img width="880" src="https://martinfowler.com/bliki/images/cqrs/cqrs.png" />


## Axon framework

Axon is a framework for building evolutionary, event-driven microservice systems, based on the principles of Domain Driven Design, Event Sourcing and CQRS.

For more information, visit [Axon website](https://axoniq.io/).

## CQRS using Axon framework

Now I'm going to create a CQRS implementation using Axon framework, still use petstore as a sample

#### Initialize Spring-boot project
Open [Spring Initializr](https://start.spring.io/) page and setup project structure as below
   
<img width="880" src="https://user-images.githubusercontent.com/3359299/47609970-998c1e80-da17-11e8-940e-26773d9f7e4f.PNG" />
   
Click "Create Project" button and download project zip file, extract it to local directory and open the project in Intellij or Eclipse.

#### Maven dependencies

```xml
		<dependency>
			<groupId>org.axonframework</groupId>
			<artifactId>axon-spring-boot-starter</artifactId>
			<version>3.4</version>
		</dependency>
		<dependency>
			<groupId>org.axonframework</groupId>
			<artifactId>axon-amqp</artifactId>
			<version>3.4</version>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<version>1.18.0</version>
			<scope>provided</scope>
		</dependency>
```

#### Application Configuration
>application.yml

```yaml
server:
  port: 9080

axon:
  amqp:
    exchange: Products

```

#### Query Object
>ProductQueryObject.java
```java
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductQueryObject {
    @Id
    private String productId;
    private String name;
    private String category;
}
```

#### Event
>ProductCreateEvent.java
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateEvent {
    private String id;
    private String name;
    private String category;
}

```
#### Repository
>ProductQueryObjectRepository.java
```java
public interface ProductQueryObjectRepository extends JpaRepository<ProductQueryObject, String> {
}
```

#### QueryObjectUpdater
>ProductQueryObjectUpdater.java
```java
@Component
public class ProductQueryObjectUpdater {
    @Autowired
    private ProductQueryObjectRepository productQueryObjectRepository;

    @EventHandler
    public void on(ProductCreateEvent event) {
        productQueryObjectRepository.save(new ProductQueryObject(event.getId(), event.getName(), event.getCategory()));
    }
}
```

#### Command
>CreateProductCommand.java
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductCommand {
    private String id;
    private String name;
    private String category;
}
```

#### controller
>
```java
@Slf4j
@RestController
@RequestMapping(value = "/v1/pet", produces = MediaType.APPLICATION_JSON_VALUE)
public class PetController {
    private static final ResponseEntity<ApiResponse> RESP_PET_NOT_FOUND
            = new ResponseEntity<>(ApiResponses.ERR_PET_NOT_FOUND, HttpStatus.NOT_FOUND);

    @Autowired
    private CommandGateway commandGateway;
    @Autowired
    private ProductQueryObjectRepository productQueryObjectRepository;

    @GetMapping
    public List<ProductQueryObject> findAll() {
        return productQueryObjectRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> find(@PathVariable String id) {
        return productQueryObjectRepository
                .findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(RESP_PET_NOT_FOUND);
    }

    @PostMapping
    public CompletableFuture<String> createProduct(@RequestBody Map<String, String> request) {
        String id = UUID.randomUUID().toString();
        return commandGateway.send(new CreateProductCommand(id, request.get("name"), request.get("category")));
    }
}
```

#### Run applicaiton

start application, and test it using Postman

**Create a new pet**
- url: http://localhost:9080/v1/pet/
- Method: POST
- Body:
```json
{
	"name": "Angelfish",
	"category": "Fish"
}
```

**Find all**
- url: http://localhost:9080/v1/pet/
- Method: GET
- Response:
```json
[
    {
        "productId": "ba8e54ad-04d5-4116-9eae-0f77f7033e70",
        "name": "Angelfish",
        "category": "Fish"
    }
]
```

**Find by ID**
- url: http://localhost:9080/v1/pet/ba8e54ad-04d5-4116-9eae-0f77f7033e70
- Method: GET
- Response:
```json
{
    "productId": "ba8e54ad-04d5-4116-9eae-0f77f7033e70",
    "name": "Angelfish",
    "category": "Fish"
}
```

#### All events are sent to RabbitMQ "Products" queue

<img width="880" src="https://user-images.githubusercontent.com/3359299/47612145-9d856400-da4a-11e8-9c74-e738409e6c14.PNG" />

 That's all for today, you can find the complete source code under [this folder](apps/cqrs-petstore).  


 
