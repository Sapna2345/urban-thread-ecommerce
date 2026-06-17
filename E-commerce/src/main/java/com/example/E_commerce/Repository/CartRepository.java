package com.example.E_commerce.Repository;

import com.example.E_commerce.Entity.Cart;
import com.example.E_commerce.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart,Long> {
    List<Cart> findByUser_Id( Long userId);
    Cart findByUser_IdAndProduct_Id(Long userId,Long Product_id);
    List<Cart> findByUser(User user);
}
