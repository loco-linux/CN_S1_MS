package com.example.tienda.boletas.repository;

import com.example.tienda.boletas.model.Boleta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoletaRepository extends JpaRepository<Boleta, Long> {
    Page<Boleta> findAll(Pageable pageable); // Agregado
}
