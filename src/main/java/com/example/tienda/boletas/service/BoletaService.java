package com.example.tienda.boletas.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.tienda.boletas.dto.CarritoCompraRequest;
import com.example.tienda.boletas.model.Boleta;
import com.example.tienda.boletas.model.DetalleBoleta;
import com.example.tienda.boletas.repository.BoletaRepository;
import com.example.tienda.productos.service.ProductService;

@Service
public class BoletaService {

    private final BoletaRepository boletaRepository;
    private final ProductService productoService; // Ya lo tienes

    public BoletaService(BoletaRepository boletaRepository, ProductService productoService) {
        this.boletaRepository = boletaRepository;
        this.productoService = productoService;
    }

    public Boleta crearBoleta(CarritoCompraRequest request) {
        Boleta boleta = new Boleta();
        boleta.setFecha(LocalDateTime.now());

        List<DetalleBoleta> detalles = request.getProductos().stream().map(item -> {
            var producto = productoService.findProductoById(item.getProductoId());
            DetalleBoleta detalle = new DetalleBoleta();
            detalle.setProductoId(producto.getId());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(BigDecimal.valueOf(producto.getPrecio())); 
            detalle.setBoleta(boleta);
            return detalle;
        }).collect(Collectors.toList());
        

        boleta.setDetalles(detalles);

        BigDecimal total = detalles.stream()
                .map(d -> d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        boleta.setTotal(total);

        return boletaRepository.save(boleta);
    }
}
