# Day 25:  Applying HATEOAS to a RESTful service with Spring Boot

## What is HATEOAS?

>HATEOAS stands for “Hypermedia as the engine of application state”

With HATEOAS, a client interacts with a network application whose application servers provide information dynamically through hypermedia. A REST client needs little to no prior knowledge about how to interact with an application or server beyond a generic understanding of hypermedia.

For example, when requesting information about a product, a REST service can return the following
- Product details
- Links to update this product
- Links to delete this product

## Implementing HATEOAS with Spring Boot

I'm still using same sample application in day09, a very simple PetStore build by spring-boot

It implements following resources
                        
|Method|Url|Description|
|:---|:---|:---|
|GET|/v1/pet|Get all pets|
|GET|/v1/pet/{id}|Find pet by id|
|GET|/v1/pet/findByCategory/{category}|Find pets by category|
|POST|/v1/pet|Add a new pet|
|PUT|/v1/pet/{id}|Update a pet|
|Delete|/v1/pet/{id}|Delete a pet|

The original version, when open http://localhost:9080/v1/pet/1 with GET method, it responses

```json
{
    "id": 1,
    "name": "Angelfish",
    "category": "Fish"
}
```

Now I'm going to add HATEOAS on it.

**Add maven dependency**

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-hateoas</artifactId>
</dependency>
```

Before change, the getPetById's code as following

```java
@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> byId(@PathVariable("id") Integer id) {
        return productRepository.getProductById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(RESP_PET_NOT_FOUND);
    }
```

Now I'm add a link to show all pets

```java
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> byId(@PathVariable("id") Integer id) {
        Optional<Product> productOptional = productRepository.getProductById(id);
        if (productOptional.isPresent()) {
            Resource<Product> resource = new Resource<>(productOptional.get());
            ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).all());
            resource.add(linkTo.withRel("all-pets"));
            return ResponseEntity.ok(resource);
        } else {
            return RESP_PET_NOT_FOUND;
        }
    }
```

Restart application, open http://localhost:9080/v1/pet/1 in browser, it shows following response

```json
{
    "id": 1,
    "name": "Angelfish",
    "category": "Fish",
    "_links": {
        "all-pets": {
            "href": "http://localhost:9080/v1/pet"
        }
    }
}
```

Add self link

```java
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> byId(@PathVariable("id") Integer id) {
        Optional<Product> productOptional = productRepository.getProductById(id);
        if (productOptional.isPresent()) {
            Resource<Product> resource = new Resource<>(productOptional.get());
            ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).all());
            resource.add(linkTo.withRel("all-pets"));
            Link selfLink = linkTo(this.getClass()).slash(id).withSelfRel();
            resource.add(selfLink);
            return ResponseEntity.ok(resource);
        } else {
            return RESP_PET_NOT_FOUND;
        }
    }
```

Send same request, it returns:
```json
{
    "id": 1,
    "name": "Angelfish",
    "category": "Fish",
    "_links": {
        "all-pets": {
            "href": "http://localhost:9080/v1/pet"
        },
        "self": {
            "href": "http://localhost:9080/v1/pet/1"
        }
    }
}
```

