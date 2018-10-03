package io.examples.spring.reactive.mongodb.service;

import io.examples.spring.reactive.mongodb.entity.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Gary Cheng
 */
public interface ProductService {
    /**
     * Return all proucts
     *
     * @return
     */
    Flux<Product> getProducts();

    /**
     * Find products by category
     *
     * @param category
     * @return
     */
    Flux<Product> getProductsByCategory(String category);

    /**
     * Find product by id
     *
     * @param id
     * @return
     */
    Mono<Product> getProductById(String id);

    /**
     * Create a new Product
     *
     * @param product
     * @return
     */
    Mono<Product> addProduct(Product product);

    /**
     * Update a product
     *
     * @param product
     * @return
     */
    Mono<Product> updateProduct(String id, Product product);

    /**
     * Delete a product by id
     *
     * @param id
     * @return
     */
    Mono<Boolean> deleteProduct(String id);
}
