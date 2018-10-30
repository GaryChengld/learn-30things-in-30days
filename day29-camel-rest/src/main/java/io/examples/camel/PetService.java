package io.examples.camel;

import io.examples.camel.common.ApiResponse;
import io.examples.camel.common.ApiResponses;
import io.examples.camel.entity.Product;
import io.examples.camel.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author Gary Cheng
 */
@Component
public class PetService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.getProducts();
    }

    public List<Product> findByCategory(String category) {
        return productRepository.getProductsByCategory(category);
    }

    public Object findById(Integer id) {
        Optional<Product> product = productRepository.getProductById(id);
        if (product.isPresent()) {
            return product.get();
        } else {
            return ApiResponses.ERR_PET_NOT_FOUND;
        }
    }

    public Product add(Product product) {
        return productRepository.addProduct(product);
    }

    public ApiResponse update(Integer id, Product product) {
        Optional<Product> existProduct = productRepository.getProductById(id);
        if (existProduct.isPresent()) {
            product.setId(id);
            productRepository.updateProduct(product);
            return ApiResponses.MSG_UPDATE_SUCCESS;
        } else {
            return ApiResponses.ERR_PET_NOT_FOUND;
        }
    }

    public ApiResponse delete(Integer id) {
        Optional<Product> existProduct = productRepository.getProductById(id);
        if (existProduct.isPresent()) {
            productRepository.deleteProduct(id);
            return ApiResponses.MSG_DELETE_SUCCESS;
        } else {
            return ApiResponses.ERR_PET_NOT_FOUND;
        }
    }
}
