package com.example.boletas.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boletas.dto.CarritoCompraRequest;
import com.example.boletas.model.Boleta;
import com.example.boletas.service.BoletaService;

@RestController
@RequestMapping("/boletas")
public class BoletaController {

    private final BoletaService boletaService;

    public BoletaController(BoletaService boletaService) {
        this.boletaService = boletaService;
    }

    @PostMapping
    public Boleta generarBoleta(@RequestBody CarritoCompraRequest request) {
        return boletaService.crearBoleta(request);
    }
}
