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

