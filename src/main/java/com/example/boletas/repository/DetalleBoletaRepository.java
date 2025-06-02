package com.example.boletas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.boletas.model.DetalleBoleta;

public interface DetalleBoletaRepository extends JpaRepository<DetalleBoleta, Long> {
}
