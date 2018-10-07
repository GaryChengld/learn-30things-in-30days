# Day 6: Spring boot 2, Spring Data and ElastocSearch programming 

Today I'm going to use ElasticSearch in spring-boot application

## Initialize Spring-boot project
Open [Spring Initializr](https://start.spring.io/) page and setup project structure as below

<img width="880" src="https://user-images.githubusercontent.com/3359299/46577135-c61ac080-c9ac-11e8-9cfe-8134ce4a0e7d.PNG" />

Click "Create Project" button and download project zip file, extract it to local directory and open the project in Intellij or Eclipse.

Now I'll create a simple RESTful Movie application use the index I created yesterday. To do index, search, and query Elasticsearch in a Spring application using Spring Data.

First of all, start ElasticSearch in commend prompt.

#### configuration Application.yml
```yaml
server:
  port: 9080

spring:
  data:
    elasticsearch:
      cluster-nodes: localhost:9300
```
The trick thing is ElasticSearch opens 2 ports, the http port is 9200 and the TransportService port is 9300, Spring Data is connecting TransportService, I spent lots of time to figure this out.

#### Application class
```java
package io.examples.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElasticSearchApplication {

    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(ElasticSearchApplication.class, args);
    }
}
```     
#### Create Movie entity and ElasticSearch mapping
```java
package io.examples.elasticsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "moviedb", type = "movies", shards = 1, replicas = 0, refreshInterval = "-1")
public class Movie {
    @Id
    private String id;
    @Field
    private String title;
    @Field
    private Integer year;
    @Field
    private String director;
    @Field
    private String writer;
    @Field
    private String description;
}

```

Note that in the @Document annotation, we indicate that instances of this class should be stored in Elasticsearch in an index called “moviedb“, and with a document type of “movies“.

#### Create repository interface
```java
package io.examples.elasticsearch.repository;

import io.examples.elasticsearch.entity.Movie;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface MovieRepository extends ElasticsearchRepository<Movie, String> {
    /**
     * Search movie by title
     *
     * @param title
     * @return
     */
    List<Movie> findByTitle(String title);

    /**
     * Search movie by description
     *
     * @param description
     * @return
     */
    List<Movie> findByDescription(String description);
}
```
Both methods are keyword based search, like other Spring Data project, it does't need create implement class, spring generates it during runtime.

#### Restful web Controller
```java
package io.examples.elasticsearch.controller;

import io.examples.elasticsearch.common.ApiResponse;
import io.examples.elasticsearch.common.ApiResponses;
import io.examples.elasticsearch.entity.Movie;
import io.examples.elasticsearch.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/v1/movie", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MovieController {
    private static final ResponseEntity<ApiResponse> RESP_NOT_FOUND
            = new ResponseEntity<>(ApiResponses.ERR_MOVIE_NOT_FOUND, HttpStatus.NOT_FOUND);

    @Autowired
    private MovieRepository movieRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Flux<Movie> all() {
        return Flux.create(emitter -> {
            movieRepository.findAll().forEach(emitter::next);
            emitter.complete();
        });
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Mono<ResponseEntity<?>> byId(@PathVariable("id") String id) {
        return Mono.create(emitter -> emitter.success(
                movieRepository.findById(id)
                        .<ResponseEntity<?>>map(ResponseEntity::ok)
                        .orElse(RESP_NOT_FOUND)));
    }

    @RequestMapping(value = "/findByTitle/{title}", method = RequestMethod.GET)
    @ResponseBody
    public Flux<Movie> byTitle(@PathVariable("title") String title) {
        return Flux.create(emitter -> {
            movieRepository.findByTitle(title).forEach(emitter::next);
            emitter.complete();
        });
    }

    @RequestMapping(value = "/findByDescription/{description}", method = RequestMethod.GET)
    @ResponseBody
    public Flux<Movie> byDescription(@PathVariable("description") String description) {
        return Flux.create(emitter -> {
            movieRepository.findByDescription(description).forEach(emitter::next);
            emitter.complete();
        });
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Mono<Movie> add(@RequestBody Movie product) {
        return Mono.create(emitter -> emitter.success(movieRepository.save(product)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Mono<ApiResponse> update(@PathVariable("id") String id, @RequestBody Movie movie) {
        return Mono.create(emitter ->
                emitter.success(movieRepository.findById(id)
                        .map(m -> {
                            movie.setId(id);
                            movieRepository.save(movie);
                            return ApiResponses.MSG_UPDATE_SUCCESS;
                        })
                        .orElse(ApiResponses.ERR_MOVIE_NOT_FOUND))
        );
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Mono<ApiResponse> delete(@PathVariable("id") String id) {
        return Mono.create(emitter ->
                emitter.success(movieRepository.findById(id)
                        .map(m -> {
                            movieRepository.delete(m);
                            return ApiResponses.MSG_UPDATE_SUCCESS;
                        })
                        .orElse(ApiResponses.ERR_MOVIE_NOT_FOUND))
        );
    }
}
```

The RESTful service implements following resources

|Method|Url|Description|
|:---|:---|:---|
|GET|/v1/movie|Get all movies|
|GET|/v1/movie/{id}|Find movie by id|
|GET|/v1/movie/findByTitle/{title}|Find movie by title|
|GET|/v1/movie/findByDescription/{description}|Find movie by description|
|POST|/v1/movie|Add a new movie|
|PUT|/v1/movie/{id}|Update a movie|
|Delete|/v1/movie/{id}|Delete a movie|

And below are the search results in browser

<img width="880" src="https://user-images.githubusercontent.com/3359299/46578403-5e28a200-c9cd-11e8-8545-d9d25de61c62.PNG" />
--
<img width="880" src="https://user-images.githubusercontent.com/3359299/46578413-95974e80-c9cd-11e8-9a2d-b9e03f3c12c6.PNG" />
--
<img width="880" src="https://user-images.githubusercontent.com/3359299/46578416-99c36c00-c9cd-11e8-84ea-a30ace19ce22.PNG" />
--
<img width="880" src="https://user-images.githubusercontent.com/3359299/46578418-9def8980-c9cd-11e8-873b-4041e2a057d2.PNG" />

