package io.examples.elasticsearch.repository;

import io.examples.elasticsearch.entity.Movie;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author Gary Cheng
 */
public interface MovieRepository extends ElasticsearchRepository<Movie, String> {
    /**
     * Search movie by title
     *
     * @param title
     * @return
     */
    List<Movie> searchByTitle(String title);

    /**
     * Search movie by description
     *
     * @param description
     * @return
     */
    List<Movie> searchByDescription(String description);
}
