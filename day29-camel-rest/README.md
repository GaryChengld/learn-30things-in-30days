# Day 29: Microservices with Apache Camel

## Apache Camel

Apache Camel is a versatile open-source integration framework based on known Enterprise Integration Patterns. Camel empowers you to define routing and mediation rules in a variety of domain-specific languages, including a Java-based Fluent API, Spring or Blueprint XML Configuration files, and a Scala DSL.

Since version 2.14, Apache camel provides REST styled DSL, the intention is to allow end users to define REST services using a REST style with verbs such as GET, POST, DELETE etc.

Now I'm going to create a microservice sample using Apache Camel REST DSL, the sample MicroService implements following resources
                        
|Method|Url|Description|
|:---|:---|:---|
|GET|/v1/pet|Get all pets|
|GET|/v1/pet/{id}|Find pet by id|
|GET|/v1/pet/findByCategory/{category}|Find pets by category|
|POST|/v1/pet|Add a new pet|
|PUT|/v1/pet/{id}|Update a pet|
|Delete|/v1/pet/{id}|Delete a pet|

#### Initialize Spring-boot project

Open [Spring Initializr](https://start.spring.io/) page and setup project structure as below

<img width="880" src="https://user-images.githubusercontent.com/3359299/47693628-c59ecf80-dbd0-11e8-80df-7150dfdd9802.PNG" />

Click "Create Project" button and download project zip file, extract it to local directory and open the project in Intellij or Eclipse.

#### Maven dependencies

Add below camel jar to maven pom.xml

```xml
    <dependency>
      <groupId>org.apache.camel</groupId>
      <artifactId>camel-jackson</artifactId>
      <version>${camel.version}</version>
    </dependency>
    <dependency>
      <groupId>org.apache.camel</groupId>
      <artifactId>camel-netty-http</artifactId>
      <version>${camel.version}</version>
    </dependency>
    <dependency>
      <groupId>org.apache.camel</groupId>
      <artifactId>camel-http</artifactId>
      <version>${camel.version}</version>
    </dependency>
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <version>1.18.0</version>
      <scope>provided</scope>
    </dependency>    
```

#### Application configure
>application.yml
```yaml
camel:
  springboot:
    main-run-controller: true

logging:
  level:
    io.examples: DEBUG
```

#### Service
>PetService.java
```java
package io.examples.camel;

import io.examples.camel.common.ApiResponse;
import io.examples.camel.common.ApiResponses;
import io.examples.camel.entity.Product;
import io.examples.camel.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author Gary Cheng
 */
@Component
public class PetService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.getProducts();
    }

    public List<Product> findByCategory(String category) {
        return productRepository.getProductsByCategory(category);
    }

    public Object findById(Integer id) {
        Optional<Product> product = productRepository.getProductById(id);
        if (product.isPresent()) {
            return product.get();
        } else {
            return ApiResponses.ERR_PET_NOT_FOUND;
        }
    }

    public Product add(Product product) {
        return productRepository.addProduct(product);
    }

    public ApiResponse update(Integer id, Product product) {
        Optional<Product> existProduct = productRepository.getProductById(id);
        if (existProduct.isPresent()) {
            product.setId(id);
            productRepository.updateProduct(product);
            return ApiResponses.MSG_UPDATE_SUCCESS;
        } else {
            return ApiResponses.ERR_PET_NOT_FOUND;
        }
    }

    public ApiResponse delete(Integer id) {
        Optional<Product> existProduct = productRepository.getProductById(id);
        if (existProduct.isPresent()) {
            productRepository.deleteProduct(id);
            return ApiResponses.MSG_DELETE_SUCCESS;
        } else {
            return ApiResponses.ERR_PET_NOT_FOUND;
        }
    }
}
```    

#### Camel router
>PetRouter.java
```java
package io.examples.camel;

import io.examples.camel.common.ApiResponse;
import io.examples.camel.entity.Product;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

/**
 * @author Gary Cheng
 */
@Component
public class PetRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("netty-http")
                .bindingMode(RestBindingMode.json)
                .port(9080);

        rest("/v1/pet")
                .get("/")
                .to("bean:petService?method=findAll")
                .get("/findByCategory/{category}").to("bean:petService?method=findByCategory(${header.category})")
                .get("/{id}")
                .to("bean:petService?method=findById(${header.id})")
                .post("/").consumes("application/json").type(Product.class)
                .to("bean:petService?method=add(${body})")
                .put("/{id}").consumes("application/json").type(Product.class)
                .to("bean:petService?method=update(${header.id}, ${body})")
                .delete("/{id}")
                .to("bean:petService?method=delete(${header.id})");
    }
}
```

#### Test application

**Start applicaiton**
>mvn spring-boot:run

it displays
```
2018-10-30 00:36:43.723  INFO 17872 --- [           main] io.examples.camel.CamelApplication       : Starting CamelApplication on Gary-PC with PID 17872 (E:\Workspaces\learn-30things-in-30days\day29-camel-rest\target\classes started by Gary in E:\Workspaces\learn-30things-in-30days)
2018-10-30 00:36:43.725 DEBUG 17872 --- [           main] io.examples.camel.CamelApplication       : Running with Spring Boot v2.0.6.RELEASE, Spring v5.0.10.RELEASE
2018-10-30 00:36:43.726  INFO 17872 --- [           main] io.examples.camel.CamelApplication       : No active profile set, falling back to default profiles: default
2018-10-30 00:36:43.766  INFO 17872 --- [           main] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@4b5a5ed1: startup date [Tue Oct 30 00:36:43 EDT 2018]; root of context hierarchy
2018-10-30 00:36:45.241  INFO 17872 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'org.apache.camel.spring.boot.CamelAutoConfiguration' of type [org.apache.camel.spring.boot.CamelAutoConfiguration$$EnhancerBySpringCGLIB$$63e0919d] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2018-10-30 00:36:45.513  INFO 17872 --- [           main] o.a.c.i.converter.DefaultTypeConverter   : Type converters loaded (core: 195, classpath: 24)
2018-10-30 00:36:46.019  INFO 17872 --- [           main] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
2018-10-30 00:36:46.081  INFO 17872 --- [           main] o.a.camel.spring.boot.RoutesCollector    : Loading additional Camel XML routes from: classpath:camel/*.xml
2018-10-30 00:36:46.081  INFO 17872 --- [           main] o.a.camel.spring.boot.RoutesCollector    : Loading additional Camel XML rests from: classpath:camel-rest/*.xml
2018-10-30 00:36:46.089  INFO 17872 --- [           main] o.a.camel.spring.SpringCamelContext      : Apache Camel 2.22.1 (CamelContext: camel-1) is starting
2018-10-30 00:36:46.091  INFO 17872 --- [           main] o.a.c.m.ManagedManagementStrategy        : JMX is enabled
2018-10-30 00:36:46.338  INFO 17872 --- [           main] o.a.camel.spring.SpringCamelContext      : StreamCaching is not in use. If using streams then its recommended to enable stream caching. See more details at http://camel.apache.org/stream-caching.html
2018-10-30 00:36:46.389  INFO 17872 --- [           main] o.a.c.c.n.h.HttpServerBootstrapFactory   : BootstrapFactory on port 9080 is using bootstrap configuration: [NettyServerBootstrapConfiguration{protocol='http', host='0.0.0.0', port=9080, broadcast=false, sendBufferSize=65536, receiveBufferSize=65536, receiveBufferSizePredictor=0, workerCount=0, bossCount=1, keepAlive=true, tcpNoDelay=true, reuseAddress=true, connectTimeout=10000, backlog=0, serverPipelineFactory=org.apache.camel.component.netty.http.HttpServerPipelineFactory@ce9b9a9, nettyServerBootstrapFactory=null, options=null, ssl=false, sslHandler=null, sslContextParameters='null', needClientAuth=false, enabledProtocols='TLSv1,TLSv1.1,TLSv1.2, keyStoreFile=null, trustStoreFile=null, keyStoreResource='null', trustStoreResource='null', keyStoreFormat='JKS', securityProvider='SunX509', passphrase='null', bossPool=null, workerPool=null, networkInterface='null'}]
2018-10-30 00:36:46.447  INFO 17872 --- [           main] o.a.camel.spring.boot.RoutesCollector    : Starting CamelMainRunController to ensure the main thread keeps running
2018-10-30 00:36:46.802  INFO 17872 --- [           main] c.n.SingleTCPNettyServerBootstrapFactory : ServerBootstrap binding to 0.0.0.0:9080
2018-10-30 00:36:46.811  INFO 17872 --- [           main] o.a.camel.component.netty.NettyConsumer  : Netty consumer bound to: 0.0.0.0:9080
2018-10-30 00:36:46.814  INFO 17872 --- [           main] o.a.camel.spring.SpringCamelContext      : Route: route1 started and consuming from: http://0.0.0.0:9080/v1/pet/?httpMethodRestrict=GET
2018-10-30 00:36:46.815  INFO 17872 --- [           main] o.a.camel.component.netty.NettyConsumer  : Netty consumer bound to: 0.0.0.0:9080
2018-10-30 00:36:46.815  INFO 17872 --- [           main] o.a.camel.spring.SpringCamelContext      : Route: route2 started and consuming from: http://0.0.0.0:9080/v1/pet/findByCategory/%7Bcategory%7D?httpMethodRestrict=GET
2018-10-30 00:36:46.816  INFO 17872 --- [           main] o.a.camel.component.netty.NettyConsumer  : Netty consumer bound to: 0.0.0.0:9080
2018-10-30 00:36:46.816  INFO 17872 --- [           main] o.a.camel.spring.SpringCamelContext      : Route: route3 started and consuming from: http://0.0.0.0:9080/v1/pet/%7Bid%7D?httpMethodRestrict=GET
2018-10-30 00:36:46.817  INFO 17872 --- [           main] o.a.camel.component.netty.NettyConsumer  : Netty consumer bound to: 0.0.0.0:9080
2018-10-30 00:36:46.817  INFO 17872 --- [           main] o.a.camel.spring.SpringCamelContext      : Route: route4 started and consuming from: http://0.0.0.0:9080/v1/pet/?httpMethodRestrict=POST
2018-10-30 00:36:46.818  INFO 17872 --- [           main] o.a.camel.component.netty.NettyConsumer  : Netty consumer bound to: 0.0.0.0:9080
2018-10-30 00:36:46.818  INFO 17872 --- [           main] o.a.camel.spring.SpringCamelContext      : Route: route5 started and consuming from: http://0.0.0.0:9080/v1/pet/%7Bid%7D?httpMethodRestrict=PUT
2018-10-30 00:36:46.819  INFO 17872 --- [           main] o.a.camel.component.netty.NettyConsumer  : Netty consumer bound to: 0.0.0.0:9080
2018-10-30 00:36:46.819  INFO 17872 --- [           main] o.a.camel.spring.SpringCamelContext      : Route: route6 started and consuming from: http://0.0.0.0:9080/v1/pet/%7Bid%7D?httpMethodRestrict=DELETE
2018-10-30 00:36:46.819  INFO 17872 --- [           main] o.a.camel.spring.SpringCamelContext      : Total 6 routes, of which 6 are started
2018-10-30 00:36:46.820  INFO 17872 --- [           main] o.a.camel.spring.SpringCamelContext      : Apache Camel 2.22.1 (CamelContext: camel-1) started in 0.730 seconds
2018-10-30 00:36:46.823  INFO 17872 --- [           main] io.examples.camel.CamelApplication       : Started CamelApplication in 3.629 seconds (JVM running for 4.156)
```

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

 That's all for today, you can find the complete source code under [this folder](.). 




