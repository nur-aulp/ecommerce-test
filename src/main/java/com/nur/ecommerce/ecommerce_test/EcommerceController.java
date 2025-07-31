package com.nur.ecommerce.ecommerce_test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EcommerceController {

    @GetMapping("/test")
    public String testEndpoint() {
        return "E-commerce service is running!";
    }

    
}