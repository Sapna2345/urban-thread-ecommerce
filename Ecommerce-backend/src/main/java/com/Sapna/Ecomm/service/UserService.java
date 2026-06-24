package com.Sapna.Ecomm.service;
import com.Sapna.Ecomm.model.User;
import com.Sapna.Ecomm.repo.UserRepository;
import com.Sapna.Ecomm.util.JwtUtil;
import com.Sapna.Ecomm.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public LoginResponse loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        System.out.println("User Found: " + user);
        System.out.println("Password Match: " +
                passwordEncoder.matches(password, user.getPassword()));
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            String token = jwtUtil.generateToken(user.getId(), user.getEmail());

            return new LoginResponse(
                    token,
                    user.getId(),
                    user.getName(),
                    user.getEmail()
            );
        }

        return null;
    }

    public List<User> getAllUsers() {
        return  userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
