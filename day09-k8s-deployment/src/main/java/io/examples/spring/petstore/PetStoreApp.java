package io.examples.spring.petstore;

import io.examples.spring.petstore.repository.FluxProductRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Gary Cheng
 */
@SpringBootApplication
@Configuration
public class PetStoreApp {

    public static void main(String[] args) {
        SpringApplication.run(PetStoreApp.class, args);
    }

    @Bean
    FluxProductRepository productRepository() {
        return FluxProductRepository.getInstance();
    }
}
