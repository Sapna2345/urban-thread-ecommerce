package com.Sapna.Ecomm.controller;

import com.Sapna.Ecomm.dto.LoginResponse;
import com.Sapna.Ecomm.model.User;
import com.Sapna.Ecomm.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
@Tag(name = "User APIs", description = "APIs for user registration, login and management")
public class UserController {
    @Autowired
     private UserService userService;

    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account"
    )
    public User registerUser(@RequestBody User user){
        return  userService.registerUser(user);
    }

    @PostMapping("/login")
    @Operation(
            summary = "User login",
            description = "Authenticates user and returns JWT token"
    )
    public LoginResponse loginUser(@RequestBody User user){
        return userService.loginUser(user.getEmail(),user.getPassword());
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete user",
            description = "Deletes a user by ID"
    )
    public String deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return "User deleted successfully";
    }

    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Returns all registered users"
    )
    public List<User> getAllUsers(){

        return userService.getAllUsers();
    }
}
