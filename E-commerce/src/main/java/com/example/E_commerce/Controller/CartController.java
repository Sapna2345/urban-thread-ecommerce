package com.example.E_commerce.Controller;

import com.example.E_commerce.Entity.Cart;
import com.example.E_commerce.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.E_commerce.Config.JwtUtil;
import java.util.List;
@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    @Autowired
    private JwtUtil jwtUtil;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    @GetMapping
    public List<Cart> getCart(@RequestParam Long userId){
        return cartService.getUserCart(userId);
    }
    @PostMapping
    public Cart addToCart(@RequestParam CartRequest request,
                          @RequestHeader("Authorization")String header) {
       String token = header.substring(7);
       String email=jwtUtil.extractEmail(token);
       return cartService.addToCart(email,request);
    }
    @PutMapping
    public Cart updateCart(@RequestParam Long userId, @RequestParam Long productId, @RequestParam
    int quantity) {
        return cartService.updateCart(userId, productId, quantity);
    }
    @DeleteMapping
    public void deleteCart(@RequestParam Long userId, @RequestParam
    Long productId) {
        cartService.deleteCartitems(userId,productId);

    }
}
//    @DeleteMapping("/delete")
//    public String deleteCart(@RequestParam Long cartId){
//        return cartService.deleteCartItem(cartId);
//    }
//    @GetMapping
//    public List<Cart>getCart(@RequestParam Long userId){
//        return cartService.getUserCart(userId);
//    }
//@PutMapping("/update")
//    public Cart updateCart(@RequestParam Long cartId, @RequestParam
//int quanity){
//       return cartService.updateQuantity(cartId,quanity);}


