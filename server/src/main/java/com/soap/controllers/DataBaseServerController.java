package com.soap.controllers;


import com.fasterxml.jackson.databind.JsonNode;
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

    public static void uploadFileBD(Map<String,String> resultado) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = "http://distribuidos2.bucaramanga.upb.edu.co/api/file";

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);

        
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(resultado);


            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            restTemplate.postForEntity(url, requestEntity, String.class);


        } catch (Exception e) {
            System.err.println("Error al obtener el token: " + e.getMessage());
        }
    }

    public static JsonNode getFile(String id_file, String id_user) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            
            String url = "http://distribuidos2.bucaramanga.upb.edu.co/api/file?user_id=" + id_user + "&file_id=" + id_file;


            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, JsonNode.class);

            JsonNode response = responseEntity.getBody();
         
            if (response == null || response.get("error").asBoolean() == true) {
                return null;
            }
            
            JsonNode list = response.get("msg");
            return list;

        } catch (Exception e) {
            System.err.println("Error al obtener el token: " + e.getMessage());
            return null;
        }
    }
    
    public static void registerUserBd(String id) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = "http://distribuidos2.bucaramanga.upb.edu.co/api/user";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> idMap = new HashMap<>();
            idMap.put("id", id);
           
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(idMap);


            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

           
            restTemplate.postForEntity(url, requestEntity, String.class);


        } catch (Exception e) {
            System.err.println("Error al obtener el token: " + e.getMessage());
        }
    }

}
