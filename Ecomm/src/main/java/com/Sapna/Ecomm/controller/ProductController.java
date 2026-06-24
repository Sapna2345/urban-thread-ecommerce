package com.Sapna.Ecomm.controller;

import com.Sapna.Ecomm.model.Product;
import com.Sapna.Ecomm.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/products")
@CrossOrigin("*")
@Tag(name = "Product APIs", description = "APIs for product management")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    @Operation(
            summary = "Get all products",
            description = "Returns a list of all products"
    )
    public List<Product> getAllProducts()
    {
        return productService.getAllProducts();
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Get product by ID",
            description = "Returns product details by product ID"
    )
    public Product getProductById(@PathVariable Long id)
    {

        return productService.getProductById(id);
    }

    @PostMapping
    @Operation(
            summary = "Add new product",
            description = "Add new product to database"
    )
    public Product addProduct(@RequestBody Product product)
    {

        return productService.addProduct(product);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete product",
            description = "Deletes any product by id"
    )
    public void DeleteProduct(@PathVariable Long id)
    {

        productService.deleteProduct(id);
    }
}
