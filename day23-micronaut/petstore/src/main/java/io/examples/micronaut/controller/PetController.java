package io.examples.micronaut.controller;

import io.examples.micronaut.health.Health;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

/**
 * @author Gary Cheng
 */
@Controller("/v1/pet")
public class PetController {
    @Get
    public HttpStatus index() {
        return HttpStatus.OK;
    }

    @Get("/version")
    public Health version() {
        return new Health("1.0", "OK");
    }
}
