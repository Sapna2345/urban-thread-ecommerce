package com.example.E_commerce.Controller;

import com.example.E_commerce.Entity.User;
import com.example.E_commerce.Service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){
        this.userService=userService;
    }
    @PostMapping
    public User registerUser(@RequestBody User user){

        return  userService.registerUser(user);
    }
    @GetMapping
    public List<User> getUsers(){
        return userService.getAllUsers();
    }

}
