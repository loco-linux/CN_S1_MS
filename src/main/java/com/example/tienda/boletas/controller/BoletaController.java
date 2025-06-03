package com.example.tienda.boletas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tienda.boletas.dto.CarritoCompraRequest;
import com.example.tienda.boletas.model.Boleta;
import com.example.tienda.boletas.service.BoletaService;

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

    // GET de prueba para verificar que BoletaController responde
    @GetMapping
    public String mensajePrueba() {
        return "Boletas GET";
    }
}
