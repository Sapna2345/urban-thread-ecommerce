package com.example.E_commerce.Service;

import com.example.E_commerce.Config.JwtUtil;
import com.example.E_commerce.Entity.User;
import com.example.E_commerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
private JwtUtil jwtUtil;
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return "User not found";
        }
        if (user.getPassword()==null){
            return "password missing in db";
        }
        if (user.getPassword().equals(password)) {
            return jwtUtil.generateToken(email);
        }
        return "invalid password";
    }
}
