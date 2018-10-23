# Day 22: Document Spring-boot RESTful Api with Swagger

Documenting applicaton's REST API is very important. It is a public interface, which other modules, applications or developers can use. 

## Swagger

[Swagger](https://swagger.io/) is an open-source software framework backed by a large ecosystem of tools that helps developers design, build, document, and consume RESTful Web services.

## SpringFox 

Springfox is a java project that aims at creating automated JSON API documentation for API's built with Spring and is used in the following tutorial to integrate Swagger into a sample application. Swagger UI for Spring Boot RESTful Webservices.

## Add Swagger document specification to Spring-boot application

Now I'll reuse the 8th day's PetStore Spring-boot sample and add Swagger document on it.

This sample RESTful application implements following resources
                             
|Method|Url|Description|
|:---|:---|:---|
|GET|/v1/pet|Get all pets|
|GET|/v1/pet/{id}|Find pet by id|
|GET|/v1/pet/findByCategory/{category}|Find pets by category|
|POST|/v1/pet|Add a new pet|
|PUT|/v1/pet/{id}|Update a pet|
|Delete|/v1/pet/{id}|Delete a pet|

**Add SpringFox dependencies to maven pom.xml

```xml
  <dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
  </dependency>
  <dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.9.2</version>
  </dependency>
```

**Configuring Swagger in the Application**

Create SwaggerConfig.java

```java
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(regex("/v1/pet.*"))
                .build();
    }
}
```

Start application and open http://localhost:9080/v2/api-docs in browser, it displays

```json
{
    "swagger": "2.0",
    "info": {
        "description": "Api Documentation",
        "version": "1.0",
        "title": "Api Documentation",
        "termsOfService": "urn:tos",
        "contact": {},
        "license": {
            "name": "Apache 2.0",
            "url": "http://www.apache.org/licenses/LICENSE-2.0"
        }
    },
    "host": "localhost:9080",
    "basePath": "/",
    "tags": [
        {
            "name": "pet-controller",
            "description": "Pet Controller"
        }
    ],
    "paths": {
        "/v1/pet": {
            "get": {
                "tags": [
                    "pet-controller"
                ],
                "summary": "all",
                "operationId": "allUsingGET",
                "produces": [
                    "application/json;charset=UTF-8"
                ],
                "responses": {
                    "200": {
                        "description": "OK",
                        "schema": {
                            "type": "array",
                            "items": {
                                "$ref": "#/definitions/Product"
                            }
                        }
                    },
                    "401": {
                        "description": "Unauthorized"
                    },
                    "403": {
                        "description": "Forbidden"
                    },
                    "404": {
                        "description": "Not Found"
                    }
                },
                "deprecated": false
            },
            "post": {
                "tags": [
                    "pet-controller"
                ],
                "summary": "add",
                "operationId": "addUsingPOST",
                "consumes": [
                    "application/json"
                ],
                "produces": [
                    "application/json;charset=UTF-8"
                ],
                "parameters": [
                    {
                        "in": "body",
                        "name": "product",
                        "description": "product",
                        "required": true,
                        "schema": {
                            "$ref": "#/definitions/Product"
                        }
                    }
                ],
                "responses": {
                    "200": {
                        "description": "OK",
                        "schema": {
                            "$ref": "#/definitions/Product"
                        }
                    },
                    "201": {
                        "description": "Created"
                    },
                    "401": {
                        "description": "Unauthorized"
                    },
                    "403": {
                        "description": "Forbidden"
                    },
                    "404": {
                        "description": "Not Found"
                    }
                },
                "deprecated": false
            }
        },
        "/v1/pet/findByCategory/{category}": {
            "get": {
                "tags": [
                    "pet-controller"
                ],
                "summary": "byCategory",
                "operationId": "byCategoryUsingGET",
                "produces": [
                    "application/json;charset=UTF-8"
                ],
                "parameters": [
                    {
                        "name": "category",
                        "in": "path",
                        "description": "category",
                        "required": true,
                        "type": "string"
                    }
                ],
                "responses": {
                    "200": {
                        "description": "OK",
                        "schema": {
                            "type": "array",
                            "items": {
                                "$ref": "#/definitions/Product"
                            }
                        }
                    },
                    "401": {
                        "description": "Unauthorized"
                    },
                    "403": {
                        "description": "Forbidden"
                    },
                    "404": {
                        "description": "Not Found"
                    }
                },
                "deprecated": false
            }
        },
        "/v1/pet/{id}": {
            "get": {
                "tags": [
                    "pet-controller"
                ],
                "summary": "byId",
                "operationId": "byIdUsingGET",
                "produces": [
                    "application/json;charset=UTF-8"
                ],
                "parameters": [
                    {
                        "name": "id",
                        "in": "path",
                        "description": "id",
                        "required": true,
                        "type": "integer",
                        "format": "int32"
                    }
                ],
                "responses": {
                    "200": {
                        "description": "OK",
                        "schema": {
                            "type": "object"
                        }
                    },
                    "401": {
                        "description": "Unauthorized"
                    },
                    "403": {
                        "description": "Forbidden"
                    },
                    "404": {
                        "description": "Not Found"
                    }
                },
                "deprecated": false
            },
            "put": {
                "tags": [
                    "pet-controller"
                ],
                "summary": "update",
                "operationId": "updateUsingPUT",
                "consumes": [
                    "application/json"
                ],
                "produces": [
                    "application/json;charset=UTF-8"
                ],
                "parameters": [
                    {
                        "name": "id",
                        "in": "path",
                        "description": "id",
                        "required": true,
                        "type": "integer",
                        "format": "int32"
                    },
                    {
                        "in": "body",
                        "name": "product",
                        "description": "product",
                        "required": true,
                        "schema": {
                            "$ref": "#/definitions/Product"
                        }
                    }
                ],
                "responses": {
                    "200": {
                        "description": "OK",
                        "schema": {
                            "$ref": "#/definitions/ApiResponse"
                        }
                    },
                    "201": {
                        "description": "Created"
                    },
                    "401": {
                        "description": "Unauthorized"
                    },
                    "403": {
                        "description": "Forbidden"
                    },
                    "404": {
                        "description": "Not Found"
                    }
                },
                "deprecated": false
            },
            "delete": {
                "tags": [
                    "pet-controller"
                ],
                "summary": "delete",
                "operationId": "deleteUsingDELETE",
                "produces": [
                    "application/json;charset=UTF-8"
                ],
                "parameters": [
                    {
                        "name": "id",
                        "in": "path",
                        "description": "id",
                        "required": true,
                        "type": "integer",
                        "format": "int32"
                    }
                ],
                "responses": {
                    "200": {
                        "description": "OK",
                        "schema": {
                            "$ref": "#/definitions/ApiResponse"
                        }
                    },
                    "204": {
                        "description": "No Content"
                    },
                    "401": {
                        "description": "Unauthorized"
                    },
                    "403": {
                        "description": "Forbidden"
                    }
                },
                "deprecated": false
            }
        }
    },
    "definitions": {
        "ApiResponse": {
            "type": "object",
            "properties": {
                "code": {
                    "type": "integer",
                    "format": "int32"
                },
                "message": {
                    "type": "string"
                },
                "type": {
                    "type": "string"
                }
            },
            "title": "ApiResponse"
        },
        "Product": {
            "type": "object",
            "properties": {
                "category": {
                    "type": "string"
                },
                "id": {
                    "type": "integer",
                    "format": "int32"
                },
                "name": {
                    "type": "string"
                }
            },
            "title": "Product"
        }
    }
}
```

Now I want some nice human readable structured documentation, and this is where Swagger UI takes over.

Change SwaggerConfig.java

```java
@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(regex("/v1/pet.*"))
                .build();
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("Spring Boot PetStore REST API")
                .description("\"Spring Boot REST API for Pet Store\"")
                .version("1.0.0")
                .license("Apache License Version 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
                .build();
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
```

Restart application and open http://localhost:9080/swagger-ui.html in browser

<img width="880" src="https://user-images.githubusercontent.com/3359299/47329920-6414b900-d644-11e8-86c3-348fe588c9c5.PNG" />

Click pet-controller, it shows

<img width="880" src="https://user-images.githubusercontent.com/3359299/47329923-67a84000-d644-11e8-9b06-dd54cf2307d7.PNG" />
