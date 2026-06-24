package com.Sapna.Ecomm.service;

import com.Sapna.Ecomm.model.Product;
import com.Sapna.Ecomm.repo.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

     @Test
    void testGetAllProducts(){
         Product p1=new Product();
         p1.setId(1L);
         p1.setName("Laptop");

         Product p2=new Product();
         p2.setId(2L);
         p2.setName("Mobile");

         List<Product> products=List.of(p1,p2);
         when(productRepository.findAll())
                 .thenReturn(products);
         List<Product> result=productService.getAllProducts();
         assertNotNull(result);
         assertEquals(2, result.size());
         assertEquals("Laptop", result.get(0).getName());

         verify(productRepository,times(1))
                 .findAll();

     }

     @Test
    void testGetAllProductById(){
         Product product=new Product();
         product.setId(1L);
         product.setName("Laptop");
         when(productRepository.findById(1L))
                 .thenReturn(Optional.of(product));

         Product result=productService.getProductById(1L);

         assertNotNull(result);
         assertEquals("Laptop",result.getName());
         verify(productRepository,times(1))
                 .findById(1L);
     }
    @Test
    void testAddProduct() {
        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(50000.0);

        when(productRepository.save(product))
                .thenReturn(product);
        Product savedProduct =
                productService.addProduct(product);
        assertNotNull(savedProduct);
        assertEquals("Laptop",
                savedProduct.getName());
        verify(productRepository, times(1))
                .save(product);
    }
    @Test
    void testDeleteProduct() {
        Long productId = 1L;
        productService.deleteProduct(productId);
        verify(productRepository, times(1))
                .deleteById(productId);
    }
    }
