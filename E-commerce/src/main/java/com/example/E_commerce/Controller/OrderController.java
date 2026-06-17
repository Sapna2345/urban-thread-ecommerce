package com.example.E_commerce.Controller;

import com.example.E_commerce.Entity.Order;
import com.example.E_commerce.Service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService){
        this.orderService=orderService;
    }
    @PostMapping
    public Order placeOrder(@RequestParam Long userId){
        return orderService.placeOrder(userId);
    }
    @GetMapping
    public List<Order>getAllOrders(){ return  orderService.getAllOrders();}
    @GetMapping("/user/{userId}")
    public List<Order>getOrderByUser(@PathVariable Long userId){
        return orderService.getOrdersByUser(userId);
    }
}
