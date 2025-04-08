package com.mawulidev.week5labs.controllers;

import com.mawulidev.week5labs.dtos.ProductDTO;
import com.mawulidev.week5labs.dtos.ResponseHandler;
import com.mawulidev.week5labs.dtos.UserUpdateRequestDTO;
import com.mawulidev.week5labs.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @Secured("ADMIN")
    @PostMapping("/v1/products")
    public ResponseEntity<Object> addProduct(@RequestBody ProductDTO productDTO) {
        return ResponseHandler.successResponse(HttpStatus.CREATED, productService.addProduct(productDTO));
    }

    @Secured("ADMIN")
    @GetMapping("/v1/products")
    public ResponseEntity<Object> getAllProduct() {
        return ResponseHandler.successResponse(HttpStatus.OK, productService.getAllProduct());
    }

    @Secured("ADMIN")
    @GetMapping("/v1/products/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable String id) {
        return ResponseHandler.successResponse(HttpStatus.OK, productService.getProductById(id));
    }

    @Secured("ADMIN")
    @PutMapping("/v1/products/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable String id, @RequestBody ProductDTO productDTO) {
        return ResponseHandler.successResponse(HttpStatus.OK, productService.updateProduct(id, productDTO));
    }

    @Secured("ADMIN")
    @DeleteMapping("/v1/products/{id}")
    public ResponseEntity<Object> deleteProductById(@PathVariable String id) {
        return ResponseHandler.successResponse(HttpStatus.OK, productService.deleteProductById(id));
    }
}