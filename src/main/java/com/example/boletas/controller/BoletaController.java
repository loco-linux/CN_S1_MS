package com.example.boletas.controller;

import org.springframework.web.bind.annotation.*;

import com.example.boletas.dto.CarritoCompraRequest;
import com.example.boletas.model.Boleta;
import com.example.boletas.service.BoletaService;

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
