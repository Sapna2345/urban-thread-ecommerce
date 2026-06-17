package com.example.E_commerce.Service;

import com.example.E_commerce.Entity.Cart;
import com.example.E_commerce.Entity.Order;
import com.example.E_commerce.Entity.OrderItem;
import com.example.E_commerce.Entity.User;
import com.example.E_commerce.Repository.CartRepository;
import com.example.E_commerce.Repository.OrderRepository;
import com.example.E_commerce.Repository.UserRepository;
import jakarta.persistence.OneToMany;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    public OrderService(OrderRepository orderRepository,CartRepository cartRepository
    ,UserRepository userRepository){
        this.orderRepository= orderRepository;
        this.cartRepository=cartRepository;
        this.userRepository=userRepository;
    }
    public List<Order> getAllOrders(){
        return  orderRepository.findAll();
    }
    public List<Order> getOrdersByUser(Long userId){
        return orderRepository.findByUser_Id(userId);
    }
    public Order placeOrder(Long userId){
        // get user
        User user = userRepository.findById(userId).orElse(null);

        // create order FIRST ✅
        Order order = new Order();
        order.setUser(user);

        // get cart items
        List<Cart> cartItems = cartRepository.findByUser_Id(userId);
        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;
        System.out.println("cart size : " + cartItems.size());
        for (Cart cart : cartItems) {

            OrderItem item = new OrderItem();

            item.setProduct(cart.getProduct());
            item.setQuanity(cart.getQuantity());
            item.setPrice(cart.getProduct().getPrice());// ✅ now order exists

            total += cart.getProduct().getPrice() * cart.getQuantity();
            item.setOrder(order);
            orderItems.add(item);
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);

        // save order
        Order savedOrder = orderRepository.save(order);

        // clear cart
        cartRepository.deleteAll(cartItems);

        return savedOrder;

    }
    }


