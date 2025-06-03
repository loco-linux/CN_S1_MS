package com.example.tienda.boletas.dto;

import java.util.List;

public class CarritoCompraRequest {
    private List<ItemCompra> productos;

    // Getters and setters

    public List<ItemCompra> getProductos() {
        return productos;
    }

    public void setProductos(List<ItemCompra> productos) {
        this.productos = productos;
    }

    public static class ItemCompra {
        private Long productoId;
        private Integer cantidad;

        
        public Long getProductoId() {
            return productoId;
        }
        public void setProductoId(Long productoId) {
            this.productoId = productoId;
        }
        public Integer getCantidad() {
            return cantidad;
        }
        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }
        
        // Getters and setters
    }
}
