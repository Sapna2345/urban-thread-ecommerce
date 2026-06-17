package com.example.E_commerce.Controller;

import com.example.E_commerce.Config.JwtUtil;
import com.example.E_commerce.Entity.Cart;
import com.example.E_commerce.Service.AuthService;
import com.example.E_commerce.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CartService cartService;
    public AuthController(AuthService authService){
        this.authService=authService;
    }
    @PostMapping("/login")
    public String login(@RequestParam String email,@RequestParam String password){
        return authService.login(email,password);
    }
    @GetMapping
    public List<Cart> getCart(@RequestHeader("Authorization") String header) {

        String token = header.substring(7); // remove "Bearer "
        String email = jwtUtil.extractEmail(token);
        return cartService.getCartByEmail(email);
    }
}
