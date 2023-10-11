package com.soap;

import com.soap.classes.SOAPFileService;
import jakarta.xml.ws.Endpoint;

public class Main 
{
    public static void main( String[] args )
    {
       SOAPFileService implementacion = new SOAPFileService();
       String url = "http://0.0.0.0:8080/SOAP"; // Cambia la URL según tus preferencias
       System.out.println("Servidor SOAP iniciado en: " + url);
       Endpoint.publish(url, implementacion);

    }
}
