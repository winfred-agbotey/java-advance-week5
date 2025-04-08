package com.mawulidev.week5labs.services;

import com.mawulidev.week5labs.dtos.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProduct();

    ProductDTO addProduct(ProductDTO productDTO);

    ProductDTO getProductById(String id);
    String updateProduct(String id, ProductDTO productDTO);
    String deleteProductById(String id);
}
