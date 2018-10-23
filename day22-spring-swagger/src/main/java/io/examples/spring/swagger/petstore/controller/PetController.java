package io.examples.spring.swagger.petstore.controller;


import io.examples.spring.swagger.petstore.common.ApiResponse;
import io.examples.spring.swagger.petstore.common.ApiResponses;
import io.examples.spring.swagger.petstore.entity.Product;
import io.examples.spring.swagger.petstore.repository.ProductRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

import java.util.List;

/**
 * Pet Controller on spring-webflux
 *
 * @author Gary Cheng
 */
@RestController
@RequestMapping(value = "/v1/pet", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(value = "petstore", description = "Operations pertaining to petss in Online Store")
public class PetController {
    private static final ResponseEntity<ApiResponse> RESP_PET_NOT_FOUND
            = new ResponseEntity<>(ApiResponses.ERR_PET_NOT_FOUND, HttpStatus.NOT_FOUND);

    @Autowired
    private ProductRepository productRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Product> all() {
        return productRepository.getProducts();
    }

    @ApiOperation(value = "Get Pet by ID", response = Product.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Successfully retrieved pet"),
            @io.swagger.annotations.ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> byId(@PathVariable("id") Integer id) {
        return productRepository.getProductById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(RESP_PET_NOT_FOUND);
    }

    @RequestMapping(value = "/findByCategory/{category}", method = RequestMethod.GET)
    @ResponseBody
    public List<Product> byCategory(@PathVariable("category") String category) {
        return productRepository.getProductsByCategory(category);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Product add(@RequestBody Product product) {
        return productRepository.addProduct(product);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ApiResponse update(@PathVariable("id") Integer id, @RequestBody Product product) {
        return productRepository.getProductById(id)
                .map(p -> {
                    product.setId(p.getId());
                    productRepository.updateProduct(product);
                    return ApiResponses.MSG_UPDATE_SUCCESS;
                })
                .orElse(ApiResponses.ERR_PET_NOT_FOUND);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ApiResponse delete(@PathVariable("id") Integer id) {
        return productRepository.getProductById(id)
                .map(p -> {
                    productRepository.deleteProduct(id);
                    return ApiResponses.MSG_DELETE_SUCCESS;
                })
                .orElse(ApiResponses.ERR_PET_NOT_FOUND);
    }
}