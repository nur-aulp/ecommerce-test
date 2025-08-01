package com.nur.ecommerce.ecommerce_test.repository;

import com.nur.ecommerce.ecommerce_test.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
