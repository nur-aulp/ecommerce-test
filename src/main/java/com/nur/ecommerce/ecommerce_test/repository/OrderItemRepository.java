package com.nur.ecommerce.ecommerce_test.repository;

import com.nur.ecommerce.ecommerce_test.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
