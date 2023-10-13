package com.soap.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import interfaces.RMIServiceAdapter;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;

import com.soap.classes.RMIServiceAdapterImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataBaseServerController {
    

    public static void registerUserBd(String id) {
        try {
            // Configura RestTemplate
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8000/api/user";

            // Crea un encabezado con el tipo de contenido
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> idMap = new HashMap<>();
            idMap.put("id", id);
           
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(idMap);


            // Crea una entidad HTTP con el encabezado y el cuerpo
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            // Realiza una solicitud POST al servidor de autenticación
            restTemplate.postForEntity(url, requestEntity, String.class);


        } catch (Exception e) {
            System.err.println("Error al obtener el token: " + e.getMessage());
        }
    }

}
