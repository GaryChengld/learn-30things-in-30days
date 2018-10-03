package io.examples.spring.reactive.mongodb.repository;

import io.examples.spring.reactive.mongodb.entity.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Gary Cheng
 */
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

    /**
     * Find products by category
     *
     * @param category
     * @return
     */
    Flux<Product> findByCategory(String category);

    /**
     * Find product by id
     *
     * @param category
     * @return
     */
    Mono<Product> findById(String category);
}
