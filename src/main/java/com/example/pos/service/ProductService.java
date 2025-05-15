package com.example.pos.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.pos.dto.ResponseData;
import com.example.pos.dto.product.CreateProductDTO;
import com.example.pos.dto.product.ProductOutputDTO;
import com.example.pos.dto.product.UpdateProductDTO;
import com.example.pos.model.Product;
import com.example.pos.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public ResponseData<Product> createProductService(CreateProductDTO productDTO) {
        try {
            if (productDTO == null || productDTO.getName().isEmpty() || productDTO.getCategoryId().isEmpty() ||
                    Objects.isNull(productDTO.getPrice())) {
                return new ResponseData<Product>(1, "Invalid product data", (Product) null);
            }

            Product product = new Product(null, productDTO.getName(), productDTO.getCategoryId(), productDTO.getPrice(),
                    productDTO.getImage());

            Product savedProduct = productRepository.save(product);

            return new ResponseData<Product>(0, "Create new Product success", savedProduct);
        } catch (Exception e) {
            return new ResponseData<Product>(2, "Error creating product: " + e.getMessage(), (Product) null);
        }
    }

    public ResponseData<Page<ProductOutputDTO>> getProducts(int page, int size, String sortBy, String direction,
            String search) {
        Sort sort = Sort.by(direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, sort);
        Page<Product> productPage;

        if (search != null && !search.trim().isEmpty()) {
            productPage = productRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        Page<ProductOutputDTO> productDTOPage = productPage.map(product -> {
            String categoryName = null;
            if (product.getCategory() != null) {
                categoryName = product.getCategory().getValue();
            }
            return new ProductOutputDTO(
                    product.getId(),
                    product.getName(),
                    product.getImage(),
                    product.getPrice(),
                    categoryName,
                    product.getCategoryId());
        });

        return new ResponseData<Page<ProductOutputDTO>>(0, "success", productDTOPage);
    }

    public ResponseData<List<ProductOutputDTO>> getProductByCategoryService(String categoryId) {
        try {
            // Lấy danh sách sản phẩm theo category (ví dụ: "coffee")
            List<Product> products = productRepository.findByCategoryId(categoryId);

            // Chuyển đổi từ entity sang DTO
            List<ProductOutputDTO> dtoList = new ArrayList<>();
            for (Product product : products) {
                String categoryName = null;
                if (product.getCategory() != null) {
                    categoryName = product.getCategory().getValue();
                }

                ProductOutputDTO dto = new ProductOutputDTO(
                        product.getId(),
                        product.getName(),
                        product.getImage(),
                        product.getPrice(),
                        categoryName,
                        product.getCategoryId());
                dtoList.add(dto);
            }
            return new ResponseData<List<ProductOutputDTO>>(0, "success", dtoList);
        } catch (Exception e) {
            return new ResponseData<List<ProductOutputDTO>>(1, "error from server: " + e.getMessage(),(List<ProductOutputDTO>) null);
        }
    }

    public ResponseData<List<ProductOutputDTO>> getAllProductService(String search) {
        try {
            List<Product> products = new ArrayList<>();
            if (search != null && !search.trim().isEmpty()) {
                products = productRepository.findByNameContaining(search);
            } else {
                products = productRepository.findAll();
            }

            List<ProductOutputDTO> productOutputDTOList = new ArrayList<>();

            for (Product product : products) {
                String categoryName = null;

                if (product.getCategory() != null) {
                    categoryName = product.getCategory().getValue(); // Lấy tên danh mục, ví dụ "coffee"
                }
                ProductOutputDTO dto = new ProductOutputDTO(product.getId(), product.getName(), product.getImage(),
                        product.getPrice(), categoryName, product.getCategoryId());
                productOutputDTOList.add(dto);
            }
            return new ResponseData<List<ProductOutputDTO>>(0, "success", productOutputDTOList);
        } catch (Exception e) {
            return new ResponseData<List<ProductOutputDTO>>(0, "error from server" + e.getMessage(),(List<ProductOutputDTO>) null);
        }
    }

    public ResponseData<Product> updateProductService(UpdateProductDTO dto) {
        try {
            Boolean checkData = productRepository.existsById(dto.getId());
            if (checkData == true) {
                Product product = productRepository.findById(dto.getId()).orElse(null);
                product.setName(dto.getName());
                product.setCategoryId(dto.getCategoryId());
                product.setPrice(dto.getPrice());
                product.setImage(dto.getImage());
                Product updatedProduct = productRepository.save(product);
                return new ResponseData<Product>(0, "Update product success", updatedProduct);
            } else {
                return new ResponseData<Product>(1, "Product not found", (Product) null);
            }
        } catch (Exception e) {
            return new ResponseData<Product>(0, "error from server" + e.getMessage(), (Product) null);
        }
    }

    public ResponseData<Product> deleteProductService(Long id) {
        try {
            Boolean checkData = productRepository.existsById(id);
            if (checkData == true) {
                productRepository.deleteById(id);
                return new ResponseData<Product>(0, "delete product success", (Product) null);
            } else {
                return new ResponseData<Product>(1, "Product not found", (Product) null);
            }
        } catch (Exception e) {
            return new ResponseData<Product>(0, "error from server" + e.getMessage(),(Product) null);
        }
    }

}
