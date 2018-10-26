# Day 25:  Applying HATEOAS to a RESTful service with Spring Boot

## What is HATEOAS?

>HATEOAS stands for “Hypermedia as the engine of application state”

With HATEOAS, a client interacts with a network application whose application servers provide information dynamically through hypermedia. A REST client needs little to no prior knowledge about how to interact with an application or server beyond a generic understanding of hypermedia.

For example, when requesting information about a product, a REST service can return the following
- Product details
- Link to update this product
- Link to delete this product

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
Now I did a refactoring for reuse purpose

modify Product.java
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product extends ResourceSupport {
    @EqualsAndHashCode.Include
    private Integer productId;
    private String name;
    private String category;
}
```

Add method in PetController.java
```java
    private Product addLinks(Product product) {
        product.removeLinks();
        Link allLink = linkTo(methodOn(this.getClass()).all()).withRel("all-pets");
        product.add(allLink);
        Link selfLink = linkTo(this.getClass()).slash(product.getProductId()).withSelfRel();
        product.add(selfLink);
        return product;
    }
```

And changed existing methods
```java
    @RequestMapping(method = RequestMethod.GET, produces = {"application/hal+json"})
    @ResponseBody
    public Resources<Product> all() {
        List<Product> products = productRepository.getProducts().stream()
                .map(this::addLinks)
                .collect(Collectors.toList());
        Link link = linkTo(this.getClass()).withSelfRel();
        return new Resources<>(products, link);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {"application/hal+json"})
    public ResponseEntity<?> byId(@PathVariable("id") Integer id) {
        return productRepository.getProductById(id)
                .map(this::addLinks)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(RESP_PET_NOT_FOUND);
    }
```

Now restart applicaiton and send request http://localhost:9080/v1/pet to get all pets, get response
```json
{
    "_embedded": {
        "productList": [
            {
                "productId": 1,
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
            },
            {
                "productId": 2,
                "name": "Tiger Shark",
                "category": "Fish",
                "_links": {
                    "all-pets": {
                        "href": "http://localhost:9080/v1/pet"
                    },
                    "self": {
                        "href": "http://localhost:9080/v1/pet/2"
                    }
                }
            },
            {
                "productId": 3,
                "name": "Koi",
                "category": "Fish",
                "_links": {
                    "all-pets": {
                        "href": "http://localhost:9080/v1/pet"
                    },
                    "self": {
                        "href": "http://localhost:9080/v1/pet/3"
                    }
                }
            },
            {
                "productId": 4,
                "name": "Goldfish",
                "category": "Fish",
                "_links": {
                    "all-pets": {
                        "href": "http://localhost:9080/v1/pet"
                    },
                    "self": {
                        "href": "http://localhost:9080/v1/pet/4"
                    }
                }
            },
            {
                "productId": 5,
                "name": "Bulldog",
                "category": "Dogs",
                "_links": {
                    "all-pets": {
                        "href": "http://localhost:9080/v1/pet"
                    },
                    "self": {
                        "href": "http://localhost:9080/v1/pet/5"
                    }
                }
            },
            {
                "productId": 6,
                "name": "Poodle",
                "category": "Dogs",
                "_links": {
                    "all-pets": {
                        "href": "http://localhost:9080/v1/pet"
                    },
                    "self": {
                        "href": "http://localhost:9080/v1/pet/6"
                    }
                }
            },
            {
                "productId": 7,
                "name": "Dalmatian",
                "category": "Dogs",
                "_links": {
                    "all-pets": {
                        "href": "http://localhost:9080/v1/pet"
                    },
                    "self": {
                        "href": "http://localhost:9080/v1/pet/7"
                    }
                }
            },
            {
                "productId": 8,
                "name": "Golden Retriever",
                "category": "Dogs",
                "_links": {
                    "all-pets": {
                        "href": "http://localhost:9080/v1/pet"
                    },
                    "self": {
                        "href": "http://localhost:9080/v1/pet/8"
                    }
                }
            },
            {
                "productId": 9,
                "name": "Labrador Retriever",
                "category": "Dogs",
                "_links": {
                    "all-pets": {
                        "href": "http://localhost:9080/v1/pet"
                    },
                    "self": {
                        "href": "http://localhost:9080/v1/pet/9"
                    }
                }
            },
            {
                "productId": 10,
                "name": "Chihuahua",
                "category": "Dogs",
                "_links": {
                    "all-pets": {
                        "href": "http://localhost:9080/v1/pet"
                    },
                    "self": {
                        "href": "http://localhost:9080/v1/pet/10"
                    }
                }
            },
            {
                "productId": 11,
                "name": "Manx",
                "category": "Cats",
                "_links": {
                    "all-pets": {
                        "href": "http://localhost:9080/v1/pet"
                    },
                    "self": {
                        "href": "http://localhost:9080/v1/pet/11"
                    }
                }
            },
            {
                "productId": 12,
                "name": "Persian",
                "category": "Cats",
                "_links": {
                    "all-pets": {
                        "href": "http://localhost:9080/v1/pet"
                    },
                    "self": {
                        "href": "http://localhost:9080/v1/pet/12"
                    }
                }
            },
            {
                "productId": 13,
                "name": "Parrot",
                "category": "Birds",
                "_links": {
                    "all-pets": {
                        "href": "http://localhost:9080/v1/pet"
                    },
                    "self": {
                        "href": "http://localhost:9080/v1/pet/13"
                    }
                }
            },
            {
                "productId": 14,
                "name": "Finch",
                "category": "Birds",
                "_links": {
                    "all-pets": {
                        "href": "http://localhost:9080/v1/pet"
                    },
                    "self": {
                        "href": "http://localhost:9080/v1/pet/14"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:9080/v1/pet"
        }
    }
}
```

 That's all for today, you can find the complete source code under [this folder](.).