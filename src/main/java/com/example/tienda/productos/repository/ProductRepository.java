package com.example.tienda.productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tienda.productos.model.Producto;

public interface ProductRepository extends JpaRepository<Producto, Long> {
}
