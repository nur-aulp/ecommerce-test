package com.nur.ecommerce.ecommerce_test.dto;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    // Perbaikan: Ubah nama field untuk match SQL
    private String address;
    // Perbaikan: Ubah nama field untuk match SQL
    private String phone;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    // Perbaikan getter dan setter
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}