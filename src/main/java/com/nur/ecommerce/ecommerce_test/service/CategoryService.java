package com.nur.ecommerce.ecommerce_test.service;

import com.nur.ecommerce.ecommerce_test.entity.Category;
import com.nur.ecommerce.ecommerce_test.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // Injeksi dependensi CategoryRepository melalui constructor
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Mendapatkan semua kategori
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Mendapatkan kategori berdasarkan ID
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    // Menyimpan atau memperbarui kategori
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Menghapus kategori berdasarkan ID
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
