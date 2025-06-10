package com.example.tienda.facturas.controller;

import com.example.tienda.service.AwsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/facturas")
@RequiredArgsConstructor
public class FacturaController {

    private final AwsService awsService = null;

/*  @PostMapping("/upload")
    public ResponseEntity<String> uploadFactura(
            @RequestParam("file") MultipartFile file,
            @RequestParam("clienteId") String clienteId) throws IOException {
        
        // Formato: cliente123/2024-06/factura123.pdf
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String nombre = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String s3Path = clienteId + "/" + fecha + "/" + nombre;

        String resultado = awsService.uploadFile(s3Path, file); // llama a tu método existente
        return ResponseEntity.ok("Factura subida correctamente: " + resultado);
    }*/

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFactura(
        @RequestParam("file") MultipartFile file,
        @RequestParam("clienteId") String clienteId) throws IOException {

        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String keyPath = clienteId + "/" + fecha; // ejemplo: cliente123/2024-06
        String resultado = awsService.uploadFile(keyPath, file);

        return ResponseEntity.ok("Factura subida correctamente a: " + keyPath + "/" + resultado);
    }

}
