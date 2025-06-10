package com.example.tienda.facturas.service;


import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.tienda.service.AwsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FacturaServiceImpl implements FacturaService {

    private final AwsService awsService;

    @Override
    public String subirFactura(String clienteId, MultipartFile archivo) {
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String path = clienteId + "/" + fecha;

        try {
            return awsService.uploadFile(path, archivo);
        } catch (IOException e) {
            throw new RuntimeException("Error al subir la factura del cliente: " + clienteId, e);
        }
    }   

    @Override
    public List<String> listarFacturasPorCliente(String clienteId) {
        return awsService.listarFacturasPorCliente(clienteId);
    }


}
