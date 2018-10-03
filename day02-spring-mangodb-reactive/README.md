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

#### Spring-boot main application
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

#### Application configuration applicaiton.yml
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

#### Create Product entity
```java
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "product")
public class Product {
    @EqualsAndHashCode.Include
    @Id
    private String id;
    private String name;
    @TextIndexed
    private String category;
}
```

#### Create Product repository interface
```java
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

    /**
     * Find products by category
     *
     * @param category
     * @return
     */
    Flux<Product> findByCategory(String category);

    /**
     * Find product by id
     *
     * @param category
     * @return
     */
    Mono<Product> findById(String category);
}
```

#### Repository with MongoDB
The repository, making use of Spring data mongo reactive we are extending our interface which annotated @Repository with ReactiveMongoRepository 
```java
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
    /**
     * Find products by category
     *
     * @param category
     * @return
     */
    Flux<Product> findByCategory(String category);

    /**
     * Find product by id
     *
     * @param category
     * @return
     */
    Mono<Product> findById(String category);
}
```

#### Service class
Now we will create service which will be consumed by the web controller

Service interface
```java
public interface ProductService {
    /**
     * Return all proucts
     *
     * @return
     */
    Flux<Product> getProducts();

    /**
     * Find products by category
     *
     * @param category
     * @return
     */
    Flux<Product> getProductsByCategory(String category);

    /**
     * Find product by id
     *
     * @param id
     * @return
     */
    Mono<Product> getProductById(String id);

    /**
     * Create a new Product
     *
     * @param product
     * @return
     */
    Mono<Product> addProduct(Product product);

    /**
     * Update a product
     *
     * @param product
     * @return
     */
    Mono<Product> updateProduct(String id, Product product);

    /**
     * Delete a product by id
     *
     * @param id
     * @return
     */
    Mono<Boolean> deleteProduct(String id);
}
```
Service class
```java
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Flux<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public Flux<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public Mono<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    @Override
    public Mono<Product> addProduct(Product product) {
        return productRepository.insert(product);
    }

    @Override
    public Mono<Product> updateProduct(String id, Product product) {
        return this.getProductById(id)
                .doOnSuccess(p -> {
                    p.setName(product.getName());
                    p.setCategory(product.getCategory());
                    productRepository.save(p).subscribe();
                });
    }

    @Override
    public Mono<Boolean> deleteProduct(String id) {
        return this.getProductById(id)
                .doOnSuccess(p -> productRepository.delete(p).subscribe())
                .flatMap(p -> Mono.just(Boolean.TRUE));
    }
}
```

#### Restful web Controller
```java
@RestController
@RequestMapping(value = "/v1/pet", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PetController {
    private static final ResponseEntity<ApiResponse> RESP_PET_NOT_FOUND
            = new ResponseEntity<>(ApiResponses.ERR_PET_NOT_FOUND, HttpStatus.NOT_FOUND);

    @Autowired
    private ProductService productService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Flux<Product> all() {
        return productService.getProducts();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Mono<ResponseEntity<?>> byId(@PathVariable("id") String id) {
        return productService.getProductById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .defaultIfEmpty(RESP_PET_NOT_FOUND);
    }

    @RequestMapping(value = "/findByCategory/{category}", method = RequestMethod.GET)
    @ResponseBody
    public Flux<Product> byCategory(@PathVariable("category") String category) {
        return productService.getProductsByCategory(category);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Mono<Product> add(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Mono<ApiResponse> update(@PathVariable("id") String id, @RequestBody Product product) {
        return productService.updateProduct(id, product)
                .map(p -> ApiResponses.MSG_UPDATE_SUCCESS)
                .defaultIfEmpty(ApiResponses.ERR_PET_NOT_FOUND);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Mono<ApiResponse> delete(@PathVariable("id") String id) {
        return productService.deleteProduct(id)
                .map(deleted -> ApiResponses.MSG_DELETE_SUCCESS)
                .defaultIfEmpty(ApiResponses.ERR_PET_NOT_FOUND);
    }
}
```

### Build
```
mvn clean install
```

### Start
start MongoDB first, go to MongoDB installation directory
```
cd bin
mongod --dbpath \var\mongodb\data --logpath \var\mongodb\log\mongod.log
```

Open another command prompt and go to example application folder
```
mvn spring-boot:run
```

You will see below messages in the command prompt

<img width="880" src="https://user-images.githubusercontent.com/3359299/46388665-a37e6400-c69b-11e8-845e-51411102db5d.PNG" />

Now the example applicaiton is started on port 9080 and connected to MongoDB.

You can find the complete example under [this folder](src).

