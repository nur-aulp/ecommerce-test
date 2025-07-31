package com.nur.ecommerce.ecommerce_test.service;

import com.nur.ecommerce.ecommerce_test.entity.Cart;
import com.nur.ecommerce.ecommerce_test.entity.CartItem;
import com.nur.ecommerce.ecommerce_test.entity.Product;
import com.nur.ecommerce.ecommerce_test.entity.User;
import com.nur.ecommerce.ecommerce_test.repository.CartRepository;
import com.nur.ecommerce.ecommerce_test.repository.CartItemRepository;
import com.nur.ecommerce.ecommerce_test.repository.ProductRepository;
import com.nur.ecommerce.ecommerce_test.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
                       ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Cart getCartByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart(user);
                    return cartRepository.save(newCart);
                });
    }

    @Transactional
    public Cart addItemToCart(Long userId, CartItem cartItem) {
        Cart cart = getCartByUserId(userId);
        
        Product product = productRepository.findById(cartItem.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + cartItem.getQuantity();

            // Memvalidasi stok saat kuantitas diperbarui
            if (newQuantity > product.getStock()) {
                throw new RuntimeException("Insufficient stock for product " + product.getName() +
                                           ". Available: " + product.getStock());
            }

            item.setQuantity(newQuantity);
        } else {
            // Memvalidasi stok saat item baru ditambahkan
            if (cartItem.getQuantity() > product.getStock()) {
                throw new RuntimeException("Insufficient stock for product " + product.getName() +
                                           ". Available: " + product.getStock());
            }
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cart.getItems().add(cartItem);
        }
        
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateCartItem(Long userId, CartItem cartItem) {
        Cart cart = getCartByUserId(userId);
        
        Product product = productRepository.findById(cartItem.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Validasi stok untuk kuantitas yang baru
        if (cartItem.getQuantity() > product.getStock()) {
            throw new RuntimeException("Insufficient stock for product " + product.getName() +
                                       ". Available: " + product.getStock());
        }

        CartItem item = cart.getItems().stream()
                .filter(ci -> ci.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        item.setQuantity(cartItem.getQuantity());
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeItemFromCart(Long userId, Long itemId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        cartItemRepository.deleteById(itemId);
        
        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
