package com.mawulidev.week5labs.services.implementions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mawulidev.week5labs.dtos.ProductDTO;
import com.mawulidev.week5labs.exceptions.EntityNotFoundException;
import com.mawulidev.week5labs.models.Product;
import com.mawulidev.week5labs.repositories.ProductRepository;
import com.mawulidev.week5labs.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link ProductService} interface, which provides
 * business logic for managing products.
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ObjectMapper mapper;

    /**
     * Retrieves all products from the database and converts them to a list of {@link ProductDTO}.
     *
     * @return A list of {@link ProductDTO} objects representing all products in the system.
     */
    @Override
    public List<ProductDTO> getAllProduct() {
        return productRepository.findAll().stream().map(product -> mapper.convertValue(product, ProductDTO.class)).toList();
    }

    /**
     * Adds a new product to the database.
     *
     * @param productDTO The {@link ProductDTO} object containing product details.
     * @return The {@link ProductDTO} object of the newly added product.
     */
    @Override
    public ProductDTO addProduct(ProductDTO productDTO) {
        Product product = productRepository.save(mapper.convertValue(productDTO, Product.class));
        return mapper.convertValue(product, ProductDTO.class);
    }

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id The unique identifier of the product.
     * @return A {@link ProductDTO} object representing the found product.
     * @throws EntityNotFoundException if no product is found with the given ID.
     */
    @Override
    public ProductDTO getProductById(String id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product does not exist"));
        return mapper.convertValue(product, ProductDTO.class);
    }

    /**
     * Updates the details of an existing product.
     *
     * @param id         The unique identifier of the product to update.
     * @param productDTO The {@link ProductDTO} containing the new product details.
     * @return A message indicating the successful update of the product.
     * @throws EntityNotFoundException if no product is found with the given ID.
     */
    @Override
    public String updateProduct(String id, ProductDTO productDTO) {
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product does not exist"));
        product.setName(productDTO.getName());
        product.setPrice(product.getPrice());
        productRepository.save(product);
        return "Product successfully updated";
    }

    /**
     * Deletes a product by its unique identifier.
     *
     * @param id The unique identifier of the product to delete.
     * @return A message indicating the successful deletion of the product.
     * @throws EntityNotFoundException if no product is found with the given ID.
     */
    @Override
    public String deleteProductById(String id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product does not exist"));
        productRepository.delete(product);
        return "Product successfully deleted";
    }
}
