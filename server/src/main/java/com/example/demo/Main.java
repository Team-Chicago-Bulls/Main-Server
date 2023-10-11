package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.soap.classes.SOAPFileService;
import jakarta.xml.ws.Endpoint;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.soap.controllers")
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
		SOAPFileService implementacion = new SOAPFileService();
		String url = "http://0.0.0.0:8080/SOAP"; // Cambia la URL según tus preferencias
		System.out.println("Servidor SOAP iniciado en: " + url);
		Endpoint.publish(url, implementacion);

	}

}
