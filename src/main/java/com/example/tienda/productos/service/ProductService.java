package com.example.tienda.productos.service;

import org.springframework.stereotype.Service;

import com.example.tienda.productos.model.Producto;
import com.example.tienda.productos.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Producto findProductoById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }
}
