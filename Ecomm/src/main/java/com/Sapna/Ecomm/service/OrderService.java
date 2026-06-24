package com.Sapna.Ecomm.service;

import com.Sapna.Ecomm.dto.OrderDTO;
import com.Sapna.Ecomm.dto.OrderItemDTO;
import com.Sapna.Ecomm.model.OrderItem;
import com.Sapna.Ecomm.model.Orders;
import com.Sapna.Ecomm.model.Product;
import com.Sapna.Ecomm.model.User;
import com.Sapna.Ecomm.repo.OrderRepository;
import com.Sapna.Ecomm.repo.ProductRepository;
import com.Sapna.Ecomm.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
  private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
  private OrderRepository orderRepository;

    public OrderDTO placeOrder(
            String email,
            Map<Long, Integer> productQuantities) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (productQuantities == null || productQuantities.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Orders order=new Orders();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus("pending");

        double totalAmount = 0;
        List<OrderItem> orderItems=new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : productQuantities.entrySet()) {
            Integer quantity = entry.getValue();

            if (quantity == null || quantity <= 0) {
                throw new RuntimeException("Quantity must be positive");
            }

            Product product = productRepository.findById(entry.getKey())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            totalAmount+= product.getPrice() * quantity;

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);

            orderItems.add(orderItem);
        }

        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);

        Orders savedOrder = orderRepository.save(order);

        return covertToDTO(savedOrder);
    }

    public List<OrderDTO> getOrdersByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return orderRepository.findByUser(user)
                .stream()
                .map(this::covertToDTO)
                .collect(Collectors.toList());
    }
    public List<OrderDTO> getAllOrders() {
      List <Orders> orders= orderRepository.findAllOrdersWithUsers();
      return orders.stream().map(this::covertToDTO).collect(Collectors.toList());
    }
    private OrderDTO covertToDTO(Orders orders) {
        List<OrderItemDTO> orderItems= orders.getOrderItems().stream()
                .map(item -> new OrderItemDTO(
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity())).collect(Collectors.toList());
        return new OrderDTO(
                orders.getId(),
                orders.getTotalAmount(),
                orders.getStatus(),
                orders.getOrderDate(),
               orders.getUser()!=null ? orders.getUser().getName() : "Unknown",
               orders.getUser()!=null ? orders.getUser().getEmail() : "Unknown",
                orderItems
        );
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }
}
















