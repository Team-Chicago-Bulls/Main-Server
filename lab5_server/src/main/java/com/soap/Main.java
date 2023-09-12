package com.soap;

import com.soap.classes.SOAPCancion;

import jakarta.xml.ws.Endpoint;


public class Main 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        SOAPCancion implementacion = new SOAPCancion();
        String url = "http://localhost:8080/miServicioSOAP"; // Cambia la URL según tus preferencias

        Endpoint.publish(url, implementacion);

        System.out.println("Servidor SOAP iniciado en: " + url);

    }
}
