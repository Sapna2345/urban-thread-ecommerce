package com.example.E_commerce.Service;
import com.example.E_commerce.Controller.CartRequest;
import com.example.E_commerce.Entity.Cart;
import com.example.E_commerce.Entity.Product;
import com.example.E_commerce.Entity.User;
import com.example.E_commerce.Repository.CartRepository;
import com.example.E_commerce.Repository.ProductRepository;
import com.example.E_commerce.Repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    public CartService(CartRepository cartRepository,UserRepository userRepository,
                       ProductRepository productRepository){
        this.cartRepository=cartRepository;
        this.userRepository=userRepository;
        this.productRepository=productRepository;
    }
    public Cart addToCart(String email, CartRequest request){
        User user=userRepository.findByEmail(email);
        Product product=productRepository.findById(request.getProductId()).orElseThrow();
        Cart cart=new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(request.getQuantity());
        return cartRepository.save(cart);
    }
    public List<Cart> getUserCart(Long userId){
        return  cartRepository.findByUser_Id(userId);
    }
//    public String deleteCartItem(Long cartId){
//        cartRepository.deleteById(cartId);
//        return "Item removed from cart";
//    }
//    public  Cart updateQuantity(Long cartId,int quantity){
//        Cart cart=cartRepository.findById(cartId).orElse(null);
//        if (cart!=null){
//            cart.setQuantity(quantity);
//            return cartRepository.save(cart);
//        }
    public Cart updateCart(Long userId,Long productId,int quantity){
        Cart cart=cartRepository.findByUser_IdAndProduct_Id(userId,productId);
        if (cart==null){
            return null;
        }

        cart.setQuantity(cart.getQuantity()+quantity);

        return cartRepository.save(cart);
    }
    public void deleteCartitems(Long userId,Long productId){
        Cart cart=cartRepository.findByUser_IdAndProduct_Id(userId,productId);
        if(cart != null){
            cartRepository.delete(cart);
        }
    }
    public List<Cart> getCartByEmail(String email){

        User user=userRepository.findByEmail(email);
        return cartRepository.findByUser(user);
    }


}
