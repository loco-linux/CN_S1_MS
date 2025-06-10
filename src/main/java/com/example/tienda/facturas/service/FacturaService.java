package com.example.tienda.facturas.service;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface FacturaService {
    String subirFactura(String clienteId, MultipartFile archivo);
    List<String> listarFacturasPorCliente(String clienteId);

}
