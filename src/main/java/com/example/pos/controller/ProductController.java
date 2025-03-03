package com.example.pos.controller;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos.dto.RefreshTokenRequest;
import com.example.pos.dto.ResponseData;
import com.example.pos.dto.product.CreateProductDTO;
import com.example.pos.dto.product.UpdateProductDTO;
import com.example.pos.model.Product;
import com.example.pos.service.ProductService;
import com.example.pos.service.RefreshTokenService;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @PreAuthorize("hasAuthority('R1')")
    @PostMapping("/create-product")
    public ResponseEntity<ResponseData<?>> createProduct(@RequestBody CreateProductDTO productDTO) {
        ResponseData<Product> response = productService.createProductService(productDTO);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('R1')")
    @GetMapping("/get-page-product")
    public ResponseEntity<ResponseData<?>> getProducts(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "") String search) {
        ResponseData<?> response = productService.getProducts(page, size, sortBy, direction,search);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('R1')")
    @PutMapping("/update-product")
    public ResponseEntity<ResponseData<Product>> updateProduct(@RequestBody UpdateProductDTO dto) {
        ResponseData<Product> response = productService.updateProductService(dto);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAuthority('R1')")
    @DeleteMapping("/delete-product")
    public ResponseEntity<ResponseData<Product>> deleteProduct(@RequestParam Long id) {
        ResponseData<Product> response = productService.deleteProductService(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllProduct")
    public ResponseEntity<ResponseData<?>> getAllProduct(@RequestParam String search) {
        ResponseData<?> response = productService.getAllProductService(search);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-Product-category")
    public ResponseEntity<ResponseData<?>> getProductByCategory(@RequestParam String category) {
        ResponseData<?> response = productService.getProductByCategoryService(category);
        return ResponseEntity.ok(response);
    }

  
}
