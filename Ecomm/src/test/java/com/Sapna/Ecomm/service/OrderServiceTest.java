package com.Sapna.Ecomm.service;
import com.Sapna.Ecomm.dto.OrderDTO;
import com.Sapna.Ecomm.model.OrderItem;
import com.Sapna.Ecomm.model.Orders;
import com.Sapna.Ecomm.model.Product;
import com.Sapna.Ecomm.model.User;
import com.Sapna.Ecomm.repo.OrderRepository;
import com.Sapna.Ecomm.repo.ProductRepository;
import com.Sapna.Ecomm.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testPlaceOrderSucess(){
        User user=new User();
        user.setId(1L);
        user.setName("Sapna");
        user.setEmail("Sapna@gmail.com");

        Product product=new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(50000.0);

        Map<Long,Integer> productquantities =new HashMap<>();
        productquantities.put(1L,2);

        Orders savedOrder=new Orders();
        savedOrder.setId(100L);
        savedOrder.setUser(user);
        savedOrder.setStatus("pending");
        savedOrder.setTotalAmount(100000.0);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(2);
        orderItem.setOrder(savedOrder);

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        savedOrder.setOrderItems(orderItems);

        when(userRepository.findByEmail("Sapna@gmail.com"))
                .thenReturn(user);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(orderRepository.save(any(Orders.class)))
                .thenReturn(savedOrder);

        OrderDTO result=orderService.placeOrder(
                "Sapna@gmail.com",productquantities);

        assertNotNull(result);
        assertEquals(100L,result.getId());
        assertEquals(100000.0,result.getTotalAmount());
        assertEquals("pending",result.getStatus());

        verify(userRepository, times(1))
                .findByEmail("Sapna@gmail.com");

        verify(productRepository, times(1))
                .findById(1L);

        verify(orderRepository, times(1))
                .save(any(Orders.class));
    }
    @Test
    void testGetOrdersByEmail() {
        User user = new User();
        user.setId(1L);
        user.setName("Sapna");
        user.setEmail("Sapna@gmail.com");

        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(50000.0);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(2);

        Orders order = new Orders();
        order.setId(100L);
        order.setUser(user);
        order.setStatus("pending");
        order.setTotalAmount(100000.0);
        order.setOrderItems(List.of(orderItem));

        when(userRepository.findByEmail("Sapna@gmail.com"))
                .thenReturn(user);

        when(orderRepository.findByUser(user))
                .thenReturn(List.of(order));

        List<OrderDTO> result =
                orderService.getOrdersByEmail("Sapna@gmail.com");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100000.0,
                result.get(0).getTotalAmount());

        verify(userRepository, times(1))
                .findByEmail("Sapna@gmail.com");

        verify(orderRepository, times(1))
                .findByUser(user);
    }
    @Test
    void testGetAllOrders() {

        User user = new User();
        user.setName("Sapna");
        user.setEmail("Sapna@gmail.com");

        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(50000.0);

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(2);

        Orders order = new Orders();
        order.setId(100L);
        order.setUser(user);
        order.setStatus("pending");
        order.setTotalAmount(100000.0);
        order.setOrderItems(List.of(orderItem));

        when(orderRepository.findAllOrdersWithUsers())
                .thenReturn(List.of(order));
        List<OrderDTO> result =
                orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Sapna",
                result.get(0).getUserName());

        verify(orderRepository, times(1))
                .findAllOrdersWithUsers();
    }
    @Test
    void testDeleteOrder() {
        Long orderId = 1L;
        orderService.deleteOrder(orderId);
        verify(orderRepository, times(1))
                .deleteById(orderId);
    }
    @Test
    void testDeleteAllOrders() {
        orderService.deleteAllOrders();
        verify(orderRepository, times(1))
                .deleteAll();
    }
}
