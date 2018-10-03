package io.examples.spring.reactive.mongodb.service.impl;

import io.examples.spring.reactive.mongodb.entity.Product;
import io.examples.spring.reactive.mongodb.repository.ProductRepository;
import io.examples.spring.reactive.mongodb.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Gary Cheng
 */
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Flux<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public Flux<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public Mono<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    @Override
    public Mono<Product> addProduct(Product product) {
        return productRepository.insert(product);
    }

    @Override
    public Mono<Product> updateProduct(String id, Product product) {
        return this.getProductById(id)
                .doOnSuccess(p -> {
                    p.setName(product.getName());
                    p.setCategory(product.getCategory());
                    productRepository.save(p).subscribe();
                });
    }

    @Override
    public Mono<Boolean> deleteProduct(String id) {
        return this.getProductById(id)
                .doOnSuccess(p -> productRepository.delete(p).subscribe())
                .flatMap(p -> Mono.just(Boolean.TRUE));
    }
}
