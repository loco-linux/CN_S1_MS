package com.example.productos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.productos","com.example.boletas"})
public class ProductosServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductosServiceApplication.class, args);
	}

}
