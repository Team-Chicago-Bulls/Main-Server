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
import java.util.concurrent.CompletableFuture;

public class DataBaseServerController {

    public DataBaseServerController() {

    }

    public void uploadFileBD(Map<String, String> resultado) {
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

    public void renameFileBD(String nombre, String file_id, String user_id) {
       
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = "http://distribuidos2.bucaramanga.upb.edu.co/api/file/name";

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = new HashMap<>();
            body.put("name", nombre);
            body.put("user_id", user_id);
            body.put("file_id", file_id);

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(body);

            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            String response = restTemplate.patchForObject(url, requestEntity, String.class);

            JsonNode jsonNode = objectMapper.readTree(response);

            if (jsonNode.get("error").asBoolean() == true) {
                System.out.println("Error al renombrar el archivo");
                
            } else {
                System.out.println("Archivo renombrado correctamente");
            }



        } catch (Exception e) {
            System.err.println("Error al renombrar el archivo: " + e.getMessage());
            
        }

        

    }

    public Map<String, Object> getFile(String id_file, String id_user) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = "http://distribuidos2.bucaramanga.upb.edu.co/api/file?user_id=" + id_user + "&file_id="
                    + id_file;

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);

            
              ResponseEntity<JsonNode> response = restTemplate.getForEntity(url,
              JsonNode.class, headers);
              
              JsonNode body = response.getBody();
              
              if (response == null || body.get("error").asBoolean() == true) {
              return null;
              }
              
              
              Map<String,Object> resultado = new HashMap<String,Object>();
              
              JsonNode list = body.get("msg");
              
              
              resultado.put("id", list.get(0).get("id").asText());
              resultado.put("name", list.get(0).get("name").asText());
              resultado.put("size", list.get(0).get("size").asText());
              resultado.put("nodo", list.get(0).get("nodo").asText());
              resultado.put("route", list.get(0).get("route").asText());
              resultado.put("nodo_backup", list.get(0).get("nodo_backup").asText());
              resultado.put("route_backup", list.get(0).get("route_backup").asText());
              
              
              
              
            return resultado;
             

        } catch (Exception e) {
            System.err.println("Error al obtener el token: " + e.getMessage());
            return null;
        }
    }

    public void registerUserBd(String id) {
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
