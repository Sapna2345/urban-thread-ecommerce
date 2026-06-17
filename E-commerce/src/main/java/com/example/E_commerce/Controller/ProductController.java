package com.example.E_commerce.Controller;

import com.example.E_commerce.Entity.Product;
import com.example.E_commerce.Service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService){
        this.productService=productService;
    }
    @PostMapping
    public Product addProduct(@RequestBody Product product){
        return productService.addProduct(product);
    }
    @GetMapping
    public List<Product>getProduct(){
        return  productService.getAllProducts();
    }
    @GetMapping("/search")
    public List<Product> search(@RequestParam String name){
        return productService.searchProducts(name);
    }
}
