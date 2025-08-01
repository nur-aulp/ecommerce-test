package com.nur.ecommerce.ecommerce_test.service;

import com.nur.ecommerce.ecommerce_test.entity.*;
import com.nur.ecommerce.ecommerce_test.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                        CartService cartService, ProductRepository productRepository,
                        TransactionRepository transactionRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.productRepository = productRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Order placeOrder(Long userId, String shippingAddress, String paymentMethod) {
        // 1. Dapatkan keranjang pengguna
        Cart cart = cartService.getCartByUserId(userId);
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty, cannot place an order.");
        }

        // 2. Validasi stok untuk setiap item di keranjang
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            if (cartItem.getQuantity() > product.getStock()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        // 3. Buat objek Order
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setStatus("pending");
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(paymentMethod);
        
        // 4. Buat OrderItem dari CartItem
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setProductName(cartItem.getProduct().getName());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            order.addOrderItem(orderItem);
        }

        // 5. Kurangi stok produk
        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }

        // 6. Simpan Order
        Order savedOrder = orderRepository.save(order);

        // 7. Bersihkan keranjang pengguna setelah order berhasil
        cartService.clearCart(userId);

        // 8. Buat entri transaksi (dengan status pending)
        Transaction transaction = new Transaction();
        transaction.setOrder(savedOrder);
        transaction.setAmount(totalAmount);
        transaction.setPaymentStatus("pending");
        transactionRepository.save(transaction);
        
        return savedOrder;
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
    
    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }
    
    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        
        // Cek apakah order masih bisa dibatalkan (misalnya, statusnya masih "pending")
        if (!"pending".equals(order.getStatus())) {
            throw new RuntimeException("Order cannot be cancelled because its status is " + order.getStatus());
        }
        
        // Kembalikan stok produk yang sudah dikurangi
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }
        
        order.setStatus("cancelled");
        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        // Tambahkan validasi jika diperlukan
        if (newStatus == null || newStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("New status cannot be null or empty.");
        }
        
        if ("cancelled".equals(order.getStatus())) {
             throw new RuntimeException("Cannot update status of a cancelled order.");
        }

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
}
