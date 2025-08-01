package com.nur.ecommerce.ecommerce_test.controller;

import com.nur.ecommerce.ecommerce_test.dto.CategoryDTO;
import com.nur.ecommerce.ecommerce_test.entity.Category;
import com.nur.ecommerce.ecommerce_test.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Helper method to map Category to CategoryDTO
    private CategoryDTO mapToCategoryDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(this::mapToCategoryDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(categoryDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(category -> new ResponseEntity<>(mapToCategoryDTO(category), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody Category category) {
        Category savedCategory = categoryService.saveCategory(category);
        return new ResponseEntity<>(mapToCategoryDTO(savedCategory), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryService.getCategoryById(id)
                .map(existingCategory -> {
                    existingCategory.setName(category.getName());
                    existingCategory.setDescription(category.getDescription());
                    Category updatedCategory = categoryService.saveCategory(existingCategory);
                    return new ResponseEntity<>(mapToCategoryDTO(updatedCategory), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
