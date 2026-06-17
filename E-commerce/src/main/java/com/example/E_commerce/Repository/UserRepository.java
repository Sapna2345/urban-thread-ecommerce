package com.example.E_commerce.Repository;

import com.example.E_commerce.Entity.Cart;
import com.example.E_commerce.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);

}
