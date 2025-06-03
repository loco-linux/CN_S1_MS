package com.example.tienda.boletas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tienda.boletas.model.DetalleBoleta;

public interface DetalleBoletaRepository extends JpaRepository<DetalleBoleta, Long> {
}
