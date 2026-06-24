package com.Sapna.Ecomm.controller;

import com.Sapna.Ecomm.dto.OrderDTO;
import com.Sapna.Ecomm.model.OrderRequest;
import com.Sapna.Ecomm.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin("*")
@Tag(name = "Order APIs", description = "APIs for placing and managing orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    @Operation(
            summary = "Place an order",
            description = "Places a new order for the logged-in user"
    )
    public OrderDTO placeOrder(
            Authentication authentication,
            @RequestBody OrderRequest orderRequest) {

        String email = authentication.getName();

        return orderService.placeOrder(
                email,
                orderRequest.getProductQuantities()
        );
    }

    @GetMapping("/all-orders")
    @Operation(
            summary = "Get all orders",
            description = "Fetch all orders from the database"
    )
    public List<OrderDTO> getAllorders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/my")
    @Operation(
            summary = "Get current user's orders",
            description = "Returns all orders placed by the logged-in user"
    )
    public List<OrderDTO> getMyOrders(Authentication authentication) {
        return orderService.getOrdersByEmail(authentication.getName());
    }

    @DeleteMapping("/{orderId}")
    @Operation(
            summary = "Delete order by ID",
            description = "Deletes an order using its ID"
    )
     public String deleteOrder(@PathVariable Long orderId){
        orderService.deleteOrder(orderId);
        return "Order deleted!";
     }
    @DeleteMapping("/all")
    @Operation(
            summary = "Delete all orders",
            description = "Deletes all orders from the database"
    )
    public String deleteAllOrders(){
        orderService.deleteAllOrders();
        return "All Order deleted!";
    }
}
