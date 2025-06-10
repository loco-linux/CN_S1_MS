package com.example.tienda.facturas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.tienda.facturas.service.FacturaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/facturas")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;

    @PostMapping("/upload")
    public ResponseEntity<String> subirFactura(@RequestParam("clienteId") String clienteId,
                                               @RequestParam("file") MultipartFile archivo) {
        try {
            String nombreArchivo = facturaService.subirFactura(clienteId, archivo);
            return ResponseEntity.ok("Factura subida correctamente: " + nombreArchivo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al subir factura: " + e.getMessage());
        }
    }

    @GetMapping("/historial")
    public ResponseEntity<List<String>> historialPorCliente(@RequestParam String clienteId) {
        try {
            List<String> archivos = facturaService.listarFacturasPorCliente(clienteId);
            return ResponseEntity.ok(archivos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(List.of("Error al obtener historial: " + e.getMessage()));
        }
    }


}
