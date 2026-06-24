package com.Sapna.Ecomm.service;
import com.Sapna.Ecomm.dto.LoginResponse;
import com.Sapna.Ecomm.model.User;
import com.Sapna.Ecomm.repo.UserRepository;
import com.Sapna.Ecomm.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    @Test
    void testRegisterUser(){
        User user=new User();
        user.setName("Sapna");
        user.setEmail("Sapna@gmail.com");
        user.setPassword("12345");

        //mocking password encoding
        when(passwordEncoder.encode("12345"))
                .thenReturn("encodedpassword");

        //save the mock
        when(userRepository.save(user))
                .thenReturn(user);

        //actual method call
        User savedUser = userService.registerUser(user);
        assertNotNull(savedUser);
        assertEquals("Sapna",savedUser.getName());
        assertEquals("Sapna@gmail.com",savedUser.getEmail());
        assertEquals("encodedpassword",savedUser.getPassword());

        //verify
        verify(passwordEncoder,times(1))
                .encode("12345");
        verify(userRepository,times(1))
                .save(user);
    }
    @Test
    void testLoginUserSuccess(){
        User user = new User();
        user.setId(1L);
        user.setName("Sapna");
        user.setEmail("Sapna@gmail.com");
        user.setPassword("encodedpassword");

        //mock repo
        when(userRepository.findByEmail("Sapna@gmail.com"))
                .thenReturn(user);

        //mock password
        when(passwordEncoder.matches("12345","encodedpassword"))
                .thenReturn(true);

        //mock token
        when(jwtUtil.generateToken(1L,"Sapna@gmail.com"))
                .thenReturn("jwt-token");

        //act
        LoginResponse response=userService.loginUser("Sapna@gmail.com","12345");

        //assert
        assertNotNull(response);
        assertEquals("jwt-token",response.getToken());
        assertEquals(1L,response.getId());
        assertEquals("Sapna",response.getName());
        assertEquals("Sapna@gmail.com",response.getEmail());

        //verify
        verify(userRepository,times(1))
                .findByEmail("Sapna@gmail.com");
        verify(passwordEncoder, times(2))
                .matches("12345", "encodedpassword");

        verify(jwtUtil, times(1))
                .generateToken(1L, "Sapna@gmail.com");
    }
    @Test
    void testLoginUserFailure(){

        User user = new User();
        user.setId(1L);
        user.setName("Sapna");
        user.setEmail("Sapna@gmail.com");
        user.setPassword("encodedpassword");

        when(userRepository.findByEmail("Sapna@gmail.com"))
                .thenReturn(user);

        when(passwordEncoder.matches("wrongpass","encodedpassword"))
                .thenReturn(false);

        LoginResponse response=userService.loginUser("Sapna@gmail.com","wrongpass");

        assertNull(response);
        verify(userRepository,times(1))
                .findByEmail("Sapna@gmail.com");
        verify(passwordEncoder,times(2))
                .matches("wrongpass","encodedpassword");
        verify(jwtUtil,never())
                .generateToken(anyLong(),anyString());
    }
    @Test
    void testGetAllUsers(){
        User user1=new User();
        user1.setId(1L);
        user1.setName("Sapna");

        User user2=new User();
        user2.setId(2L);
        user2.setName("Rahul");

        List<User> users=List.of(user1,user2);
        when(userRepository.findAll())
                .thenReturn(users);

        List<User> result=userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2,result.size());
        assertEquals("Sapna",result.get(0).getName());

        verify(userRepository, times(1)).findAll();


    }
    @Test
    void testdeleteUser(){

        Long userId=1L;
        userRepository.deleteById(userId);
        verify(userRepository,times(1))
                .deleteById(userId);
    }
}
