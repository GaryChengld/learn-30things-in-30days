package io.examples.spring.reactive.mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * @author Gary Cheng
 */
@SpringBootApplication
@EnableReactiveMongoRepositories
@EnableMongoAuditing
public class ReactiveMongoApp {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveMongoApp.class, args);
    }
}
