package io.examples.spring.reactive.mongodb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Gary Cheng
 */

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "product")
public class Product {
    @EqualsAndHashCode.Include
    @Id
    private String id;
    private String name;
    @TextIndexed
    private String category;
}
