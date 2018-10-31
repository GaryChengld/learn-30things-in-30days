# Day 30: Helidon - a new open source Java microservices framework

Today is the last day of my 30 days challenge, I'm going to learn Helidon- a new Java microservice framework that has been open sourced recently by Oracle.

## Helidon

Helidon is a collection of Java libraries for writing microservices that run on a fast web core powered by Netty.

Helidon supports two programming models

- Helidon SE: a functional programming style that uses the Helidon WebServer, Config and Security APIs directly. This gives you full transparency and control.
- Helidon MP: a more declarative model that supports the MicroProfile family of APIs. This will be familiar to Java EE developers.

**Architecture**

<img width="600" src="https://cdn-images-1.medium.com/max/800/0*LnuwXH0ikIKQ7eoB" />

- Helidon SE components are marked green. These are Config, Security and RxServer.
- The Java EE/Jakarta EE components we use are marked grey. These are JSON-P, JAX-RS/Jersey and CDI. These components are required for MicroProfile implementation.
- Helidon MP is a thin layer on top of Helidon SE components.
- Optional Oracle Cloud services components are marked red and used in both Helidon SE and Helidon MP.

The sample MicroService use MicroProfile model and implements following resources
                        
|Method|Url|Description|
|:---|:---|:---|
|GET|/v1/pet|Get all pets|
|GET|/v1/pet/{id}|Find pet by id|
|GET|/v1/pet/findByCategory/{category}|Find pets by category|
|POST|/v1/pet|Add a new pet|
|PUT|/v1/pet/{id}|Update a pet|
|Delete|/v1/pet/{id}|Delete a pet|

## Generate The Project

Generate the project sources using Helidon Maven archetypes
>mvn archetype:generate -DinteractiveMode=false \
     -DarchetypeGroupId=io.helidon.archetypes \
     -DarchetypeArtifactId=helidon-quickstart-mp \
     -DarchetypeVersion=0.10.4 \
     -DgroupId=io.examples.helidon \
     -DartifactId=petstore \
     -Dpackage=io.examples.helidon.petstore

Import the project by intellij or eclipse

## Add maven dependency
>pom.xml
```xml
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.0</version>
            <scope>provided</scope>
        </dependency>
```

## Applicaiton config
>microprofile-config.properties
```
# Microprofile server properties
server.port=9080
server.host=0.0.0.0
```

## Main class
>Main.java
```java
package io.examples.helidon.petstore;

import java.io.IOException;
import java.util.logging.LogManager;

import io.helidon.microprofile.server.Server;

/**
 * Main method simulating trigger of main method of the server.
 */
public final class Main {

    /**
     * Cannot be instantiated.
     */
    private Main() { }

    /**
     * Application main entry point.
     * @param args command line arguments
     * @throws IOException if there are problems reading logging properties
     */
    public static void main(final String[] args) throws IOException {
        startServer();
    }

    /**
     * Start the server.
     * @return the created {@link Server} instance
     * @throws IOException if there are problems reading logging properties
     */
    protected static Server startServer() throws IOException {

        // load logging configuration
        LogManager.getLogManager().readConfiguration(
                Main.class.getResourceAsStream("/logging.properties"));


        // Server will automatically pick up configuration from
        // microprofile-config.properties
        Server server = Server.create();
        server.start();
        return server;
    }
}
```

## Resource lass
>PetResource.java
```java
package io.examples.helidon.petstore;

import io.examples.helidon.petstore.common.ApiResponse;
import io.examples.helidon.petstore.common.ApiResponses;
import io.examples.helidon.petstore.entity.Product;
import io.examples.helidon.petstore.repository.ProductRepository;
import io.examples.helidon.petstore.repository.impl.InMemoryProductRepository;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

/**
 * @author Gary Cheng
 */
@Path("/v1/pet")
@RequestScoped
@Slf4j
public class PetResource {
    private static final Response RESP_PET_NOT_FOUND
            = Response.status(Response.Status.NOT_FOUND).entity(ApiResponses.ERR_PET_NOT_FOUND).build();

    private ProductRepository productRepository = InMemoryProductRepository.getInstance();

    @GET
    public List<Product> all() {
        return productRepository.getProducts();
    }

    @GET
    @Path("{id}")
    public Response byId(@PathParam("id") Integer id) {
        Optional<Product> product = productRepository.getProductById(id);
        return product.map(p -> Response.ok().entity(product.get()).build())
                .orElse(RESP_PET_NOT_FOUND);
    }

    @GET
    @Path("/findByCategory/{category}")
    public List<Product> byCategory(@PathParam("category") String category) {
        return productRepository.getProductsByCategory(category);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Product add(Product product) {
        log.debug("Add new pet:{}", product);
        return productRepository.addProduct(product);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public ApiResponse update(@PathParam("id") Integer id, Product product) {
        return productRepository.getProductById(id).map(p -> {
            product.setId(p.getId());
            productRepository.updateProduct(product);
            return ApiResponses.MSG_UPDATE_SUCCESS;
        }).orElse(ApiResponses.ERR_PET_NOT_FOUND);
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public ApiResponse delete(@PathParam("id") Integer id) {
        return productRepository.getProductById(id).map(p -> {
            productRepository.deleteProduct(id);
            return ApiResponses.MSG_DELETE_SUCCESS;
        }).orElse(ApiResponses.ERR_PET_NOT_FOUND);
    }
}
```

## Application class
>PetApplication.java
```java
package io.examples.helidon.petstore;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
@ApplicationPath("/")
public class PetApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> set = new HashSet<>();
        set.add(PetResource.class);
        return Collections.unmodifiableSet(set);
    }
}
```

## Build
>mvn clean install

## start application
>java -jar target\petstore.jar

```
E:\Workspaces\learn-30things-in-30days\day30-helidon\petstore>java -jar target\petstore.jar
2018.10.30 23:39:45 INFO org.jboss.weld.Version Thread[main,5,main]: WELD-000900: 3.0.3 (Final)
2018.10.30 23:39:45 INFO org.jboss.weld.Bootstrap Thread[main,5,main]: WELD-ENV-000020: Using jandex for bean discovery
2018.10.30 23:39:45 INFO org.jboss.weld.Bootstrap Thread[main,5,main]: WELD-000101: Transactional services not available. Injection of @Inject UserTransaction not available. Transactional observers will be invoked synchronously.
2018.10.30 23:39:46 INFO org.jboss.weld.Event Thread[main,5,main]: WELD-000411: Observer method [BackedAnnotatedMethod] private org.glassfish.jersey.ext.cdi1x.internal.CdiComponentProvider.processAnnotatedType(@Observes ProcessAnnotatedType) receives events for all annotated types. Consider restricting events using @WithAnnotations or a generic type with bounds.
2018.10.30 23:39:46 WARN org.jboss.weld.Bootstrap Thread[main,5,main]: WELD-000146: BeforeBeanDiscovery.addAnnotatedType(AnnotatedType<?>) used for class org.glassfish.jersey.ext.cdi1x.internal.CdiComponentProvider$JaxRsParamProducer is deprecated from CDI 1.1!
2018.10.30 23:39:46 INFO org.jboss.weld.Bootstrap Thread[main,5,main]: WELD-ENV-002003: Weld SE container 0b98b4c1-e5eb-452b-8825-3f216d78e9a4 initialized
2018.10.30 23:39:47 INFO io.netty.util.internal.PlatformDependent Thread[main,5,main]: Your platform does not provide complete low-level API for accessing direct buffers reliably. Unless explicitly requested, heap buffer will always be preferred to avoid potential system instability.
2018.10.30 23:39:47 INFO io.helidon.webserver.netty.NettyWebServer Thread[nioEventLoopGroup-2-1,10,main]: Channel '@default' started: [id: 0x617c5b0c, L:/0:0:0:0:0:0:0:0:9080]
2018.10.30 23:39:47 INFO io.helidon.microprofile.server.ServerImpl Thread[nioEventLoopGroup-2-1,10,main]: Server started on http://localhost:9080 (and all other host addresses) in 352 milliseconds.
```

The service is started on port 9080

## Testing

**Get All Pets**
- URL: http://localhost:9080/v1/pet/
- Method: GET
- Reponse:
```json
[
    {
        "id": 1,
        "name": "Angelfish",
        "category": "Fish"
    },
    {
        "id": 2,
        "name": "Tiger Shark",
        "category": "Fish"
    },
    {
        "id": 3,
        "name": "Koi",
        "category": "Fish"
    },
    {
        "id": 4,
        "name": "Goldfish",
        "category": "Fish"
    },
    {
        "id": 5,
        "name": "Bulldog",
        "category": "Dogs"
    },
    {
        "id": 6,
        "name": "Poodle",
        "category": "Dogs"
    },
    {
        "id": 7,
        "name": "Dalmatian",
        "category": "Dogs"
    },
    {
        "id": 8,
        "name": "Golden Retriever",
        "category": "Dogs"
    },
    {
        "id": 9,
        "name": "Labrador Retriever",
        "category": "Dogs"
    },
    {
        "id": 10,
        "name": "Chihuahua",
        "category": "Dogs"
    },
    {
        "id": 11,
        "name": "Manx",
        "category": "Cats"
    },
    {
        "id": 12,
        "name": "Persian",
        "category": "Cats"
    },
    {
        "id": 13,
        "name": "Parrot",
        "category": "Birds"
    },
    {
        "id": 14,
        "name": "Finch",
        "category": "Birds"
    }
]
```

**Find pet by ID**
- URL: http://localhost:9080/v1/pet/1
- Method: GET
- Reponse:
```json
{
    "id": 1,
    "name": "Angelfish",
    "category": "Fish"
}
```

- URL: http://localhost:9080/v1/pet/19
- Method: GET
- Reponse:
```json
{
    "code": 1001,
    "type": "Error",
    "message": "Pet not found"
}
```

**Find by category**

- URL: http://localhost:9080/v1/pet/findByCategory/Fish
- Method: GET
- Reponse:
```json
[
    {
        "id": 1,
        "name": "Angelfish",
        "category": "Fish"
    },
    {
        "id": 4,
        "name": "Goldfish",
        "category": "Fish"
    },
    {
        "id": 3,
        "name": "Koi",
        "category": "Fish"
    },
    {
        "id": 2,
        "name": "Tiger Shark",
        "category": "Fish"
    }
]
```

**Add a pet**
- URL: http://localhost:9080/v1/pet/
- Method: POST
- Body:
```json
{
	"name": "Another Fish",
	"category": "Fish"
}
```
- Reponse:
```json
{
    "id": 15,
    "name": "Another Fish",
    "category": "Fish"
}
```

**Update a pet**
- URL: http://localhost:9080/v1/pet/2
- Method: PUT
- Body:
```json
{
	"name": "New Fish name",
	"category": "Fish"
}
```
- Reponse:
```json
{
    "code": 2001,
    "type": "Message",
    "message": "Update pet successfully"
}
```

**Delete a pet**
- URL: http://localhost:9080/v1/pet/14
- Method: DELETE
- Reponse:
```json
{
    "code": 2002,
    "type": "Message",
    "message": "Delete pet successfully"
}
```

 That's all for today, you can find the complete source code under [this folder](petstore). 
