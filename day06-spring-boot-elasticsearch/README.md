# Day 6: Spring boot 2, Spring Data and ElastocSearch programming 

Today I'm going to use ElasticSearch in spring-boot application

## Initialize Spring-boot project
Open [Spring Initializr](https://start.spring.io/) page and setup project structure as below

<img width="880" src="https://user-images.githubusercontent.com/3359299/46577135-c61ac080-c9ac-11e8-9cfe-8134ce4a0e7d.PNG" />

Client create project button and download project zip file, extract it to local directory and open the project in Intellij or Eclipse.

Now I'll create a simple RESTful Movie application use the index I created yesterday. To do index, search, and query Elasticsearch in a Spring application using Spring Data.

First of all, start ElasticSearch in commend prompt.

#### Create Movie entity and ElasticSearch mapping
```java
package io.examples.elasticsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "moviedb", type = "movies", shards = 1, replicas = 0, refreshInterval = "-1")
public class Movie {
    @Id
    private String id;
    private String title;
    private Integer year;
    private String director;
    private String writer;
    private String description;
}
```

Note that in the @Document annotation, we indicate that instances of this class should be stored in Elasticsearch in an index called “moviedb“, and with a document type of “movies“. 