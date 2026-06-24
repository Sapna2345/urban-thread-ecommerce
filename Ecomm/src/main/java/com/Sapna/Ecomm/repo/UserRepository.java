package com.Sapna.Ecomm.repo;

import com.Sapna.Ecomm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);
}
