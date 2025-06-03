package com.example.tienda.boletas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping
    public List <Boleta> listarBoletas(){
        return boletaService.listarTodas();
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
