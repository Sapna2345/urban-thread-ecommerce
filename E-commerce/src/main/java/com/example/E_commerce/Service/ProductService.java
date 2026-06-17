package com.example.E_commerce.Service;

import com.example.E_commerce.Entity.Product;
import com.example.E_commerce.Repository.ProductRepository;
import com.example.E_commerce.Repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ProductService {
    private final ProductRepository productRepository;
    public ProductService(ProductRepository productRepository){

        this.productRepository=productRepository;
    }
    public Product addProduct(Product product){

        return productRepository.save(product);
    }
    public List<Product> getAllProducts()
    {
        return productRepository.findAll();
    }
    public List<Product>searchProducts(String name){

        return productRepository.findByNameContaining(name);
    }

}
