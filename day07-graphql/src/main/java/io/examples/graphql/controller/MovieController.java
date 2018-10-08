package io.examples.graphql.controller;

import io.examples.graphql.common.ApiResponse;
import io.examples.graphql.common.ApiResponses;
import io.examples.graphql.entity.Movie;
import io.examples.graphql.repository.MovieRepository;
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

/**
 * @author Gary Cheng
 */
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
