package com.nur.ecommerce.ecommerce_test.controller;

import com.nur.ecommerce.ecommerce_test.dto.OrderDTO;
import com.nur.ecommerce.ecommerce_test.dto.OrderItemDTO;
import com.nur.ecommerce.ecommerce_test.entity.Order;
import com.nur.ecommerce.ecommerce_test.entity.OrderItem;
import com.nur.ecommerce.ecommerce_test.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Metode helper untuk mapping
    private OrderDTO mapToOrderDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setPaymentMethod(order.getPaymentMethod());
        List<OrderItemDTO> items = order.getOrderItems().stream()
                                        .map(this::mapToOrderItemDTO)
                                        .collect(Collectors.toList());
        dto.setItems(items);
        return dto;
    }

    private OrderItemDTO mapToOrderItemDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProductName());
        dto.setPrice(item.getPrice());
        dto.setQuantity(item.getQuantity());
        return dto;
    }

    @PostMapping("/place/{userId}")
    public ResponseEntity<OrderDTO> placeOrder(@PathVariable Long userId,
                                                @RequestParam String shippingAddress,
                                                @RequestParam String paymentMethod) {
        Order newOrder = orderService.placeOrder(userId, shippingAddress, paymentMethod);
        return new ResponseEntity<>(mapToOrderDTO(newOrder), HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        List<OrderDTO> orderDTOs = orders.stream()
                                         .map(this::mapToOrderDTO)
                                         .collect(Collectors.toList());
        return new ResponseEntity<>(orderDTOs, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId)
                           .map(order -> new ResponseEntity<>(mapToOrderDTO(order), HttpStatus.OK))
                           .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long orderId) {
        Order cancelledOrder = orderService.cancelOrder(orderId);
        return new ResponseEntity<>(mapToOrderDTO(cancelledOrder), HttpStatus.OK);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long orderId, @RequestParam String newStatus) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
        return new ResponseEntity<>(mapToOrderDTO(updatedOrder), HttpStatus.OK);
    }
}
