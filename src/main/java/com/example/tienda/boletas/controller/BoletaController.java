package com.example.tienda.boletas.controller;

import java.util.List;


import org.springframework.web.bind.annotation.*;

import com.example.tienda.boletas.dto.CarritoCompraRequest;
import com.example.tienda.boletas.model.Boleta;
import com.example.tienda.boletas.service.BoletaService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/v1/boletas")
public class BoletaController {

    private final BoletaService boletaService;

    public BoletaController(BoletaService boletaService) {
        this.boletaService = boletaService;
    }

    // POST para generar boleta
    @PostMapping
    public Boleta generarBoleta(@RequestBody CarritoCompraRequest request) {
        return boletaService.crearBoleta(request);
    }

    // GET paginado
    @GetMapping
    public Page<Boleta> listarBoletas(Pageable pageable) {
        return boletaService.listarBoletasPaginadas(pageable);
    }
    
    @GetMapping("/{id}")
    public Boleta obtenerBoleta(@PathVariable Long id){
        return boletaService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public void eliminarBoleta(@PathVariable Long id){
        boletaService.eliminarPorId(id);
    }

    // GET de prueba para verificar que BoletaController responde
    @GetMapping("/ping")
    public String mensajePrueba() {
        return "Boletas GET";
    }
}
