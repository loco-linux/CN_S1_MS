package com.example.boletas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.boletas.model.Boleta;

public interface BoletaRepository extends JpaRepository<Boleta, Long> {
}
