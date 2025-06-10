package com.example.tienda.facturas.service;


import org.springframework.web.multipart.MultipartFile;

public interface FacturaService {
    String subirFactura(String clienteId, MultipartFile archivo);
}
