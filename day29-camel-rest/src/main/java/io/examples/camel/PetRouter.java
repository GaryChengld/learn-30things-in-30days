package io.examples.camel;

import io.examples.camel.common.ApiResponse;
import io.examples.camel.entity.Product;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

/**
 * @author Gary Cheng
 */
@Component
public class PetRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("netty-http")
                .bindingMode(RestBindingMode.json)
                .port(9080);

        rest("/v1/pet")
                .get("/")
                .to("bean:petService?method=findAll")
                .get("/findByCategory/{category}").to("bean:petService?method=findByCategory(${header.category})")
                .get("/{id}")
                .to("bean:petService?method=findById(${header.id})")
                .post("/").consumes("application/json").type(Product.class)
                .to("bean:petService?method=add(${body})")
                .put("/{id}").consumes("application/json").type(Product.class)
                .to("bean:petService?method=update(${header.id}, ${body})")
                .delete("/{id}")
                .to("bean:petService?method=delete(${header.id})");
    }
}
