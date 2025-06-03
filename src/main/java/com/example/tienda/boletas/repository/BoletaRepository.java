package com.example.tienda.boletas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tienda.boletas.model.Boleta;

public interface BoletaRepository extends JpaRepository<Boleta, Long> {
}
