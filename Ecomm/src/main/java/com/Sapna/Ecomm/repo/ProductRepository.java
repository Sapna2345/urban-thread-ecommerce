package com.Sapna.Ecomm.repo;

import com.Sapna.Ecomm.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
