package com.nur.ecommerce.ecommerce_test.controller;

import com.nur.ecommerce.ecommerce_test.dto.CartDTO;
import com.nur.ecommerce.ecommerce_test.dto.CartItemDTO;
import com.nur.ecommerce.ecommerce_test.entity.Cart;
import com.nur.ecommerce.ecommerce_test.entity.CartItem;
import com.nur.ecommerce.ecommerce_test.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Mapping entity Cart ke CartDTO
    private CartDTO mapToCartDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setUserId(cart.getUser().getId());
        cartDTO.setCreatedAt(cart.getCreatedAt());
        cartDTO.setItems(cart.getItems().stream()
                .map(this::mapToCartItemDTO)
                .collect(Collectors.toList()));
        return cartDTO;
    }

    // Mapping entity CartItem ke CartItemDTO
    private CartItemDTO mapToCartItemDTO(CartItem cartItem) {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setId(cartItem.getId());
        cartItemDTO.setProductId(cartItem.getProduct().getId());
        cartItemDTO.setProductName(cartItem.getProduct().getName());
        cartItemDTO.setQuantity(cartItem.getQuantity());
        return cartItemDTO;
    }

    // Mendapatkan keranjang belanja berdasarkan ID pengguna
    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCartByUserId(@PathVariable Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return new ResponseEntity<>(mapToCartDTO(cart), HttpStatus.OK);
    }

    // Menambahkan item ke keranjang
    @PostMapping("/{userId}/add") // Dan ini
    public ResponseEntity<CartDTO> addItemToCart(@PathVariable Long userId, @RequestBody CartItem cartItem) {
        Cart updatedCart = cartService.addItemToCart(userId, cartItem);
        return new ResponseEntity<>(mapToCartDTO(updatedCart), HttpStatus.CREATED);
    }

    // Memperbarui kuantitas item
    @PutMapping("/{userId}/update")
    public ResponseEntity<CartDTO> updateCartItem(@PathVariable Long userId, @RequestBody CartItem cartItem) {
        Cart updatedCart = cartService.updateCartItem(userId, cartItem);
        return new ResponseEntity<>(mapToCartDTO(updatedCart), HttpStatus.OK);
    }

    // Menghapus item dari keranjang
    @DeleteMapping("/{userId}/item/{itemId}/remove")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable Long userId, @PathVariable Long itemId) {
        cartService.removeItemFromCart(userId, itemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Mengosongkan keranjang
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
