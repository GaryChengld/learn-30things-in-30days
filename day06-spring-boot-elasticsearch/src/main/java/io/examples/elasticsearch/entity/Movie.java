package io.examples.elasticsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author Gary Cheng
 */
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
