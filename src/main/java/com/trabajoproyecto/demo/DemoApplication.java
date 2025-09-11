package com.trabajoproyecto.demo;

// Clase principal de la aplicación Spring Boot
// importaciones necesarias para iniciar la aplicación
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Anotación que indica que esta es una aplicación Spring Boot
@SpringBootApplication
public class DemoApplication {

	// Método principal que inicia la aplicación
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
