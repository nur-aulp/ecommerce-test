package com.nur.ecommerce.ecommerce_test.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler.java
 *
 * Kelas ini berfungsi sebagai penangkap exception (kesalahan)
 * secara global di seluruh aplikasi.
 * Dengan @ControllerAdvice, kita bisa mengelola respons
 * HTTP untuk setiap jenis exception yang terjadi,
 * sehingga API selalu mengembalikan respons JSON yang konsisten.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Metode untuk menangani semua RuntimeException.
     * @ExceptionHandler(RuntimeException.class) akan mendengarkan
     * semua exception yang merupakan turunan dari RuntimeException.
     *
     * @param ex Exception yang terjadi
     * @return ResponseEntity yang berisi pesan error dalam format JSON
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        // Buat map untuk menampung pesan error
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", java.time.LocalDateTime.now());
        body.put("message", ex.getMessage());

        // Mengembalikan respons dengan status code 404 Not Found dan body JSON
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
