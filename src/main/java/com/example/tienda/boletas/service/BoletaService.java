package com.example.tienda.boletas.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tienda.boletas.dto.CarritoCompraRequest;
import com.example.tienda.boletas.model.Boleta;
import com.example.tienda.boletas.model.DetalleBoleta;
import com.example.tienda.boletas.repository.BoletaRepository;
import com.example.tienda.productos.service.ProductService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class BoletaService {

    private final BoletaRepository boletaRepository;
    private final ProductService productService;

    public BoletaService(BoletaRepository boletaRepository, ProductService productService) {
        this.boletaRepository = boletaRepository;
        this.productService = productService;
    }

    public Boleta crearBoleta(CarritoCompraRequest request) {
        Boleta boleta = new Boleta();
        boleta.setFecha(LocalDateTime.now());

        List<DetalleBoleta> detalles = request.getProductos().stream().map(item -> {
            var producto = productService.findProductoById(item.getProductoId());
            DetalleBoleta detalle = new DetalleBoleta();
            detalle.setProductoId(producto.getId());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(BigDecimal.valueOf(producto.getPrecio()));
            detalle.setBoleta(boleta);
            return detalle;
        }).toList();

        boleta.setDetalles(detalles);

        BigDecimal total = detalles.stream()
                .map(d -> d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        boleta.setTotal(total);

        return boletaRepository.save(boleta);
    }

    public List<Boleta> listarTodas() {
        return boletaRepository.findAll();
    }

    public Boleta buscarPorId(Long id) {
        return boletaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Boleta no encontrada con ID: " + id));
    }

    public void eliminarPorId(Long id) {
        boletaRepository.deleteById(id);
    }

    // Nuevo método con paginación
    public Page<Boleta> listarBoletasPaginadas(Pageable pageable) {
        return boletaRepository.findAll(pageable);
    }
}
