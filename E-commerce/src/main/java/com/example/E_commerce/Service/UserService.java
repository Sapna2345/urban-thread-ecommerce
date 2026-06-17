package com.example.E_commerce.Service;

import com.example.E_commerce.Entity.User;
import com.example.E_commerce.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private  final UserRepository userRepository;
    public UserService(UserRepository userRepository){

        this.userRepository=userRepository;
    }
    public User registerUser(User user){
        System.out.println("password received : "+ user.getPassword());
        return userRepository.save(user);
    }
    public List<User>getAllUsers(){
        return userRepository.findAll();
    }

}
