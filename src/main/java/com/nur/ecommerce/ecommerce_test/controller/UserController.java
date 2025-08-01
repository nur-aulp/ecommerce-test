package com.nur.ecommerce.ecommerce_test.controller;

import com.nur.ecommerce.ecommerce_test.dto.UserDTO;
import com.nur.ecommerce.ecommerce_test.entity.User;
import com.nur.ecommerce.ecommerce_test.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private UserDTO mapToUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        // Perbaikan: Mapping ke field yang benar
        dto.setAddress(user.getAddress());
        dto.setPhone(user.getPhone());
        return dto;
    }
    
    private User mapToUserEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        // Perbaikan: Mapping ke field yang benar
        user.setAddress(dto.getAddress());
        user.setPhone(dto.getPhone());
        return user;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOs = users.stream()
                .map(this::mapToUserDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> new ResponseEntity<>(mapToUserDTO(user), HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        User newUser = mapToUserEntity(userDTO);
        newUser.setPasswordHash("dummy_hashed_password");
        newUser.setCreatedAt(LocalDateTime.now());
        User createdUser = userService.createUser(newUser);
        return new ResponseEntity<>(mapToUserDTO(createdUser), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        User existingUser = userService.getUserById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setFullName(userDTO.getFullName());
        // Perbaikan: Mapping ke field yang benar
        existingUser.setAddress(userDTO.getAddress());
        existingUser.setPhone(userDTO.getPhone());
        existingUser.setUpdatedAt(LocalDateTime.now());
        
        User updatedUser = userService.updateUser(existingUser);
        return new ResponseEntity<>(mapToUserDTO(updatedUser), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}