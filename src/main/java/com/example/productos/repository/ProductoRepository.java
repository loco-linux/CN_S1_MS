package com.example.productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.productos.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
